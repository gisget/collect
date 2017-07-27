package org.openforis.collect.metamodel.view;

import java.util.List;

import org.openforis.collect.designer.metamodel.AttributeType;
import org.openforis.idm.metamodel.NumericAttributeDefinition;
import org.openforis.idm.metamodel.NumericAttributeDefinition.Type;

public class NumericAttributeDefView extends AttributeDefView {

	private Type numericType;

	public NumericAttributeDefView(int id, String name, String label, AttributeType type, List<String> fieldNames,
			boolean key, boolean multiple, NumericAttributeDefinition.Type numericType) {
		super(id, name, label, type, fieldNames, key, multiple);
		this.numericType = numericType;
	}
	
	public Type getNumericType() {
		return numericType;
	}

}
