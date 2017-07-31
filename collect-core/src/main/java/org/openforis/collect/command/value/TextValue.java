package org.openforis.collect.command.value;

public class TextValue implements Value {

	private String value;

	public TextValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
