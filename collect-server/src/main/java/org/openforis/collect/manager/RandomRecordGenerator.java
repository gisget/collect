package org.openforis.collect.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.collect.model.EntityAddChange;
import org.openforis.collect.model.NodeChange;
import org.openforis.collect.model.NodeChangeSet;
import org.openforis.collect.model.RecordFilter;
import org.openforis.collect.model.RecordUpdater;
import org.openforis.collect.model.SamplingDesignItem;
import org.openforis.collect.model.SamplingDesignSummaries;
import org.openforis.collect.model.User;
import org.openforis.commons.collection.Visitor;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.CodeAttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.EntityDefinition.TraversalType;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.Code;
import org.openforis.idm.model.CodeAttribute;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RandomRecordGenerator {
	
	@Autowired
	private RecordManager recordManager;
	@Autowired
	private SurveyManager surveyManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private SamplingDesignManager samplingDesignManager;
	
	private RecordUpdater recordUpdater = new RecordUpdater();
	
	@Transactional
	public CollectRecord generate(int surveyId, Parameters parameters) {
		CollectSurvey survey = surveyManager.getById(surveyId);
		User user = userManager.loadById(parameters.getUserId());
		
		List<String> recordKey = provideRandomLessMeasuredRecordKey(survey, user);
		
		CollectRecord record = createRecord(survey, user);
		
		setRecordKeyValues(record, recordKey);
		
		if (parameters.isAddSecondLevelEntities()) {
			addSecondLevelEntities(record, recordKey);
		}
		recordManager.save(record);
		return record;
	}

	private CollectRecord createRecord(CollectSurvey survey, User user) {
		EntityDefinition rootEntityDef = survey.getSchema().getFirstRootEntityDefinition();
		String rootEntityName = rootEntityDef.getName();
		CollectRecord record = recordManager.create(survey, rootEntityName, user, null);
		return record;
	}

	private List<String> provideRandomLessMeasuredRecordKey(final CollectSurvey survey, User user) {
		Map<List<String>, Integer> recordMeasurementsByKey = calculateRecordMeasurementsByKey(survey, user);
		
		if (recordMeasurementsByKey.isEmpty()) {
			throw new IllegalStateException(String.format("Sampling design data not defined for survey %s", survey.getName()));
		}
		Integer minMeasurements = Collections.min(recordMeasurementsByKey.values());
		//do not consider measurements different from minimum measurement
		Iterator<Entry<List<String>, Integer>> iterator = recordMeasurementsByKey.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<List<String>, Integer> entry = iterator.next();
			if (entry.getValue() != minMeasurements) {
				iterator.remove();
			}
		}
		//randomly select one record key among the ones with minimum measurements
		List<List<String>> recordKeys = new ArrayList<List<String>>(recordMeasurementsByKey.keySet());
		int recordKeyIdx = new Double(Math.floor(Math.random() * recordKeys.size())).intValue();
		List<String> recordKey = recordKeys.get(recordKeyIdx);
		return recordKey;
	}

	private void addSecondLevelEntities(CollectRecord record, List<String> recordKey) {
		CollectSurvey survey = (CollectSurvey) record.getSurvey();
		List<SamplingDesignItem> secondLevelSamplingPointItems = samplingDesignManager.loadChildItems(survey.getId(), recordKey);
		List<CodeAttributeDefinition> samplingPointDataCodeAttributeDefs = findSamplingPointCodeAttributes(survey);
		if (! secondLevelSamplingPointItems.isEmpty() && samplingPointDataCodeAttributeDefs.size() > 1) {
			int levelIndex = 1;
			for (SamplingDesignItem samplingDesignItem : secondLevelSamplingPointItems) {
				CodeAttributeDefinition levelKeyDef = samplingPointDataCodeAttributeDefs.get(levelIndex);
				EntityDefinition levelEntityDef = levelKeyDef.getParentEntityDefinition();
				Entity parentLevelEntity = record.getRootEntity();
				NodeChangeSet addEntityChangeSet = recordUpdater.addEntity(parentLevelEntity, levelEntityDef);
				Entity entity = getAddedEntity(addEntityChangeSet);
				CodeAttribute keyAttr = entity.getChild(levelKeyDef);
				recordUpdater.updateAttribute(keyAttr, new Code(samplingDesignItem.getLevelCode(levelIndex + 1)));
			}
		}
	}

	private CollectSurvey setRecordKeyValues(CollectRecord record, List<String> recordKey) {
		CollectSurvey survey = (CollectSurvey) record.getSurvey();
		List<AttributeDefinition> keyAttributeDefs = getNonMeasurementKeyDefs(survey);
		//TODO update measurement attribute with... username?
		for (int i = 0; i < keyAttributeDefs.size(); i++) {
			String keyPart = recordKey.get(i);
			AttributeDefinition keyAttrDef = keyAttributeDefs.get(i);
			Attribute<?,Value> keyAttribute = record.findNodeByPath(keyAttrDef.getPath());
			recordUpdater.updateAttribute(keyAttribute, keyAttrDef.createValue(keyPart));
		}
		return survey;
	}

	private Map<List<String>, Integer> calculateRecordMeasurementsByKey(CollectSurvey survey, final User user) {
		final List<AttributeDefinition> nonMeasurementKeyDefs = getNonMeasurementKeyDefs(survey);
		final Map<List<String>, Integer> measurementsByRecordKey = new HashMap<List<String>, Integer>();
		recordManager.visitSummaries(new RecordFilter(survey), null, new Visitor<CollectRecord>() {
			public void visit(CollectRecord summary) {
				if (summary.getCreatedBy().getId() != user.getId()) {
					List<String> keys = summary.getRootEntityKeyValues();
					List<String> nonMeasurementKeys;
					if (keys.size() > nonMeasurementKeyDefs.size()) {
						nonMeasurementKeys = keys.subList(0, nonMeasurementKeyDefs.size() - 1);
					} else {
						nonMeasurementKeys = keys;
					}
					Integer measurements = measurementsByRecordKey.get(nonMeasurementKeys);
					if (measurements == null) {
						measurements = 1;
					} else {
						measurements += 1;
					}
					measurementsByRecordKey.put(nonMeasurementKeys, measurements);
				}
			}
		});
		List<AttributeDefinition> nonMeasurementKeyAttrDefs = getNonMeasurementKeyDefs(survey);
		
		SamplingDesignSummaries samplingPoints = samplingDesignManager.loadBySurvey(survey.getId(), nonMeasurementKeyAttrDefs.size());
		for (SamplingDesignItem item : samplingPoints.getRecords()) {
			if (item.getLevelCodes().size() == nonMeasurementKeyAttrDefs.size()) {
				List<String> key = item.getLevelCodes().subList(0, nonMeasurementKeyAttrDefs.size());
				Integer measurements = measurementsByRecordKey.get(key);
				if (measurements == null) {
					measurementsByRecordKey.put(key, 0);
				}
			}
		}
		return measurementsByRecordKey;
	}

	private List<CodeAttributeDefinition> findSamplingPointCodeAttributes(final CollectSurvey survey) {
		EntityDefinition rootEntityDef = survey.getSchema().getFirstRootEntityDefinition();
		final List<CodeAttributeDefinition> samplingPointDataCodeAttributeDefs = new ArrayList<CodeAttributeDefinition>();
		rootEntityDef.traverse(new NodeDefinitionVisitor() {
			public void visit(NodeDefinition def) {
				if (def instanceof CodeAttributeDefinition 
						&& ((CodeAttributeDefinition) def).getList().equals(survey.getSamplingDesignCodeList())) {
					samplingPointDataCodeAttributeDefs.add((CodeAttributeDefinition) def);
				}
			}
		}, TraversalType.BFS);
		return samplingPointDataCodeAttributeDefs;
	}
	
	private Entity getAddedEntity(NodeChangeSet changeSet) {
		List<NodeChange<?>> changes = changeSet.getChanges();
		for (NodeChange<?> nodeChange : changes) {
			if (nodeChange instanceof EntityAddChange) {
				Entity entity = (Entity) nodeChange.getNode();
				return entity;
			}
		}
		throw new IllegalArgumentException("Cannot find added entity in node change set");
	}
	
	private List<AttributeDefinition> getNonMeasurementKeyDefs(CollectSurvey survey) {
		EntityDefinition rootEntityDef = survey.getSchema().getFirstRootEntityDefinition();
		List<AttributeDefinition> keyAttrDefs = rootEntityDef.getKeyAttributeDefinitions();
		List<AttributeDefinition> nonMeasurementKeyAttrDefs = new ArrayList<AttributeDefinition>();
		for (AttributeDefinition keyAttrDef : keyAttrDefs) {
			if (! survey.getAnnotations().isMeasurementAttribute(keyAttrDef)) {
				nonMeasurementKeyAttrDefs.add(keyAttrDef);
			}
		}
		return nonMeasurementKeyAttrDefs;
	}
	
	public static class Parameters {
		
		private int userId;
		private boolean addSecondLevelEntities = false;
		private Integer rootEntityId; //optional: default to first root entity
		private Integer versionId; //optional: default to last version

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}
		
		public boolean isAddSecondLevelEntities() {
			return addSecondLevelEntities;
		}

		public void setAddSecondLevelEntities(boolean addSecondLevelEntities) {
			this.addSecondLevelEntities = addSecondLevelEntities;
		}

		public Integer getRootEntityId() {
			return rootEntityId;
		}

		public void setRootEntityId(Integer rootEntityId) {
			this.rootEntityId = rootEntityId;
		}

		public Integer getVersionId() {
			return versionId;
		}

		public void setVersionId(Integer versionId) {
			this.versionId = versionId;
		}
	}
}
