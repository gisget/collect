package org.openforis.collect.persistence.utils;

import java.util.ArrayList;
import java.util.List;

public class TableMetaData {
	
	private List<ColumnMetaData> columnsMetaData = new ArrayList<ColumnMetaData>();
	
	public void addColumnMetaData(ColumnMetaData columnMetaData) {
		columnsMetaData.add(columnMetaData);
	}
	
	public List<ColumnMetaData> getColumnsMetaData() {
		return columnsMetaData;
	}
	
	public void setColumnsMetaData(List<ColumnMetaData> columnsMetaData) {
		this.columnsMetaData = columnsMetaData;
	}
	
	public static class ColumnMetaData {
		
		private String name;
		private Integer dataType;
		
		public ColumnMetaData(String name, Integer dataType) {
			super();
			this.name = name;
			this.dataType = dataType;
		}
		
		public String getName() {
			return name;
		}
		
		public Integer getDataType() {
			return dataType;
		}
	}
}

