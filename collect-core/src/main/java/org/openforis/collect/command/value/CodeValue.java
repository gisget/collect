package org.openforis.collect.command.value;

public class CodeValue implements Value {

	private String value;
	private String qualifier;

	public CodeValue(String value) {
		this.value = value;
	}
	
	public CodeValue(String value, String qualifier) {
		this(value);
		this.qualifier = qualifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	
}
