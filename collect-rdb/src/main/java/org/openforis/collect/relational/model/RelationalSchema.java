package org.openforis.collect.relational.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.openforis.collect.relational.CollectRdbException;
import org.openforis.idm.metamodel.CodeList;
import org.openforis.idm.metamodel.EntityDefinition;
import org.openforis.idm.metamodel.NodeDefinition;
import org.openforis.idm.metamodel.Survey;

/**
 * 
 * @author G. Miceli
 *
 */
public final class RelationalSchema {

	private Survey survey;
	private String name;
	private LinkedHashMap<String, Table<?>> tables;
	private Map<Integer, DataTable> dataTableByNodeId;
	private Map<CodeListTableKey, CodeTable> codeListTables;
	private Map<String, DataTable> rootDataTables;
	
	RelationalSchema(Survey survey, String name) throws CollectRdbException {
		this.survey = survey;
		this.name = name;
		this.tables = new LinkedHashMap<String, Table<?>>();
		this.codeListTables = new LinkedHashMap<CodeListTableKey, CodeTable>();
		this.rootDataTables = new HashMap<String, DataTable>();
		this.dataTableByNodeId = new HashMap<Integer, DataTable>();
	}

	public Survey getSurvey() {
		return survey;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Table<?>> getTables() {
		List<Table<?>> tableList = new ArrayList<Table<?>>(tables.values());
		return Collections.unmodifiableList(tableList);
	}
	
	public List<DataTable> getDataTables() {
		List<DataTable> result = new ArrayList<DataTable>();
		Queue<DataTable> queue = new LinkedList<DataTable>();
		queue.addAll(getRootDataTables());
		while(!queue.isEmpty()) {
			DataTable table = queue.poll();
			result.add(table);
			queue.addAll(table.getChildTables());
		}
		return result;
	}
	
	public Collection<DataTable> getRootDataTables() {
		return rootDataTables.values();
	}
	
	public Table<?> getTable(String name) {
		Table<?> table = tables.get(name);
		if ( table == null ) {
			throw new IllegalArgumentException("Table not found: " + name);
		} else {
			return table;
		}
	}
	
	public List<CodeTable> getCodeListTables() {
		List<CodeTable> tableList = new ArrayList<CodeTable>(codeListTables.values());
		return Collections.unmodifiableList(tableList);
	}
	
	public CodeTable getCodeListTable(CodeList list, Integer levelIdx) {
		CodeListTableKey key = new CodeListTableKey(list.getId(), levelIdx);
		return codeListTables.get(key);
	}

	public boolean containsTable(String name) {
		return tables.containsKey(name);
	}

	/**
	 * Adds a table to the schema.  If table with the same name 
	 * already exists it will be replaced.
	 * @param table
	 */
	void addTable(Table<?> table) {
		String name = table.getName();
		tables.put(name, table);
		if ( table instanceof DataTable ) {
			DataTable dataTable = (DataTable) table;
			NodeDefinition defn = dataTable.getNodeDefinition();
			dataTableByNodeId.put(defn.getId(), dataTable);
			if ( dataTable.getParent() == null ) {
				rootDataTables.put(defn.getName(), dataTable);
			}
		} else if ( table instanceof CodeTable ) {
			CodeTable codeListTable = (CodeTable) table;
			CodeList codeList = codeListTable.getCodeList();
			CodeListTableKey key = new CodeListTableKey(codeList.getId(), codeListTable.getLevelIdx());
			codeListTables.put(key, codeListTable);
		}
	}

	void assignAncestorTable(EntityDefinition entityDefn) {
		int nodeId = entityDefn.getId();
		while( ! entityDefn.isMultiple() ) {
				entityDefn = entityDefn.getParentEntityDefinition();
		}
		dataTableByNodeId.put( nodeId, dataTableByNodeId.get(entityDefn.getId()) );
	}

	public DataTable getDataTable(NodeDefinition nodeDefinition) {
		int id = nodeDefinition.getId();
		return dataTableByNodeId.get(id);
	}
	
	private static class CodeListTableKey {
		private int listId;
		private Integer levelIdx;
		
		CodeListTableKey(int listId, Integer levelIdx) {
			this.listId = listId;
			this.levelIdx = levelIdx;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((levelIdx == null) ? 0 : levelIdx.hashCode());
			result = prime * result + listId;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CodeListTableKey other = (CodeListTableKey) obj;
			if (levelIdx == null) {
				if (other.levelIdx != null)
					return false;
			} else if (!levelIdx.equals(other.levelIdx))
				return false;
			if (listId != other.listId)
				return false;
			return true;
		}
		
	}

}