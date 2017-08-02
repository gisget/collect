package org.openforis.collect.event;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author D. Wiell
 * @author S. Ricci
 *
 */
public class RelevanceChangedEvent extends RecordEvent {

	private final String childDefId;
	private boolean relevant;

	public RelevanceChangedEvent(String surveyName, Integer recordId,
			RecordStep step, String definitionId, List<String> ancestorIds,
			String nodeId, String childDefId, boolean relevant, Date timestamp, String userName) {
		super(surveyName, recordId, step, definitionId, ancestorIds, nodeId,
				timestamp, userName);
		this.childDefId = childDefId;
		this.relevant = relevant;
	}
	
	public String getChildDefId() {
		return childDefId;
	}
	
	public boolean isRelevant() {
		return relevant;
	}
	
}
