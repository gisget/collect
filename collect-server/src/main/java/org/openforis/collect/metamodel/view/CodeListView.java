package org.openforis.collect.metamodel.view;

import java.util.ArrayList;
import java.util.List;

public class CodeListView extends SurveyObjectView {
	
	private String name;
	List<CodeListItemView> items = new ArrayList<CodeListItemView>();
	private boolean hierarchical;
	
	public List<CodeListItemView> getItems() {
		return items;
	}
	
	public void setItems(List<CodeListItemView> items) {
		this.items = items;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isHierarchical() {
		return hierarchical;
	}
	
	public void setHierarchical(boolean hierarchical) {
		this.hierarchical = hierarchical;
	}
}