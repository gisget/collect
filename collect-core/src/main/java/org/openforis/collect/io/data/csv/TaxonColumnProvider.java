/**
 * 
 */
package org.openforis.collect.io.data.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.openforis.collect.metamodel.CollectAnnotations;
import org.openforis.collect.model.CollectSurvey;
import org.openforis.idm.metamodel.TaxonAttributeDefinition;

/**
 * @author M. Togna
 * @author S. Ricci
 * 
 */
public class TaxonColumnProvider extends CompositeAttributeColumnProvider<TaxonAttributeDefinition> {

	private static final String[] DEFAULT_FIELDS = new String[] {
			TaxonAttributeDefinition.CODE_FIELD_NAME,
			TaxonAttributeDefinition.SCIENTIFIC_NAME_FIELD_NAME,
			TaxonAttributeDefinition.VERNACULAR_NAME_FIELD_NAME,
			TaxonAttributeDefinition.LANGUAGE_CODE_FIELD_NAME,
			TaxonAttributeDefinition.LANGUAGE_VARIETY_FIELD_NAME
	};
	
	private static final String[] FAMILY_FIELDS = new String[] {
			TaxonAttributeDefinition.FAMILY_CODE_FIELD_NAME,
			TaxonAttributeDefinition.FAMILY_SCIENTIFIC_NAME_FIELD_NAME
	};

	public TaxonColumnProvider(CSVExportConfiguration config, TaxonAttributeDefinition defn) {
		super(config, defn);
	}

	@Override
	protected String[] getFieldNames() {
		CollectSurvey survey = attributeDefinition.getSurvey();
		CollectAnnotations annotations = survey.getAnnotations();
		List<String> visibleFields = new ArrayList<String>(Arrays.asList(
				survey.getUIOptions().getVisibleFields(attributeDefinition)));
		boolean showFamily = annotations.isShowFamily(attributeDefinition);
		if (! visibleFields.contains(TaxonAttributeDefinition.CODE_FIELD_NAME)) {
			//always include CODE field
			visibleFields.add(0, TaxonAttributeDefinition.CODE_FIELD_NAME);
		}
		if (! showFamily) {
			visibleFields.removeAll(Arrays.asList(FAMILY_FIELDS));
		}
		return visibleFields.toArray(new String[visibleFields.size()]);
	}
}
