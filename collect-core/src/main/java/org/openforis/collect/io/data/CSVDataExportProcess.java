package org.openforis.collect.io.data;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.util.IOUtils;
import org.openforis.collect.io.data.DataExportStatus.Format;
import org.openforis.collect.io.data.csv.AutomaticColumnProvider;
import org.openforis.collect.io.data.csv.CSVExportConfiguration;
import org.openforis.collect.io.data.csv.ColumnProvider;
import org.openforis.collect.io.data.csv.ColumnProviderChain;
import org.openforis.collect.io.data.csv.DataTransformation;
import org.openforis.collect.io.data.csv.ModelCsvWriter;
import org.openforis.collect.io.data.csv.NodePositionColumnProvider;
import org.openforis.collect.io.data.csv.PivotExpressionColumnProvider;
import org.openforis.collect.io.data.csv.SingleFieldAttributeColumnProvider;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.process.AbstractProcess;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.RecordFilter;
import org.openforis.collect.persistence.RecordPersistenceException;
import org.openforis.commons.io.OpenForisIOUtils;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.metamodel.Schema;
import org.openforis.idm.model.expression.InvalidExpressionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author S. Ricci
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CSVDataExportProcess extends AbstractProcess<Void, DataExportStatus> {
	
	private static Log LOG = LogFactory.getLog(CSVDataExportProcess.class);

	@Autowired
	private RecordManager recordManager;
	
	private File outputFile;
	private RecordFilter recordFilter;
	private Integer entityId;
	private boolean alwaysGenerateZipFile;
	private CSVExportConfiguration config;
	
	public CSVDataExportProcess() {
		alwaysGenerateZipFile = false;
		config = new CSVExportConfiguration();
	}
	
	@Override
	protected void initStatus() {
		this.status = new DataExportStatus(Format.CSV);		
	}
	
	@Override
	public void startProcessing() throws Exception {
		super.startProcessing();
		exportData();
	}
	
	private void exportData() throws Exception {
		BufferedOutputStream bufferedOutputStream = null;
		ZipOutputStream zipOS = null;
		if ( outputFile.exists() ) {
			outputFile.delete();
			outputFile.createNewFile();
		}
		try {
			status.setTotal(calculateTotal());
			FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			
			Collection<EntityDefinition> entities = getEntitiesToExport();
			
			if ( entities.size() == 1 && ! alwaysGenerateZipFile ) {
				//export entity into a single csv file 
				EntityDefinition entity = entities.iterator().next();
				exportData(bufferedOutputStream, entity.getId());
			} else {
				//export entities into a zip file containing different csv files
				zipOS = new ZipOutputStream(bufferedOutputStream);
				EntryNameGenerator entryNameGenerator = new EntryNameGenerator();
				for (EntityDefinition entity : entities) {
					String entryName = entryNameGenerator.generateEntryName(entity);
					ZipEntry entry = new ZipEntry(entryName);
					zipOS.putNextEntry(entry);
					exportData(zipOS, entity.getId());
					zipOS.closeEntry();
				}
			}
		} catch (Exception e) {
			status.error();
			status.setErrorMessage(e.getMessage());
			LOG.error(e.getMessage(), e);
			throw e;
		} finally {
			IOUtils.close(zipOS);
			IOUtils.close(bufferedOutputStream);

		}
		//System.out.println("Exported "+rowsCount+" rows from "+read+" records in "+(duration/1000)+"s ("+(duration/rowsCount)+"ms/row).");
	}

//	private String calculateOutputFileName() {
//		return "data.zip";
//		/*
//		StringBuilder sb = new StringBuilder();
//		sb.append(survey.getName());
//		sb.append("_");
//		sb.append(rootEntityName);
//		sb.append("_");
//		sb.append("csv_data");
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		String today = formatter.format(new Date());
//		sb.append(today);
//		sb.append(".zip");
//		return sb.toString();
//		*/
//	}

	private void exportData(OutputStream outputStream, int entityId) throws InvalidExpressionException, IOException, RecordPersistenceException {
		Writer outputWriter = new OutputStreamWriter(outputStream, OpenForisIOUtils.UTF_8);
		DataTransformation transform = getTransform(entityId);
		
		@SuppressWarnings("resource")
		//closing modelWriter will close referenced output stream
		ModelCsvWriter modelWriter = new ModelCsvWriter(outputWriter, transform);
		modelWriter.printColumnHeadings();
		
		CollectSurvey survey = recordFilter.getSurvey();
		Step step = recordFilter.getStepGreaterOrEqual();
		List<CollectRecord> summaries = recordManager.loadSummaries(recordFilter);
		for (CollectRecord s : summaries) {
			if ( status.isRunning() ) {
				CollectRecord record = recordManager.load(survey, s.getId(), step);
				modelWriter.printData(record);
				status.incrementProcessed();
			} else {
				break;
			}
		}
		modelWriter.flush();
	}
	
	private Collection<EntityDefinition> getEntitiesToExport() {
		final Collection<EntityDefinition> result = new ArrayList<EntityDefinition>();
		Schema schema = recordFilter.getSurvey().getSchema();
		if ( entityId == null ) {
			EntityDefinition rootEntity = schema.getRootEntityDefinition(recordFilter.getRootEntityId());
			rootEntity.traverse(new NodeDefinitionVisitor() {
				@Override
				public void visit(NodeDefinition node) {
					if ( node instanceof EntityDefinition && node.isMultiple() ) {
						result.add((EntityDefinition) node);
					}
				}
			});
		} else {
			EntityDefinition entity = (EntityDefinition) schema.getDefinitionById(entityId);
			result.add(entity);
		}
		return result;
	}
	
	private int calculateTotal() {
		int totalRecords = recordManager.countRecords(recordFilter);
		Collection<EntityDefinition> entitiesToExport = getEntitiesToExport();
		int result = totalRecords * entitiesToExport.size();
		return result;
	}

	protected DataTransformation getTransform(int entityId) throws InvalidExpressionException {
		List<ColumnProvider> columnProviders = new ArrayList<ColumnProvider>();
		
		CollectSurvey survey = recordFilter.getSurvey();
		Schema schema = survey.getSchema();
		EntityDefinition entityDefn = (EntityDefinition) schema.getDefinitionById(entityId);
		
		//entity children columns
		AutomaticColumnProvider entityColumnProvider = createEntityColumnProvider(entityDefn);

		//ancestor columns
		columnProviders.addAll(createAncestorsColumnsProvider(entityDefn));
		
		//position column
		if ( isPositionColumnRequired(entityDefn) ) {
			columnProviders.add(createPositionColumnProvider(entityDefn));
		}
		columnProviders.add(entityColumnProvider);
		
		//create data transformation
		ColumnProvider provider = new ColumnProviderChain(config, columnProviders);
		String axisPath = entityDefn.getPath();
		return new DataTransformation(axisPath, provider);
	}

	protected AutomaticColumnProvider createEntityColumnProvider(EntityDefinition entityDefn) {
		AutomaticColumnProvider entityColumnProvider = new AutomaticColumnProvider(config, "", entityDefn, null);
		return entityColumnProvider;
	}
	
	private List<ColumnProvider> createAncestorsColumnsProvider(EntityDefinition entityDefn) {
		List<ColumnProvider> columnProviders = new ArrayList<ColumnProvider>();
		EntityDefinition ancestorDefn = (EntityDefinition) entityDefn.getParentDefinition();
		int depth = 1;
		while ( ancestorDefn != null ) {
			ColumnProvider parentKeysColumnsProvider = createAncestorColumnProvider(ancestorDefn, depth);
			columnProviders.add(0, parentKeysColumnsProvider);
			ancestorDefn = ancestorDefn.getParentEntityDefinition();
			depth++;
		}
		return columnProviders;
	}
	
	private ColumnProvider createAncestorColumnProvider(EntityDefinition entityDefn, int depth) {
		List<ColumnProvider> providers = new ArrayList<ColumnProvider>();
		String pivotExpression = StringUtils.repeat("parent()", "/", depth);
		if ( config.isIncludeAllAncestorAttributes() ) {
			AutomaticColumnProvider ancestorEntityColumnProvider = new AutomaticColumnProvider(config, entityDefn.getName() + "_", entityDefn);
			providers.add(0, ancestorEntityColumnProvider);
		} else {
			//include only key attributes
			List<AttributeDefinition> keyAttrDefns = entityDefn.getKeyAttributeDefinitions();
			for (AttributeDefinition keyDefn : keyAttrDefns) {
				String columnName = calculateAncestorKeyColumnName(keyDefn, false);
				SingleFieldAttributeColumnProvider keyColumnProvider = new SingleFieldAttributeColumnProvider(config, keyDefn, columnName);
				providers.add(keyColumnProvider);
			}
			if ( isPositionColumnRequired(entityDefn) ) {
				ColumnProvider positionColumnProvider = createPositionColumnProvider(entityDefn);
				providers.add(positionColumnProvider);
			}
		}
		ColumnProvider result = new PivotExpressionColumnProvider(config, pivotExpression, providers.toArray(new ColumnProvider[0]));
		return result;
	}
	
	private boolean isPositionColumnRequired(EntityDefinition entityDefn) {
		return entityDefn.getParentDefinition() != null && entityDefn.isMultiple() && entityDefn.getKeyAttributeDefinitions().isEmpty();
	}
	
	private ColumnProvider createPositionColumnProvider(EntityDefinition entityDefn) {
		String columnName = calculatePositionColumnName(entityDefn);
		NodePositionColumnProvider columnProvider = new NodePositionColumnProvider(columnName);
		return columnProvider;
	}
	
	private String calculateAncestorKeyColumnName(AttributeDefinition attrDefn, boolean includeAllAncestors) {
		EntityDefinition parent = attrDefn.getParentEntityDefinition();
		return parent.getName() + "_" + attrDefn.getName();
	}
	
	private String calculatePositionColumnName(EntityDefinition nodeDefn) {
		return "_" + nodeDefn.getName() + "_position";
	}
	
	private static class EntryNameGenerator {
		
		private Set<String> entryNames;
		
		public EntryNameGenerator() {
			entryNames = new HashSet<String>();
		}
		
		public String generateEntryName(EntityDefinition entity) {
			String name = entity.getName() + ".csv";
			if ( entryNames.contains(name) ) {
				name = entity.getParentEntityDefinition().getName() + "_" + name;
			}
			entryNames.add(name);
			return name;
		}
	}

	public File getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public RecordFilter getRecordFilter() {
		return recordFilter;
	}
	
	public void setRecordFilter(RecordFilter recordFilter) {
		this.recordFilter = recordFilter;
	}
	
	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	public CSVExportConfiguration getConfig() {
		return config;
	}
	
	public void setConfig(CSVExportConfiguration config) {
		this.config = config;
	}

	public boolean isAlwaysGenerateZipFile() {
		return alwaysGenerateZipFile;
	}

	public void setAlwaysGenerateZipFile(boolean alwaysGenerateZipFile) {
		this.alwaysGenerateZipFile = alwaysGenerateZipFile;
	}

}

