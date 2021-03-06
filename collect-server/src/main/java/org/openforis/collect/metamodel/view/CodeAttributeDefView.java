package org.openforis.collect.metamodel.view;

import java.util.List;

import org.openforis.collect.designer.metamodel.AttributeType;

public class CodeAttributeDefView extends AttributeDefView {

	private int codeListId;

	public CodeAttributeDefView(int id, String name, String label, AttributeType type, List<String> fieldNames,
			boolean key, boolean multiple, boolean showInRecordSummaryList, boolean qualifier, int codeListId) {
		super(id, name, label, type, fieldNames, key, multiple, showInRecordSummaryList, qualifier);
		this.codeListId = codeListId;
	}
	
	public int getCodeListId() {
		return codeListId;
	}
	

}
