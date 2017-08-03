package org.openforis.collect.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.openforis.collect.manager.AbstractPersistedObjectManager;
import org.openforis.commons.web.AbstractFormUpdateValidationResponse;
import org.openforis.commons.web.PersistedObjectForm;
import org.openforis.commons.web.Response;
import org.openforis.idm.metamodel.PersistedObject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author S. Ricci
 *
 */
public abstract class AbstractPersistedObjectEditFormController<T extends PersistedObject, 
											F extends PersistedObjectForm<T>, 
											M extends AbstractPersistedObjectManager<T, ?>> {
	
	private static final String[] IGNORE_FIELDS = new String[] {"creationDate", "modifiedDate", "uuid"};
	
	protected M itemManager;
	
	protected abstract T createItemInstance();
	protected abstract F createFormInstance(T item);
	
	@RequestMapping(value="list.json", method = RequestMethod.GET)
	public @ResponseBody
	List<F> loadAll() {
		List<T> items = loadAllItems();
		return createFormInstances(items);
	}
	
	@RequestMapping(value = "/{id}.json", method = RequestMethod.GET)
	public @ResponseBody
	F load(@PathVariable int id) {
		T item = loadItem(id);
		F form = createFormInstance(item);
		return form;
	}
	
	@RequestMapping(value="save.json", method = RequestMethod.POST)
	public @ResponseBody
	Response save(@Validated F form, BindingResult result) {
		List<ObjectError> errors = result.getAllErrors();
		SimpleFormUpdateResponse response;
		if (errors.isEmpty()) {
			T item = loadOrCreateItem(form);
			copyFormIntoItem(form, item);
			itemManager.save(item);
			F responseForm = createFormInstance(item);
			response = new SimpleFormUpdateResponse(responseForm);
		} else {
			response = new SimpleFormUpdateResponse(errors);
		}
		return response;
	}
	
	@RequestMapping(value="duplicate.json", method = RequestMethod.POST)
	public @ResponseBody
	Response duplicate(@RequestParam int itemId, BindingResult result) {
		T item = itemManager.loadById(itemId);
		T newItem = item; //TODO clone?!
		newItem.setId(null);
		itemManager.save(newItem);
		F responseForm = createFormInstance(newItem);
		return new SimpleFormUpdateResponse(responseForm);
	}
	
	@RequestMapping(value="validate.json", method = RequestMethod.POST)
	public @ResponseBody
	Response validate(@Validated F form, BindingResult result) {
		List<ObjectError> errors = result.getAllErrors();
		Response response = new SimpleFormUpdateResponse(errors);
		return response;
	}
	
	@RequestMapping(value = "{id}.json", method = RequestMethod.DELETE)
	public @ResponseBody
	Response delete(@PathVariable int id) {
		try {
			T item = loadItem(id);
			itemManager.delete(item);
			return new Response();
		} catch (Exception e) {
			return createErrorResponse(e);
		}
	}
	
	protected T loadOrCreateItem(F form) {
		if (form.getId() == null) {
			return createItemInstance();
		} else {
			return loadItem(form.getId());
		}
	}

	protected T loadItem(int id) {
		return itemManager.loadById(id);
	}
	
	protected List<T> loadAllItems() {
		return itemManager.loadAll();
	}
	
	protected List<F> createFormInstances(List<T> items) {
		List<F> forms = new ArrayList<F>(items.size());
		for (T item : items) {
			forms.add(createFormInstance(item));
		}
		return forms;
	}

	protected void copyFormIntoItem(F form, T item) {
		form.copyTo(item, IGNORE_FIELDS);
	}
	
	protected Response createErrorResponse(Exception e) {
		Response response = new Response();
		response.setErrorStatus();
		response.setErrorMessage(e.getMessage());
		return response;
	}
	
	public void setItemManager(M itemManager) {
		this.itemManager = itemManager;
	}

	public class SimpleFormUpdateResponse extends AbstractFormUpdateValidationResponse<F> {

		public SimpleFormUpdateResponse(F form) {
			super(form);
		}

		public SimpleFormUpdateResponse(List<ObjectError> errors) {
			super(errors);
		}
		
	}

}
