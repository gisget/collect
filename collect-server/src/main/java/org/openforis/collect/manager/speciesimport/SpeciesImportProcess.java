package org.openforis.collect.manager.speciesimport;

import static org.openforis.idm.model.species.Taxon.TaxonRank.FAMILY;
import static org.openforis.idm.model.species.Taxon.TaxonRank.FORM;
import static org.openforis.idm.model.species.Taxon.TaxonRank.GENUS;
import static org.openforis.idm.model.species.Taxon.TaxonRank.SPECIES;
import static org.openforis.idm.model.species.Taxon.TaxonRank.SUBSPECIES;
import static org.openforis.idm.model.species.Taxon.TaxonRank.VARIETY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.collect.io.exception.ParsingException;
import org.openforis.collect.io.metadata.parsing.ParsingError;
import org.openforis.collect.io.metadata.parsing.ParsingError.ErrorType;
import org.openforis.collect.io.metadata.species.SpeciesFileColumn;
import org.openforis.collect.io.parsing.CSVFileOptions;
import org.openforis.collect.manager.SpeciesManager;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.manager.process.AbstractProcess;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.CollectTaxonomy;
import org.openforis.collect.model.TaxonTree;
import org.openforis.collect.model.TaxonTree.Node;
import org.openforis.collect.persistence.SurveyStoreException;
import org.openforis.idm.metamodel.ReferenceDataSchema;
import org.openforis.idm.metamodel.ReferenceDataSchema.ReferenceDataDefinition;
import org.openforis.idm.metamodel.ReferenceDataSchema.ReferenceDataDefinition.Attribute;
import org.openforis.idm.metamodel.ReferenceDataSchema.TaxonomyDefinition;
import org.openforis.idm.model.species.Taxon;
import org.openforis.idm.model.species.Taxon.TaxonRank;
import org.openforis.idm.model.species.TaxonVernacularName;

/**
 * 
 * @author S. Ricci
 * 
 */
public class SpeciesImportProcess extends AbstractProcess<Void, SpeciesImportStatus> {

	private static final String RAW_SCIENTIFIC_NAME_TREE_NODE_METADATA = "rawScientificName";
	private static final String LINE_NUMBER_TREE_NODE_METADATA = "lineNumber";
	private static final String TAXONOMY_NOT_FOUND_ERROR_MESSAGE_KEY = "speciesImport.error.taxonomyNotFound";
	private static final String INVALID_FAMILY_NAME_ERROR_MESSAGE_KEY = "speciesImport.error.invalidFamilyName";
	private static final String INVALID_GENUS_NAME_ERROR_MESSAGE_KEY = "speciesImport.error.invalidGenusName";
	private static final String INVALID_SPECIES_NAME_ERROR_MESSAGE_KEY = "speciesImport.error.invalidSpeciesName";
	private static final String INVALID_SCIENTIFIC_NAME_ERROR_MESSAGE_KEY = "speciesImport.error.invalidScientificName";
	private static final String IMPORTING_FILE_ERROR_MESSAGE_KEY = "speciesImport.error.internalErrorImportingFile";
	
	private static final TaxonRank[] TAXON_RANKS = new TaxonRank[] {FAMILY, GENUS, SPECIES, SUBSPECIES, VARIETY, FORM};
	public static final String GENUS_SUFFIX = "sp.";

	private static Log LOG = LogFactory.getLog(SpeciesImportProcess.class);

	//input
	private SpeciesManager speciesManager;
	private SurveyManager surveyManager;
	private CollectSurvey survey;
	private int taxonomyId;
	private File file;
	private CSVFileOptions csvFileOptions;
	private boolean overwriteAll;
	
	//temp variables
	private String taxonomyName;
	private TaxonTree taxonTree;
	private SpeciesCSVReader reader;
	private List<SpeciesLine> lines;
	
	public SpeciesImportProcess(SurveyManager surveyManager, SpeciesManager speciesManager, 
			CollectSurvey survey, int taxonomyId, 
			File file, CSVFileOptions csvFileOptions, boolean overwriteAll) {
		super();
		this.surveyManager = surveyManager;
		this.speciesManager = speciesManager;
		this.survey = survey;
		this.taxonomyId = taxonomyId;
		this.file = file;
		this.csvFileOptions = csvFileOptions;
		this.overwriteAll = overwriteAll;
	}
	
	@Override
	public void init() {
		super.init();
		CollectTaxonomy taxonomy = speciesManager.loadTaxonomyById(survey, taxonomyId);
		taxonomyName = taxonomy.getName();
		lines = new ArrayList<SpeciesLine>();
		validateParameters();
	}
	
	protected void validateParameters() {
		if ( ! file.exists() && ! file.canRead() ) {
			status.error();
			status.setErrorMessage(IMPORTING_FILE_ERROR_MESSAGE_KEY);
		} else if ( taxonomyId <= 0 ) {
			status.error();
			status.setErrorMessage(TAXONOMY_NOT_FOUND_ERROR_MESSAGE_KEY);
		}
	}
	
	@Override
	protected void initStatus() {
		status = new SpeciesImportStatus(taxonomyId);
	}

	@Override
	public void startProcessing() throws Exception {
		super.startProcessing();
		processFile();
	}

	protected void processFile() throws IOException, SurveyStoreException {
		parseTaxonCSVLines(file);
		if ( status.isRunning() ) {
			processLines();
		}
		if ( status.isRunning() && ! status.hasErrors() ) {
			persistTaxa();
		} else {
			status.error();
		}
		if ( status.isRunning() ) {
			status.complete();
		}
	}

	protected void parseTaxonCSVLines(File file) {
		long currentRowNumber = 0;
		try {
			reader = new SpeciesCSVReader(file, csvFileOptions);
			reader.init();
			
			TaxonomyDefinition taxonomyDefinition = initializeTaxonomyDefinition();

			taxonTree = new TaxonTree(taxonomyDefinition);
			
			status.addProcessedRow(1);
			currentRowNumber = 2;
			while ( status.isRunning() ) {
				try {
					SpeciesLine line = reader.readNextLine();
					if ( line != null ) {
						lines.add(line);
					}
					if ( ! reader.isReady() ) {
						break;
					}
				} catch (ParsingException e) {
					status.addParsingError(currentRowNumber, e.getError());
				} finally {
					currentRowNumber ++;
				}
			}
			status.setTotal(reader.getLinesRead() + 1);
		} catch (ParsingException e) {
			status.error();
			status.addParsingError(1, e.getError());
		} catch (Exception e) {
			status.error();
			status.addParsingError(currentRowNumber, new ParsingError(ErrorType.IOERROR, e.getMessage()));
			LOG.error("Error importing species CSV file", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private TaxonomyDefinition initializeTaxonomyDefinition() {
		List<String> infoColumnNames = reader.getInfoColumnNames();
		TaxonomyDefinition taxonomyDefinition = new TaxonomyDefinition(taxonomyName);
		taxonomyDefinition.setAttributes(ReferenceDataDefinition.Attribute.fromNames(infoColumnNames));
		ReferenceDataSchema referenceDataSchema = survey.getReferenceDataSchema();
		referenceDataSchema.addTaxonomyDefinition(taxonomyDefinition);
		return taxonomyDefinition;
	}
	
	protected void processLines() {
		for (TaxonRank rank : TAXON_RANKS) {
			for (SpeciesLine line : lines) {
				long lineNumber = line.getLineNumber();
				if ( ! status.isRowProcessed(lineNumber) && ! status.isRowInError(lineNumber) ) {
					try {
						boolean processed = processLine(line, rank);
						if (processed ) {
							status.addProcessedRow(lineNumber);
						}
					} catch (ParsingException e) {
						status.addParsingError(lineNumber, e.getError());
					}
				}
			}
		}
	}
	
	protected boolean processLine(SpeciesLine line, TaxonRank rank) throws ParsingException {
		boolean mostSpecificRank = line.getRank() == rank;
		switch (rank) {
		case FAMILY:
			createTaxonFamily(line);
			return mostSpecificRank;
		case GENUS:
			createTaxonGenus(line);
			return mostSpecificRank;
		case SPECIES:
			createTaxonSpecies(line);
			return mostSpecificRank;
		case SUBSPECIES:
		case VARIETY:
		case FORM:
			Taxon parent = findParentTaxon(line);
			if ( ! mostSpecificRank || parent == null ) {
				return false;
			} else {
				createTaxon(line, rank, parent);
				return true;
			}
		default: 
			return false;
		}
	}

	protected void processVernacularNames(SpeciesLine line, Taxon taxon) {
		Map<String, List<String>> langToVernacularName = line.getLanguageToVernacularNames();
		Set<String> vernacularLangCodes = langToVernacularName.keySet();
		for (String langCode : vernacularLangCodes) {
			List<String> vernacularNames = langToVernacularName.get(langCode);
			for (String vernacularName : vernacularNames) {
				TaxonVernacularName taxonVN = new TaxonVernacularName();
				taxonVN.setLanguageCode(langCode);
				taxonVN.setVernacularName(vernacularName);
				taxonTree.addVernacularName(taxon, taxonVN);
			}
		}
	}

	protected void persistTaxa() throws SurveyStoreException {
		saveSurvey();
		CollectTaxonomy taxonomy = speciesManager.loadTaxonomyById(survey, taxonomyId);
		speciesManager.insertTaxons(taxonomy, taxonTree, overwriteAll);
	}
	
	@SuppressWarnings("deprecation")
	private void saveSurvey() throws SurveyStoreException {
		if ( survey.isTemporary() ) {
			surveyManager.save(survey);
		} else {
			surveyManager.updateModel(survey);
		}
	}

	private Taxon findParentTaxon(SpeciesLine line) throws ParsingException {
		TaxonRank rank = line.getRank();
		TaxonRank parentRank = rank.getParent();
		String scientificName;
		switch ( parentRank ) {
		case FAMILY:
			scientificName = line.getFamilyName();
			break;
		case GENUS:
			scientificName = line.getGenus();
			break;
		case SPECIES:
			scientificName = line.getSpeciesName();
			break;
		default:
			throw new RuntimeException("Unsupported rank");
		}
		Taxon result = taxonTree.findTaxonByScientificName(scientificName);
		return result;
	}
	
	protected Taxon createTaxonFamily(SpeciesLine line) throws ParsingException {
		String familyName = line.getFamilyName();
		if ( familyName == null ) {
			ParsingError error = new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), SpeciesFileColumn.SCIENTIFIC_NAME.getColumnName(), INVALID_FAMILY_NAME_ERROR_MESSAGE_KEY);
			throw new ParsingException(error);
		}
		return createTaxon(line, FAMILY, null, familyName);
	}
	
	protected Taxon createTaxonGenus(SpeciesLine line) throws ParsingException {
		String genus = line.getGenus();
		if ( genus == null ) {
			ParsingError error = new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), SpeciesFileColumn.SCIENTIFIC_NAME.getColumnName(), INVALID_GENUS_NAME_ERROR_MESSAGE_KEY);
			throw new ParsingException(error);
		}
		Taxon taxonFamily = createTaxonFamily(line);
		String normalizedScientificName = StringUtils.join(genus, " ", GENUS_SUFFIX);
		return createTaxon(line, GENUS, taxonFamily, normalizedScientificName);
	}

	protected Taxon createTaxonSpecies(SpeciesLine line) throws ParsingException {
		String speciesName;
		if ( line.getRank() == SPECIES ) {
			speciesName = line.getCanonicalScientificName();
		} else {
			speciesName = line.getSpeciesName();
		}
		if ( speciesName == null ) {
			ParsingError error = new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), SpeciesFileColumn.SCIENTIFIC_NAME.getColumnName(), INVALID_SPECIES_NAME_ERROR_MESSAGE_KEY);
			throw new ParsingException(error);
		}
		Taxon taxonGenus = createTaxonGenus(line);
		return createTaxon(line, SPECIES, taxonGenus, speciesName);
	}
	
	protected Taxon createTaxon(SpeciesLine line, TaxonRank rank, Taxon parent) throws ParsingException {
		String normalizedScientificName = line.getCanonicalScientificName();
		if ( normalizedScientificName == null ) {
			ParsingError error = new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), SpeciesFileColumn.SCIENTIFIC_NAME.getColumnName(), INVALID_SCIENTIFIC_NAME_ERROR_MESSAGE_KEY);
			throw new ParsingException(error);
		}
		return createTaxon(line, rank, parent, normalizedScientificName);
	}
	
	protected Taxon createTaxon(SpeciesLine line, TaxonRank rank, Taxon parent, String normalizedScientificName) throws ParsingException {
		boolean mostSpecificRank = line.getRank() == rank;
		Taxon taxon = taxonTree.findTaxonByScientificName(normalizedScientificName);
		boolean newTaxon = taxon == null;
		if ( newTaxon ) {
			taxon = new Taxon();
			taxon.setTaxonRank(rank);
			taxon.setScientificName(normalizedScientificName);
			
			Node node = taxonTree.addNode(parent, taxon);
			node.addMetadata(LINE_NUMBER_TREE_NODE_METADATA, line.getLineNumber());
			node.addMetadata(RAW_SCIENTIFIC_NAME_TREE_NODE_METADATA, line.getRawScientificName());
		} else if (mostSpecificRank) {
			checkDuplicateScientificName(line, parent, normalizedScientificName);
		}
		if ( mostSpecificRank ) {
			String code = line.getCode();
			Integer taxonId = line.getTaxonId();
			checkDuplicates(line, code, taxonId);
			taxon.setCode(code);
			taxon.setTaxonId(taxonId);
			
			TaxonomyDefinition taxonDefinition = survey.getReferenceDataSchema().getTaxonomyDefinition(taxonomyName);
			List<Attribute> taxonAttributes = taxonDefinition.getAttributes();
			for (Attribute attribute : taxonAttributes) {
				String value = line.getInfoAttribute(attribute.getName());
				taxon.addInfoAttribute(value);
			}
			taxonTree.updateNodeInfo(taxon);
			processVernacularNames(line, taxon);
		}
		return taxon;
	}

	protected void checkDuplicates(SpeciesLine line, String code,
			Integer taxonId) throws ParsingException {
		Node foundNode = null;
		foundNode = taxonTree.getNodeByTaxonId(taxonId);
		if ( foundNode != null ) {
			throwDuplicateRowParsingException(line, SpeciesFileColumn.NO, foundNode);
		}
		foundNode = taxonTree.getNodeByCode(code);
		if ( foundNode != null ) {
			throwDuplicateRowParsingException(line, SpeciesFileColumn.CODE, foundNode);
		}
	}

	protected void checkDuplicateScientificName(SpeciesLine line, Taxon parent,
			String normalizedScientificName) throws ParsingException {
		Node duplicateNode = taxonTree.getDuplicateScienfificNameNode(parent, normalizedScientificName);
		if ( duplicateNode != null ) {
			throwDuplicateRowParsingException(line, SpeciesFileColumn.SCIENTIFIC_NAME, duplicateNode);
		}
	}

	protected void throwDuplicateRowParsingException(SpeciesLine line, SpeciesFileColumn column, Node foundNode) throws ParsingException {
		ParsingError error = new ParsingError(ErrorType.DUPLICATE_VALUE, line.getLineNumber(), column.getColumnName());
		error.setMessageArgs(new String[] {foundNode.getMetadata(LINE_NUMBER_TREE_NODE_METADATA).toString()});
		throw new ParsingException(error);
	}

}
