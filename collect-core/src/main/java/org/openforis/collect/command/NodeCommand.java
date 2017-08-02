package org.openforis.collect.command;

import java.util.List;

import org.openforis.collect.event.RecordEvent;

public abstract class NodeCommand implements Command<List<RecordEvent>> {

	private static final long serialVersionUID = 1L;

	private String username;
	private int surveyId;
	private int recordId;
	private int parentEntityId;
	private int nodeDefId;
	private Integer nodeId;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(int surveyId) {
		this.surveyId = surveyId;
	}

	public int getRecordId() {
		return recordId;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public int getParentEntityId() {
		return parentEntityId;
	}

	public void setParentEntityId(int parentEntityId) {
		this.parentEntityId = parentEntityId;
	}

	public int getNodeDefId() {
		return nodeDefId;
	}

	public void setNodeDefId(int nodeDefId) {
		this.nodeDefId = nodeDefId;
	}

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

}