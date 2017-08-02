package org.openforis.collect.event;


import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openforis.collect.model.AttributeAddChange;
import org.openforis.collect.model.AttributeChange;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.EntityAddChange;
import org.openforis.collect.model.EntityChange;
import org.openforis.collect.model.NodeChange;
import org.openforis.collect.model.NodeChangeSet;
import org.openforis.collect.model.NodeDeleteChange;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NumericAttributeDefinition;
import org.openforis.idm.metamodel.NumericAttributeDefinition.Type;
import org.openforis.idm.metamodel.RangeAttributeDefinition;
import org.openforis.idm.metamodel.Survey;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.BooleanAttribute;
import org.openforis.idm.model.BooleanValue;
import org.openforis.idm.model.Code;
import org.openforis.idm.model.CodeAttribute;
import org.openforis.idm.model.Coordinate;
import org.openforis.idm.model.CoordinateAttribute;
import org.openforis.idm.model.DateAttribute;
import org.openforis.idm.model.Entity;
import org.openforis.idm.model.Node;
import org.openforis.idm.model.NodeVisitor;
import org.openforis.idm.model.NumberAttribute;
import org.openforis.idm.model.NumericRangeAttribute;
import org.openforis.idm.model.TaxonAttribute;
import org.openforis.idm.model.TextAttribute;
import org.openforis.idm.model.Time;
import org.openforis.idm.model.TimeAttribute;

/**
 * 
 * @author D. Wiell
 * @author S. Ricci
 *
 */
public class EventProducer {

	public List<RecordEvent> produceFor(CollectRecord record, final String userName) {
		final List<RecordEvent> events = new ArrayList<RecordEvent>();
		
		final Integer recordId = record.getId();
		final RecordStep recordStep = record.getStep().toRecordStep();
		
		record.getRootEntity().traverse(new NodeVisitor() {
			public void visit(Node<? extends NodeDefinition> node, int idx) {
				NodeDefinition nodeDef = node.getDefinition();
				List<String> ancestorIds = getAncestorIds(nodeDef, node.getAncestorIds());
				EventFactory factory = new EventFactory(recordId, recordStep, ancestorIds, node, userName);
				if (node instanceof Entity) {
					events.addAll(factory.entityCreated());
				} else if (node instanceof Attribute) {
					if (nodeDef.isMultiple()) {
						events.addAll(factory.attributeCreated());
					} else {
						events.addAll(factory.attributeUpdated());
					}
				}
			}
		});
		return events;
	}
	
	public List<RecordEvent> produceFor(NodeChangeSet changeSet, String userName) {
		return toEvents(changeSet, userName);
	}

	private List<RecordEvent> toEvents(NodeChangeSet changeSet, String userName) {
		List<RecordEvent> events = new ArrayList<RecordEvent>();
		List<NodeChange<?>> changes = changeSet.getChanges();
		for (NodeChange<?> change : changes) {
			events.addAll(toEvent(change, userName));
		}
		return events;
	}

	private List<? extends RecordEvent> toEvent(NodeChange<?> change, String userName) {
		Node<?> node = change.getNode();
		List<String> ancestorIds = getAncestorIds(node.getDefinition(), change.getAncestorIds());
		Integer recordId = change.getRecordId();
		RecordStep recordStep = change.getRecordStep().toRecordStep();

		EventFactory factory = new EventFactory(recordId, recordStep, ancestorIds, node, userName);

		if (change instanceof EntityChange) {
			if (change instanceof EntityAddChange) {
				return factory.entityCreated();
			} else {
				List<RecordEvent> events = new ArrayList<RecordEvent>();
				EntityChange entityChange = (EntityChange) change;
				EntityDefinition entityDef = entityChange.getNode().getDefinition();
				Map<String, Boolean> childrenRelevance = entityChange.getChildrenRelevance();
				for (Entry<String, Boolean> entry : childrenRelevance.entrySet()) {
					String childName = entry.getKey();
					boolean relevant = entry.getValue();
					NodeDefinition childDef = entityDef.getChildDefinition(childName);
					events.add(factory.relevanceChanged(childDef.getId(), relevant));
				}
			}
		} else if (change instanceof AttributeChange) {
			if (change instanceof AttributeAddChange) {
				return factory.attributeCreated();
			} else {
				return factory.attributeUpdated();
			}
		} else if (change instanceof NodeDeleteChange) {
			if (node instanceof Entity) {
				return factory.entityDeleted();
			} else {
				return factory.attributeDeleted();
			}
		}
		return emptyList();
	}

	private List<String> getAncestorIds(NodeDefinition nodeDef, List<Integer> ancestorEntityIds) {
		if (nodeDef instanceof EntityDefinition && ((EntityDefinition) nodeDef).isRoot()) {
			return Collections.emptyList();
		}
		List<String> ancestorIds = new ArrayList<String>();
		if (nodeDef.isMultiple()) {
			int parentId = ancestorEntityIds.get(0);
			ancestorIds.add(getNodeCollectionId(parentId, nodeDef));
		}
		
		List<EntityDefinition> ancestorDefs = nodeDef.getAncestorEntityDefinitions();
		for (int ancestorIdx = 0; ancestorIdx < ancestorEntityIds.size(); ancestorIdx++) {
			int ancestorEntityId = ancestorEntityIds.get(ancestorIdx);
			EntityDefinition ancestorDef = ancestorDefs.get(ancestorIdx);
			ancestorIds.add(String.valueOf(ancestorEntityId));
			boolean inCollection = ! ancestorDef.isRoot() && ancestorDef.isMultiple();
			if (inCollection) {
				Integer ancestorParentId = ancestorEntityIds.get(ancestorIdx + 1);
				ancestorIds.add(getNodeCollectionId(ancestorParentId, ancestorDef)); 
			}
		}
		return ancestorIds;
	}

	private String getNodeCollectionId(int parentId, NodeDefinition memberDef) {
		return parentId + "|" + memberDef.getId();
	}
	
	private String getNodeCollectionDefinitionId(EntityDefinition parentDef, NodeDefinition memberDef) {
		return parentDef.getId() + "|" + memberDef.getId();
	}	

	private class EventFactory {
		Node<?> node;
		List<String> ancestorIds;
		Integer recordId;
		RecordStep recordStep;

		Survey survey;
		String surveyName;
		NodeDefinition nodeDef;
		int definitionId;
		int nodeId;
		Date timestamp;
		String userName;
		
		EventFactory(Integer recordId, RecordStep recordStep,  List<String> ancestorIds, Node<?> node, String userName) {
			this.recordId = recordId;
			this.recordStep = recordStep;
			this.ancestorIds = ancestorIds;
			this.node = node;
			this.userName = userName;
			
			this.survey = node.getSurvey();
			this.surveyName = survey.getName();
			this.nodeDef = node.getDefinition();
			this.definitionId = nodeDef.getId();
			this.nodeId = node.getInternalId();
			this.timestamp = new Date();
		}
		
		List<? extends RecordEvent> entityCreated() {
			List<RecordEvent> events = new ArrayList<RecordEvent>();
			Entity entity = (Entity) node;
			if (entity.isRoot()) {
				events.add(new RootEntityCreatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						String.valueOf(nodeId), timestamp, userName));
			} else {
				 events.add(new EntityCreatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), timestamp, userName));
			}
			//add node collection created events
			for (NodeDefinition childDef : ((EntityDefinition) nodeDef).getChildDefinitions()) {
				if (childDef.isMultiple()) {
					String collectionId = getNodeCollectionId(nodeId, childDef);
					String collectionDefId = getNodeCollectionDefinitionId(entity.getDefinition(), childDef);
					if (childDef instanceof AttributeDefinition) {
						events.add(new AttributeCollectionCreatedEvent(surveyName, recordId, recordStep, collectionDefId, 
								ancestorIds, collectionId, timestamp, userName));
					} else {
						events.add(new EntityCollectionCreatedEvent(surveyName, recordId, recordStep, collectionDefId, 
								ancestorIds, collectionId, timestamp, userName));
					}
				}
			}
			return events;
		}
		
		RelevanceChangedEvent relevanceChanged(int childDefinitionId, boolean relevant) {
			return new RelevanceChangedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
					ancestorIds, String.valueOf(nodeId), String.valueOf(childDefinitionId), relevant, timestamp, userName);
		}
		
		List<? extends RecordEvent> attributeCreated() {
			List<RecordEvent> result = new ArrayList<RecordEvent>();
			result.add(new AttributeCreatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), timestamp, userName));
			if (node.hasData()) {
				result.addAll(attributeUpdated());
			}
			return result;
		}
		
		List<? extends RecordEvent> attributeUpdated() {
			AttributeUpdatedEvent event = null;
			if (node instanceof BooleanAttribute) {
				BooleanValue value = ((BooleanAttribute) node).getValue();
				event = new BooleanAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), value.getValue(), timestamp, userName);
			} else if (node instanceof CodeAttribute) {
				Code value = ((CodeAttribute) node).getValue();
				event = new CodeAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), value.getCode(), 
						value.getQualifier(), timestamp, userName);
			} else if (node instanceof CoordinateAttribute) {
				Coordinate value = ((CoordinateAttribute) node).getValue();
				event = new CoordinateAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), value.getX(), value.getY(), value.getSrsId(), 
						timestamp, userName);
			} else if (node instanceof DateAttribute) {
				org.openforis.idm.model.Date value = ((DateAttribute) node).getValue();
				event = new DateAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), value.toJavaDate(), timestamp, userName);
			//TODO
//				} else if (node instanceof FileAttribute) {
			} else if (node instanceof NumberAttribute<?, ?>) {
				NumberAttribute<?, ?> attribute = (NumberAttribute<?, ?>) node;
				Number value = attribute.getNumber();
				Integer unitId = attribute.getUnitId();
				Type valueType = ((NumericAttributeDefinition) nodeDef).getType();
				switch(valueType) {
				case INTEGER:
					event = new IntegerAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
							ancestorIds, String.valueOf(nodeId), 
							(Integer) value, unitId, timestamp, userName);
					break;
				case REAL:
					event = new DoubleAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
							ancestorIds, String.valueOf(nodeId), 
							(Double) value, unitId, timestamp, userName);
					break;
				default:
					throw new IllegalArgumentException("Numeric type not supported: " + valueType);
				}
			} else if (node instanceof NumericRangeAttribute<?, ?>) {
				NumericRangeAttribute<?, ?> attribute = (NumericRangeAttribute<?, ?>) node;
				Number from = attribute.getFrom();
				Number to = attribute.getTo();
				Integer unitId = attribute.getUnitId();
				Type valueType = ((RangeAttributeDefinition) nodeDef).getType();
				switch(valueType) {
				case INTEGER:
					event = new IntegerRangeAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
							ancestorIds, String.valueOf(nodeId), 
							(Integer) from, (Integer) to, unitId, timestamp, userName);
					break;
				case REAL:
					event = new DoubleRangeAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
							ancestorIds, String.valueOf(nodeId), 
							(Double) from, (Double) to, unitId, timestamp, userName);
					break;
				default:
					throw new IllegalArgumentException("Numeric type not supported: " + valueType);
				}
			} else if (node instanceof TaxonAttribute) {
				TaxonAttribute taxonAttr = (TaxonAttribute) node;
				event = new TaxonAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), 
						taxonAttr.getCode(), taxonAttr.getScientificName(), taxonAttr.getVernacularName(), taxonAttr.getLanguageCode(), 
						taxonAttr.getLanguageVariety(), timestamp, userName);
			} else if (node instanceof TextAttribute) {
				String text = ((TextAttribute) node).getText();
				event = new TextAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), text, timestamp, userName);
			} else if (node instanceof TimeAttribute) {
				Time value = ((TimeAttribute) node).getValue();
				event = new DateAttributeUpdatedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
						ancestorIds, String.valueOf(nodeId), value.toJavaDate(), timestamp, userName);
//			} else {
//				TODO fail for not supported node types
//				throw new IllegalArgumentException("Unexpected node type: " + node.getClass().getSimpleName());
			}
			return event == null ? Collections.<RecordEvent>emptyList() : Arrays.<RecordEvent>asList(event);
		}
		
		List<? extends RecordEvent> entityDeleted() {
			return asList(new EntityDeletedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId), 
					ancestorIds, String.valueOf(nodeId), timestamp, userName));
		}
		
		List<? extends RecordEvent> attributeDeleted() {
			return asList(new AttributeDeletedEvent(surveyName, recordId, recordStep, String.valueOf(definitionId),
					ancestorIds, String.valueOf(nodeId), timestamp, userName));	
		}
	}

}
