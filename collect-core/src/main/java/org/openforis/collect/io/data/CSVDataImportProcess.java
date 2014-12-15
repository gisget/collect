/**
 * 
 */
package org.openforis.collect.io.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openforis.collect.io.ReferenceDataImportStatus;
import org.openforis.collect.io.data.DataLine.EntityIdentifier;
import org.openforis.collect.io.data.DataLine.EntityIdentifierDefinition;
import org.openforis.collect.io.data.DataLine.EntityKeysIdentifier;
import org.openforis.collect.io.data.DataLine.EntityPositionIdentifier;
import org.openforis.collect.io.data.DataLine.FieldValueKey;
import org.openforis.collect.io.exception.ParsingException;
import org.openforis.collect.io.metadata.parsing.ParsingError;
import org.openforis.collect.io.metadata.parsing.ParsingError.ErrorType;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.UserManager;
import org.openforis.collect.manager.process.AbstractProcess;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectRecord.Step;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.NodeAddChange;
import org.openforis.collect.model.NodeChange;
import org.openforis.collect.model.NodeChangeSet;
import org.openforis.collect.model.User;
import org.openforis.collect.persistence.RecordPersistenceException;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.CodeAttributeDefinition;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.CodeListItem;
import org.openforis.idm.metamodel.CodeListService;
import org.openforis.idm.metamodel.CoordinateAttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NumberAttributeDefinition;
import org.openforis.idm.metamodel.NumericAttributeDefinition;
import org.openforis.idm.metamodel.Schema;
import org.openforis.idm.metamodel.SpatialReferenceSystem;
import org.openforis.idm.metamodel.Survey;
import org.openforis.idm.metamodel.Unit;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.CodeAttribute;
import org.openforis.idm.model.CoordinateAttribute;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Field;
import org.openforis.idm.model.Node;
import org.openforis.idm.model.NumberAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author S. Ricci
 *
 */
@Component("csvDataImportProcess")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional
public class CSVDataImportProcess extends AbstractProcess<Void, ReferenceDataImportStatus<ParsingError>> {

	private static Log LOG = LogFactory.getLog(CSVDataImportProcess.class);

	private static final String CSV = "csv";
	private static final String IMPORTING_FILE_ERROR_MESSAGE_KEY = "csvDataImport.error.internalErrorImportingFile";
	private static final String NO_RECORD_FOUND_ERROR_MESSAGE_KEY = "csvDataImport.error.noRecordFound";
	private static final String MULTIPLE_RECORDS_FOUND_ERROR_MESSAGE_KEY = "csvDataImport.error.multipleRecordsFound";
	private static final String ONLY_NEW_RECORDS_ALLOWED_MESSAGE_KEY = "csvDataImport.error.onlyNewRecordsAllowed";
	private static final String MULTIPLE_PARENT_ENTITY_FOUND_MESSAGE_KEY = "csvDataImport.error.multipleParentEntityFound";
	private static final String PARENT_ENTITY_NOT_FOUND_MESSAGE_KEY = "csvDataImport.error.noParentEntityFound";
	private static final String UNIT_NOT_FOUND_MESSAGE_KEY = "csvDataImport.error.unitNotFound";
	private static final String SRS_NOT_FOUND_MESSAGE_KEY = "csvDataImport.error.srsNotFound";
	private static final String RECORD_NOT_IN_SELECTED_STEP_MESSAGE_KEY= "csvDataImport.error.recordNotInSelectedStep";
	private static final String NO_ROOT_ENTITY_SELECTED_ERROR_MESSAGE_KEY = "csvDataImport.error.noRootEntitySelected";
	private static final String NO_MODEL_VERSION_FOUND_ERROR_MESSAGE_KEY = "csvDataImport.error.noModelVersionFound";

	private static final String MULTIPLE_ATTRIBUTE_VALUES_SEPARATOR = ",";


	@Autowired
	private RecordManager recordManager;
	@Autowired
	private UserManager userManager;
	
	//parameters
	/**
	 * Input file
	 */
	private File file;
	/**
	 * Current survey
	 */
	private CollectSurvey survey;
	/**
	 * Record step that will be considered for insert or update
	 */
	private Step step;
	/**
	 * Entity definition that should be considered as the parent of each attribute in the csv file
	 */
	private int parentEntityDefinitionId;
	/**
	 * If true, records are validated after insert or update
	 */
	private boolean recordValidationEnabled;
	/**
	 * If true, only new records will be inserted and only root entities can be added
	 */
	private boolean insertNewRecords;
	/**
	 * When insertNewRecords is true, it indicates the name of the model version used during new record creation
	 */
	private String newRecordVersionName;
	
	/**
	 * If true, the process automatically creates ancestor multiple entities if they do not exist.
	 */
	private boolean createAncestorEntities;

	//transient variables
	private CollectRecord lastModifiedRecordSummary;
	private CollectRecord lastModifiedRecord;
	private User adminUser;

	public CSVDataImportProcess() {
		recordValidationEnabled = true;
		insertNewRecords = false;
	}
	
	@Override
	public void init() {
		super.init();
		validateParameters();
		adminUser = userManager.loadAdminUser();
	}
	
	@Override
	protected void initStatus() {
		status = new ReferenceDataImportStatus<ParsingError>();
	}
	
	protected void validateParameters() {
		if ( ! file.exists() || ! file.canRead() ) {
			status.error();
			status.setErrorMessage(IMPORTING_FILE_ERROR_MESSAGE_KEY);
		} else if ( insertNewRecords && survey.getSchema().getRootEntityDefinition(parentEntityDefinitionId) == null ) {
			status.error();
			status.setErrorMessage(NO_ROOT_ENTITY_SELECTED_ERROR_MESSAGE_KEY);
		} else if ( insertNewRecords && newRecordVersionName != null && survey.getVersion(newRecordVersionName) == null ) {
			status.error();
			status.setErrorMessage(NO_MODEL_VERSION_FOUND_ERROR_MESSAGE_KEY);
			status.setErrorMessageArgs(new String[]{newRecordVersionName});
		} else {
			String fileName = file.getName();
			String extension = FilenameUtils.getExtension(fileName);
			if ( !  CSV.equalsIgnoreCase(extension) ) {
				String errorMessage = "File type not supported" + extension;
				status.setErrorMessage(errorMessage);
				status.error();
				LOG.error("Error importing file: " + errorMessage);
			}
		}
	}
	
	@Override
	public void startProcessing() throws Exception {
		super.startProcessing();
		processFile();
	}

	protected void processFile() {
		long currentRowNumber = 0;
		DataCSVReader reader = null;
		try {
			EntityDefinition parentEntityDefn = getParentEntityDefinition();
			reader = new DataCSVReader(file, parentEntityDefn);
			reader.init();
			status.addProcessedRow(1);
			
			status.setTotal(reader.size());
			
			currentRowNumber = 1;
			while ( status.isRunning() ) {
				currentRowNumber ++;
				try {
					DataLine line = reader.readNextLine();
					if ( line != null ) {
						processLine(line);
					}
					if ( ! reader.isReady() ) {
						//end of file reached
						if ( step != null ) {
							saveLastModifiedRecord();
						}
						break;
					}
				} catch (ParsingException e) {
					status.addParsingError(currentRowNumber, e.getError());
				}
			}
			if ( status.hasErrors() ) {
				status.error();
			} else if ( status.isRunning() ) {
				status.complete();
			}
		} catch (ParsingException e) {
			status.error();
			status.addParsingError(1, e.getError());
		} catch (Exception e) {
			status.error();
			status.addParsingError(currentRowNumber, new ParsingError(ErrorType.IOERROR, e.toString()));
			LOG.error("Error importing CSV file", e);
		} finally {
			close(reader);
		}
	}

	private void processLine(DataLine line) throws RecordPersistenceException {
		if ( validateRecordKey(line) ) {
			if ( insertNewRecords ) {
				//create new record
				EntityDefinition rootEntityDefn = survey.getSchema().getRootEntityDefinition(parentEntityDefinitionId);
				CollectRecord record = recordManager.create(survey, rootEntityDefn.getName(), adminUser, newRecordVersionName);
				setRecordKeys(line, record);
				setValuesInRecord(line, record, Step.ENTRY);
				insertRecord(record);
			} else if ( step == null ) {
				CollectRecord recordSummary = loadRecordSummary(line);
				Step originalRecordStep = recordSummary.getStep();
				//set values in each step data
				for (Step currentStep : Step.values()) {
					if ( currentStep.compareTo(originalRecordStep) <= 0  ) {
						CollectRecord record = recordManager.load(survey, recordSummary.getId(), currentStep);
						setValuesInRecord(line, record, currentStep);
						//always save record when updating multiple record steps in the same process
						updateRecord(record, originalRecordStep, currentStep);
					}
				}
			} else {
				CollectRecord recordSummary = loadRecordSummary(line);
				Step originalRecordStep = recordSummary.getStep();
				if ( step.compareTo(originalRecordStep) <= 0  ) {
					CollectRecord record;
					if ( lastModifiedRecordSummary == null || ! recordSummary.getId().equals(lastModifiedRecordSummary.getId() ) ) {
						//record changed
						if ( lastModifiedRecordSummary != null ) {
							saveLastModifiedRecord();
						}
						record = recordManager.load(survey, recordSummary.getId(), step);
					} else {
						record = lastModifiedRecord;
					}
					setValuesInRecord(line, record, step);
					lastModifiedRecordSummary = recordSummary;
					lastModifiedRecord = record;
				} else {
					status.addParsingError(new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), (String) null, RECORD_NOT_IN_SELECTED_STEP_MESSAGE_KEY));
				}
			}
			status.addProcessedRow(line.getLineNumber());
		}
	}

	private void setRecordKeys(DataLine line, CollectRecord record) {
		EntityDefinition rootEntityDefn = record.getRootEntity().getDefinition();
		String[] recordKeyValues = line.getRecordKeyValues(rootEntityDefn);

		List<AttributeDefinition> keyAttributeDefinitions = rootEntityDefn.getKeyAttributeDefinitions();
		for ( int i = 0; i < keyAttributeDefinitions.size(); i ++ ) {
			AttributeDefinition keyDefn = keyAttributeDefinitions.get(i);
			Attribute<?, ?> keyAttr = (Attribute<?, ?>) record.findNodeByPath(keyDefn.getPath() ); //for record key attributes, absolute path must be equal to relative path
			setValueInField(keyAttr, keyDefn.getMainFieldName(), recordKeyValues[i], line.getLineNumber(), null);
		}
	}

	private void saveLastModifiedRecord() throws RecordPersistenceException {
		Step originalStep = lastModifiedRecordSummary.getStep();
		
		updateRecord(lastModifiedRecord, originalStep, step);
		
		if ( step.compareTo(originalStep) < 0 ) {
			//reset record step to the original one
			CollectRecord record = recordManager.load(survey, lastModifiedRecordSummary.getId(), originalStep);
			record.setStep(originalStep);
			
			updateRecord(record, originalStep, originalStep);
		}
	}
	
	private boolean validateRecordKey(DataLine line) {
		long currentRowNumber = line.getLineNumber();
		EntityDefinition parentEntityDefn = getParentEntityDefinition();
		EntityDefinition rootEntityDefn = parentEntityDefn.getRootEntity();
		String[] recordKeyValues = line.getRecordKeyValues(rootEntityDefn);
		List<CollectRecord> recordSummaries = recordManager.loadSummaries(survey, rootEntityDefn.getName(), recordKeyValues);
		String[] recordKeyColumnNames = DataCSVReader.getKeyAttributeColumnNames(
				parentEntityDefn,
				rootEntityDefn.getKeyAttributeDefinitions());
		String errorMessageKey = null;
		if ( insertNewRecords ) {
			if ( ! recordSummaries.isEmpty() ) {
				errorMessageKey = ONLY_NEW_RECORDS_ALLOWED_MESSAGE_KEY;
			}
		} else if ( recordSummaries.size() == 0 ) {
			errorMessageKey = NO_RECORD_FOUND_ERROR_MESSAGE_KEY;
		} else if ( recordSummaries.size() > 1 ) {
			errorMessageKey = MULTIPLE_RECORDS_FOUND_ERROR_MESSAGE_KEY;
		}
		if ( errorMessageKey == null ) {
			return true;
		} else {
			ParsingError parsingError = new ParsingError(ErrorType.INVALID_VALUE, 
					currentRowNumber, recordKeyColumnNames, errorMessageKey);
			parsingError.setMessageArgs(new String[]{StringUtils.join(recordKeyValues)});
			status.addParsingError(currentRowNumber, parsingError);
			return false;
		}
	}
	
	private CollectRecord loadRecordSummary(DataLine line) {
		EntityDefinition parentEntityDefn = getParentEntityDefinition();
		EntityDefinition rootEntityDefn = parentEntityDefn.getRootEntity();
		String[] recordKeyValues = line.getRecordKeyValues(rootEntityDefn);
		List<CollectRecord> recordSummaries = recordManager.loadSummaries(survey, rootEntityDefn.getName(), recordKeyValues);
		CollectRecord recordSummary = recordSummaries.get(0);
		return recordSummary;
	}

	private boolean setValuesInRecord(DataLine line, CollectRecord record, Step step) {
		//LOG.info("Setting values in record: " + record.getId() + "[" + record.getRootEntityKeyValues() + "]" + " step: " + step);
		record.setStep(step);
		Entity parentEntity = getOrCreateParentEntity(record, line);
		if ( parentEntity == null ) {
			//TODO add parsing error?
			return false;
		} else {
			setValuesInAttributes(parentEntity, line);
			return true;
		}
	}

	private void setValuesInAttributes(Entity parentEntity, DataLine line) {
		setValuesInAttributes(parentEntity, line.getFieldValues(), line.getColumnNamesByField(), line.getLineNumber());
	}
	
	private void setValuesInAttributes(Entity ancestorEntity, Map<FieldValueKey, String> fieldValues, 
			Map<FieldValueKey, String> colNameByField, long row) {
		Set<Entry<FieldValueKey,String>> entrySet = fieldValues.entrySet();
		for (Entry<FieldValueKey, String> entry : entrySet) {
			FieldValueKey fieldValueKey = entry.getKey();
			String strValue = entry.getValue();
			EntityDefinition ancestorDefn = ancestorEntity.getDefinition();
			Schema schema = ancestorDefn.getSchema();
			AttributeDefinition attrDefn = (AttributeDefinition) schema.getDefinitionById(fieldValueKey.getAttributeDefinitionId());
			String fieldName = fieldValueKey.getFieldName();
			Entity parentEntity = getOrCreateParentEntity(ancestorEntity, attrDefn);
			String colName = colNameByField.get(fieldValueKey);
			if ( attrDefn.isMultiple() ) {
				if ( attrDefn instanceof CodeAttributeDefinition ) {
					setValuesInMultipleCodeAttribute(parentEntity, (CodeAttributeDefinition) attrDefn, fieldName, strValue, colName, row);
				} else {
					setValuesInMultipleAttribute(parentEntity, attrDefn, fieldName,
							strValue, colName, row);
				}
			} else {
				setValueInField(parentEntity, attrDefn, 0, fieldName,
						strValue, colName, row);
			}
		}
	}

	private void setValuesInMultipleCodeAttribute(Entity parentEntity,
			CodeAttributeDefinition attrDefn, String fieldName, String strValues,
			String colName, long row) {
		if ( fieldName.equals(CodeAttributeDefinition.QUALIFIER_FIELD) ) {
			CodeAttribute codeAttr = getQualifiableCodeAttribute(parentEntity, attrDefn);
			if ( codeAttr == null ) {
				setValuesInMultipleAttribute(parentEntity, (AttributeDefinition) attrDefn, fieldName, strValues, colName, row);
			} else {
				codeAttr.getQualifierField().setValue(strValues);
			}
		} else {
			setValuesInMultipleAttribute(parentEntity, (AttributeDefinition) attrDefn, fieldName, strValues, colName, row);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private CodeAttribute getQualifiableCodeAttribute(Entity parentEntity, CodeAttributeDefinition attrDefn) {
		CodeListItem qualifiableItem = getQualifiableItem(attrDefn);
		if ( qualifiableItem != null ) {
			List attributes = parentEntity.getAll(attrDefn);
			CodeAttribute codeAttr = findCodeAttributeByCode(qualifiableItem.getCode(), attributes);
			if ( codeAttr != null ) {
				return codeAttr;
			}
		}
		return null;
	}

	private CodeAttribute findCodeAttributeByCode(String code, List<CodeAttribute> attributes) {
		for (CodeAttribute codeAttr: attributes) {
			if ( code.equals(codeAttr.getValue().getCode()) ) {
				return codeAttr;
			}
		}
		return null;
	}

	private CodeListItem getQualifiableItem(CodeAttributeDefinition attrDefn) {
		CodeListService codeListService = attrDefn.getSurvey().getContext().getCodeListService();
		CodeList list = attrDefn.getList();
		if(codeListService.hasQualifiableItems(list) ) {
			List<CodeListItem> items = codeListService.loadRootItems(list);
			for (CodeListItem item : items) {
				if ( item.isQualifiable() ) {
					return item;
				}
			}
		}
		return null;
	}
	
	private void setValuesInMultipleAttribute(Entity parentEntity,
			AttributeDefinition attrDefn, String fieldName, String strValues,
			String colName, long row) {
		int newValuesCount;
		if ( StringUtils.isBlank(strValues) ) {
			newValuesCount = 0;
		} else {
			String[] splittedValues = strValues.split(MULTIPLE_ATTRIBUTE_VALUES_SEPARATOR);
			newValuesCount = splittedValues.length;
			for (int i = 0; i < newValuesCount; i++) {
				String strVal = splittedValues[i].trim();
				setValueInField(parentEntity, attrDefn, i, fieldName,
						strVal, colName, row);
			}
		}
		//remove old attributes
		int totalCount = parentEntity.getCount(attrDefn);
		if ( totalCount > newValuesCount ) {
			for (int i = totalCount - 1; i >= newValuesCount; i--) {
				Attribute<?, ?> attr = (Attribute<?, ?>) parentEntity.get(attrDefn, i);
				boolean toBeDeleted = false;
				if ( i == 0 ) {
					//do not delete attribute if it's empty but it has remarks or field symbols
					if ( ! attr.hasData() )  {
						toBeDeleted = true;
					}
				} else {
					toBeDeleted = true;
				}
				if ( toBeDeleted ) {
					recordManager.deleteNode(attr);
				}
			}
		}
	}

	private void setValueInField(Entity parentEntity,
			AttributeDefinition attrDefn, int index, String fieldName,
			String value, String colName, long row) {
		String attrName = attrDefn.getName();
		Attribute<?, ?> attr = (Attribute<?, ?>) parentEntity.get(attrDefn, index);
		if ( attr == null ) {
			attr = (Attribute<?, ?>) performNodeAdd(parentEntity, attrName);
		}
		try {
			setValueInField(attr, fieldName, value, row, colName);
		} catch ( Exception e) {
			status.addParsingError(new ParsingError(ErrorType.INVALID_VALUE, row, colName));
		}
	}
	
	private void setValueInField(Attribute<?, ?> attr, String fieldName, String value, long row, String colName) {
		if ( attr instanceof NumberAttribute && 
				(fieldName.equals(NumberAttributeDefinition.UNIT_FIELD) ||
				fieldName.equals(NumberAttributeDefinition.UNIT_NAME_FIELD)) ) {
			setUnitField(attr, value, row, colName);
		} else if ( attr instanceof CoordinateAttribute && fieldName.equals(CoordinateAttributeDefinition.SRS_FIELD_NAME) ) {
			setSRSIdField(attr, value, row, colName);
		} else {
			@SuppressWarnings("unchecked")
			Field<Object> field = (Field<Object>) attr.getField(fieldName);
			Object fieldValue = field.parseValue(value);
			recordManager.updateField(field, fieldValue);
		}
	}

	private void setSRSIdField(Attribute<?, ?> attr, String value, long row,
			String colName) {
		boolean valid = true;
		if ( StringUtils.isNotBlank(value) ) {
			//check SRS id validity
			Survey survey = attr.getSurvey();
			SpatialReferenceSystem srs = survey.getSpatialReferenceSystem(value);
			if ( srs == null ) {
				ParsingError parsingError = new ParsingError(ErrorType.INVALID_VALUE, row, colName, SRS_NOT_FOUND_MESSAGE_KEY);
				parsingError.setMessageArgs(new String[]{value});
				status.addParsingError(parsingError);
				valid = false;
			}
		}
		if ( valid ) {
			Field<String> field = ((CoordinateAttribute) attr).getSrsIdField();
			recordManager.updateField(field, value);
		}
	}

	private void setUnitField(Attribute<?, ?> attr, String value, long row,
			String colName) {
		if ( StringUtils.isBlank(value) ) {
			((NumberAttribute<?, ?>) attr).setUnit(null);
		} else {
			Survey survey = attr.getSurvey();
			Unit unit = survey.getUnit(value);
			NumericAttributeDefinition defn = (NumericAttributeDefinition) attr.getDefinition();
			if ( unit == null || ! defn.getUnits().contains(unit) ) {
				ParsingError parsingError = new ParsingError(ErrorType.INVALID_VALUE, row, colName, UNIT_NOT_FOUND_MESSAGE_KEY);
				parsingError.setMessageArgs(new String[]{value});
				status.addParsingError(parsingError);
			} else {
				Field<Integer> field = ((NumberAttribute<?, ?>) attr).getUnitField();
				recordManager.updateField(field, unit.getId());
			}
		}
	}

	private Entity getOrCreateParentEntity(Entity ancestorEntity, AttributeDefinition attrDefn) {
		EntityDefinition ancestorEntityDefn = ancestorEntity.getDefinition();
		List<EntityDefinition> attributeAncestors = attrDefn.getAncestorEntityDefinitions();
		int indexOfAncestorEntity = attributeAncestors.indexOf(ancestorEntityDefn);
		if ( indexOfAncestorEntity < 0 ) {
			throw new IllegalArgumentException("AttributeDefinition is not among the ancestor entity descendants");
		} else if ( indexOfAncestorEntity == attributeAncestors.size() - 1 ) {
			return ancestorEntity;
		} else {
			Entity currentParent = ancestorEntity;
			List<EntityDefinition> nearestAncestors = attributeAncestors.subList(indexOfAncestorEntity + 1, attributeAncestors.size());
			for (EntityDefinition ancestor : nearestAncestors) {
				if ( currentParent.getCount(ancestor) == 0 ) {
					Entity newNode = (Entity) ancestor.createNode();
					currentParent.add(newNode);
					currentParent = newNode;
				} else {
					currentParent = (Entity) currentParent.getChild(ancestor);
				}
			}
			return currentParent;
		}
	}

	private void updateRecord(CollectRecord record, Step originalRecordStep, Step dataStep) throws RecordPersistenceException {
		record.setModifiedDate(new Date());
		record.setModifiedBy(adminUser);
		
		if ( dataStep == Step.ANALYSIS ) {
			record.setStep(Step.CLEANSING);
			recordManager.save(record);
			record.setStep(Step.ANALYSIS);
		}
		recordManager.save(record);
	}
	
	private void insertRecord(CollectRecord record) throws RecordPersistenceException {
		recordManager.save(record);
	}
	
//	@SuppressWarnings("unchecked")
//	private <T extends Value> void setValueInAttribute(Attribute<?, T> attr, String strVal) {
//		T val;
//		if ( attr instanceof DateAttribute ) {
//			val = (T) Date.parseDate(strVal);
//		} else if ( attr instanceof CodeAttribute ) {
//			val = (T) new Code(strVal);
//		} else if ( attr instanceof CoordinateAttribute ) {
//			val = (T) Coordinate.parseCoordinate(strVal);
//		} else if ( attr instanceof TextAttribute ) {
//			val = (T) new TextValue(strVal);
//		} else {
//			throw new UnsupportedOperationException("Attribute type not supported: " + attr.getClass().getName());
//		}
//		attr.setValue(val);
//	}

	private Entity getOrCreateParentEntity(CollectRecord record, DataLine line) {
		Survey survey = record.getSurvey();
		Schema schema = survey.getSchema();
		EntityDefinition parentEntityDefn = (EntityDefinition) schema.getDefinitionById(parentEntityDefinitionId);
		Entity rootEntity = record.getRootEntity();
		Entity currentParent = rootEntity;
		List<EntityDefinition> ancestorEntityDefns = parentEntityDefn.getAncestorEntityDefinitions();
		ancestorEntityDefns.add(parentEntityDefn);
		//skip the root entity
		for (int i = 1; i < ancestorEntityDefns.size(); i++) {
			EntityDefinition ancestorDefn = ancestorEntityDefns.get(i);
			String ancestorName = ancestorDefn.getName();
			EntityIdentifier<?> identifier = line.getAncestorIdentifier(ancestorDefn.getId());
			Entity childEntity;
			if ( ancestorDefn.isMultiple() ) {
				List<Entity> childEntities = findChildEntities(currentParent, ancestorName, identifier);
				switch ( childEntities.size() ) {
				case 0:
					if ( createAncestorEntities || ancestorDefn == parentEntityDefn ) {
						childEntity = createChildEntity(currentParent, ancestorName, identifier, line.getColumnNamesByField(), line.getLineNumber());
					} else {
						status.addParsingError(createParentEntitySearchError(record, line, identifier, PARENT_ENTITY_NOT_FOUND_MESSAGE_KEY));
						return null;
					}
					break;
				case 1:
					childEntity = childEntities.get(0);
					break;
				default:
					status.addParsingError(createParentEntitySearchError(record,
							line, identifier, MULTIPLE_PARENT_ENTITY_FOUND_MESSAGE_KEY));
					return null;
				}
			} else {
				if ( currentParent.getCount(ancestorDefn) == 0 ) {
					Node<?> newNode = ancestorDefn.createNode();
					currentParent.add(newNode);
				}
				childEntity = (Entity) currentParent.getChild(ancestorDefn);
			}
			currentParent = childEntity;
		}
		return currentParent;
	}

	private List<Entity> findChildEntities(Entity currentParent, String childName, EntityIdentifier<?> identifier) {
		if ( identifier instanceof EntityPositionIdentifier ) {
			int position = ((EntityPositionIdentifier) identifier).getPosition();
			if ( currentParent.getCount(childName) >= position ) {
				ArrayList<Entity> result = new ArrayList<Entity>();
				Entity child = (Entity) currentParent.get(childName, position - 1);
				result.add(child);
				return result;
			} else {
				return Collections.emptyList();
			}
		} else {
			EntityDefinition parentDefn = currentParent.getDefinition();
			EntityDefinition childDefn = parentDefn.getChildDefinition(childName, EntityDefinition.class);
			List<AttributeDefinition> keyDefns = childDefn.getKeyAttributeDefinitions();
			String[] keys = new String[keyDefns.size()];
			for (int i = 0; i < keyDefns.size(); i++) {
				AttributeDefinition keyDefn = keyDefns.get(i);
				String key = ((EntityKeysIdentifier) identifier).getKeyValue(keyDefn.getId());
				keys[i] = key;
			}
			return currentParent.findChildEntitiesByKeys(childDefn, keys);
		}
	}
	
	private Entity createChildEntity(Entity currentParent, String childName,
			EntityIdentifier<?> identifier,
			Map<FieldValueKey, String> colNamesByField, long row) {
		if ( identifier instanceof EntityPositionIdentifier ) {
			int position = ((EntityPositionIdentifier) identifier).getPosition();
			if ( position == currentParent.getCount(childName) + 1 ) {
				Entity entity = (Entity) performNodeAdd(currentParent, childName);
				return entity;
			} else {
				throw new IllegalArgumentException(
						String.format("Trying to create child in a invalid position: row=%d path=%s[%d]",
								row, 
								currentParent.getPath() + "/" + childName,
								position));
			}
		} else {
			Entity entity = (Entity) performNodeAdd(currentParent, childName);
			String[] keyValues = ((EntityKeysIdentifier) identifier).getKeyValues();
			setKeyValues(entity, keyValues, colNamesByField, row);
			return entity;
		}
	}

	private Node<?> performNodeAdd(Entity parent, String childName) {
		NodeChangeSet changeSet = recordManager.addNode(parent, childName);
		for (NodeChange<?> nodeChange : changeSet.getChanges()) {
			if ( nodeChange instanceof NodeAddChange ) {
				return nodeChange.getNode();
			}
		}
		throw new RuntimeException(String.format("Error adding new entity with name %s to parent %s", childName, parent.getPath()));
	}

	private ParsingError createParentEntitySearchError(CollectRecord record, DataLine line, 
			EntityIdentifier<?> identifier, String messageKey) {
		EntityIdentifierDefinition identifierDefn = identifier.getDefinition();
		Survey survey = record.getSurvey();
		Schema schema = survey.getSchema();
		EntityDefinition parentEntityDefn = (EntityDefinition) schema.getDefinitionById(identifierDefn.getEntityDefinitionId());

		String[] colNames = DataCSVReader.getKeyAttributeColumnNames(parentEntityDefn, parentEntityDefn.getKeyAttributeDefinitions());
		ParsingError error = new ParsingError(ErrorType.INVALID_VALUE, line.getLineNumber(), colNames, messageKey);
		List<String> recordKeys = record.getRootEntityKeyValues();
		CollectionUtils.filter(recordKeys, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return StringUtils.isNotBlank((String) object);
			}
		});
		String jointRecordKeys = StringUtils.join(recordKeys, ", ");
		String jointParentEntityKeys = identifier instanceof EntityPositionIdentifier ? 
				"[" + ((EntityPositionIdentifier) identifier).getPosition() + "]" :
				StringUtils.join(((EntityKeysIdentifier) identifier).getKeyValues(), ", ");
		error.setMessageArgs(new String[]{parentEntityDefn.getName(), jointParentEntityKeys, jointRecordKeys});
		return error;
	}
	
	private void setKeyValues(Entity entity, String[] values, Map<FieldValueKey, String> colNamesByField, long row) {
		//create key attribute values by name
		Map<FieldValueKey, String> keyValuesByField = new HashMap<FieldValueKey, String>();
		EntityDefinition entityDefn = entity.getDefinition();
		List<AttributeDefinition> keyDefns = entityDefn.getKeyAttributeDefinitions();
		for (int i = 0; i < keyDefns.size(); i++) {
			AttributeDefinition keyDefn = keyDefns.get(i);
			keyValuesByField.put(new FieldValueKey(keyDefn), values[i]);
		}
		setValuesInAttributes(entity, keyValuesByField, colNamesByField, row);
	}

	private void close(DataCSVReader reader) {
		try {
			if ( reader != null ) {
				reader.close();
			}
		} catch (IOException e) {
			LOG.error("Error closing reader", e);
		}
	}

	private EntityDefinition getParentEntityDefinition() {
		return (EntityDefinition) survey.getSchema().getDefinitionById(parentEntityDefinitionId);
	}

	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public CollectSurvey getSurvey() {
		return survey;
	}
	
	public void setSurvey(CollectSurvey survey) {
		this.survey = survey;
	}
	
	public Step getStep() {
		return step;
	}
	
	public void setStep(Step step) {
		this.step = step;
	}

	public int getParentEntityDefinitionId() {
		return parentEntityDefinitionId;
	}
	
	public void setParentEntityDefinitionId(int parentEntityDefinitionId) {
		this.parentEntityDefinitionId = parentEntityDefinitionId;
	}
	
	public boolean isRecordValidationEnabled() {
		return recordValidationEnabled;
	}
	
	public void setRecordValidationEnabled(boolean recordValidationEnabled) {
		this.recordValidationEnabled = recordValidationEnabled;
	}

	public boolean isInsertNewRecords() {
		return insertNewRecords;
	}
	
	public void setInsertNewRecords(boolean insertNewRecords) {
		this.insertNewRecords = insertNewRecords;
	}
	
	public String getNewRecordVersionName() {
		return newRecordVersionName;
	}
	
	public void setNewRecordVersionName(String newRecordVersionName) {
		this.newRecordVersionName = newRecordVersionName;
	}
	
	public boolean isCreateAncestorEntities() {
		return createAncestorEntities;
	}
	
	public void setCreateAncestorEntities(boolean createAncestorEntities) {
		this.createAncestorEntities = createAncestorEntities;
	}
	
	static class ImportException extends Exception {

		private static final long serialVersionUID = 1L;

		public ImportException() {
			super();
		}
		
	}
}
