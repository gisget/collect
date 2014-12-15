package org.openforis.collect.datacleansing;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.AttributeDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.NodeDefinitionVisitor;
import org.openforis.idm.metamodel.validation.Check;

/**
 * 
 * @author S. Ricci
 *
 */
public class DataErrorQueryGenerator {
	
	private static final String MISSING_DATA_QUERY_TITLE_FORMAT = "Missing data for '%s' attribute";
	private static final String MISSING_DATA_CONDITION_FORMAT = "idm:blank(%s)";

	public List<DataErrorQuery> generateMissingDataQueries(final CollectSurvey survey, final DataErrorType type) {
		List<DataErrorQuery> result = new ArrayList<DataErrorQuery>();
		survey.getSchema().traverse(new NodeDefinitionVisitor() {
			public void visit(NodeDefinition def) {
				if (def instanceof AttributeDefinition) {
					if (StringUtils.isNotBlank(def.getMinCountExpression())) {
						DataErrorQuery query = new DataErrorQuery(survey);
						query.setTitle(String.format(MISSING_DATA_QUERY_TITLE_FORMAT, def.getName()));
						query.setType(type);
						query.setAttributeDefinition((AttributeDefinition) def);
						query.setEntityDefinition(def.getParentEntityDefinition());
						query.setConditions(String.format(MISSING_DATA_CONDITION_FORMAT, def.getName()));
					}
				}
			}
		});
		return result;
	}
	
	public List<DataErrorQuery> generateValidationCheckQueries(CollectSurvey survey) {
		final List<DataErrorQuery> result = new ArrayList<DataErrorQuery>();
		survey.getSchema().traverse(new NodeDefinitionVisitor() {
			public void visit(NodeDefinition def) {
				if (def instanceof AttributeDefinition) {
					for (Check<?> check : ((AttributeDefinition) def).getChecks()) {
						DataErrorQuery query = createQuery(def, check);
						result.add(query);
					}
				}
			}
		});
		return result;
	}

	private DataErrorQuery createQuery(NodeDefinition def, Check<?> check) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(check.getCondition())) {
			sb.append(check.getCondition());
			sb.append(" and ");
		}
		sb.append(check.getExpression());
		DataErrorQuery query = new DataErrorQuery((CollectSurvey) def.getSurvey());
		//TODO
		query.setTitle("");
//				query.setType(type);
		query.setAttributeDefinition((AttributeDefinition) def);
		query.setEntityDefinition(def.getParentEntityDefinition());
		query.setConditions(sb.toString());
		return query;
	}
	
}
