package org.openforis.collect.metamodel.view;

import java.util.List;

import org.openforis.collect.designer.metamodel.AttributeType;

public class CodeAttributeDefView extends AttributeDefView {

	private int codeListId;
	private Integer parentCodeAttributeDefinitionId;

	public CodeAttributeDefView(int id, String name, String label, AttributeType type, List<String> fieldNames,
			boolean key, boolean multiple, int codeListId, Integer parentCodeAttrId) {
		super(id, name, label, type, fieldNames, key, multiple);
		this.codeListId = codeListId;
		this.parentCodeAttributeDefinitionId = parentCodeAttrId;
	}
	
	public int getCodeListId() {
		return codeListId;
	}
	
	public Integer getParentCodeAttributeDefinitionId() {
		return parentCodeAttributeDefinitionId;
	}
	

}
