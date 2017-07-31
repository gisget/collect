package org.openforis.collect.command.value;

public class TimeValue implements Value {

	private Integer hour;
	private Integer minute;
	
	public TimeValue(Integer hour, Integer minute) {
		super();
		this.hour = hour;
		this.minute = minute;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getMinute() {
		return minute;
	}

	public void setMinute(Integer minute) {
		this.minute = minute;
	}
	
	
}
