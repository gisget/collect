package org.openforis.collect.command.value;

public class BooleanValue implements Value {

	private Boolean value;

	public BooleanValue(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}
	
	
	
}
