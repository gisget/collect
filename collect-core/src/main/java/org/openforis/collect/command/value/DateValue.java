package org.openforis.collect.command.value;

import java.util.Date;

public class DateValue implements Value {

	private Date value;

	public DateValue(Date value) {
		this.value = value;
	}
	
	public Date getValue() {
		return value;
	}

	public void setValue(Date value) {
		this.value = value;
	}
	
	
	
}
