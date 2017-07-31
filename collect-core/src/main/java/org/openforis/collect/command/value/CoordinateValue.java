package org.openforis.collect.command.value;

public class CoordinateValue {

	private Double x;
	private Double y;
	private String srsId;

	public CoordinateValue(Double x, Double y, String srsId) {
		super();
		this.x = x;
		this.y = y;
		this.srsId = srsId;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getSrsId() {
		return srsId;
	}

	public void setSrsId(String srsId) {
		this.srsId = srsId;
	}
	
}
