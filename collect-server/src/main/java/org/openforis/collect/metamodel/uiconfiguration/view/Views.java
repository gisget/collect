package org.openforis.collect.metamodel.uiconfiguration.view;

import java.util.ArrayList;
import java.util.List;

import org.openforis.commons.lang.Objects;

public abstract class Views {
	
	public static <O extends Object, V extends Object> List<V> fromObjects(List<O> objects, Class<V> viewType) {
		if (objects == null) {
			return null;
		}
		List<V> views = new ArrayList<V>(objects.size());
		for (O o : objects) {
			V view = Objects.newInstance(viewType, o);
			views.add(view);
		}
		return views;
	}
}