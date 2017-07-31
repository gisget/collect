package org.openforis.collect.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openforis.collect.command.AddAttributeCommand;
import org.openforis.collect.command.AddEntityCommand;
import org.openforis.collect.command.CommandDispatcher;
import org.openforis.collect.command.CreateRecordCommand;
import org.openforis.collect.command.DeleteNodeCommand;
import org.openforis.collect.command.DeleteRecordCommand;
import org.openforis.collect.command.UpdateAttributeCommand;
import org.openforis.collect.command.UpdateBooleanAttributeCommand;
import org.openforis.collect.command.UpdateCodeAttributeCommand;
import org.openforis.collect.command.UpdateDateAttributeCommand;
import org.openforis.collect.command.UpdateTextAttributeCommand;
import org.openforis.collect.command.UpdateTimeAttributeCommand;
import org.openforis.collect.command.value.BooleanValue;
import org.openforis.collect.command.value.CodeValue;
import org.openforis.collect.command.value.DateValue;
import org.openforis.collect.command.value.TextValue;
import org.openforis.collect.command.value.TimeValue;
import org.openforis.collect.command.value.Value;
import org.openforis.collect.designer.metamodel.AttributeType;
import org.openforis.collect.event.RecordEvent;
import org.openforis.collect.utils.Dates;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("command")
public class CommandController extends BasicController {

	@Autowired
	private CommandDispatcher commandDispatcher;
//	@Autowired
//	private MessageSource messageSource;
//	@Autowired
//	private SurveyContext surveyContext;

	@RequestMapping(value="record", method=POST, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> createRecord(@RequestBody CreateRecordCommand command) {
		List<RecordEvent> events = commandDispatcher.submit(command);
		return toView(events);
	}

	@RequestMapping(value="record", method=DELETE, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> deleteRecord(@RequestBody DeleteRecordCommand command) {
		RecordEvent events = commandDispatcher.submit(command);
		return toView(Arrays.asList(events));
	}

	@RequestMapping(value="record/attribute", method=POST, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> addAttribute(@RequestBody AddAttributeCommand command) {
		List<RecordEvent> events = commandDispatcher.submit(command);
		return toView(events);
	}
	
	@RequestMapping(value="record/attribute", method=PATCH, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> updateAttribute(@RequestBody UpdateAttributeCommandWrapper commandWrapper) {
		UpdateAttributeCommand<?> command = commandWrapper.toCommand();
		List<RecordEvent> events = commandDispatcher.submit(command);
		return toView(events);
	}
	
	@RequestMapping(value="record/entity", method=POST, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> addEntity(@RequestBody AddEntityCommand command) {
		List<RecordEvent> events = commandDispatcher.submit(command);
		return toView(events);
	}
	
	@RequestMapping(value="record/node", method=DELETE, consumes=APPLICATION_JSON_VALUE)
	@Transactional
	public @ResponseBody List<RecordEventView> deleteNode(@RequestBody DeleteNodeCommand command) {
		List<RecordEvent> events = commandDispatcher.submit(command);
		return toView(events);
	}
	
	private List<RecordEventView> toView(List<RecordEvent> events) {
		List<RecordEventView> result = new ArrayList<RecordEventView>(events.size());
		for (RecordEvent recordEvent : events) {
			result.add(new RecordEventView(recordEvent));
		}
		return result;
	}
		
	static class RecordEventView {
		
		private RecordEvent event;
		
		public RecordEventView(RecordEvent event) {
			super();
			this.event = event;
		}

		public String getEventType() {
			return event.getClass().getSimpleName();
		}
		
		public RecordEvent getEvent() {
			return event;
		}
		
	}
	
	static class UpdateAttributeCommandWrapper extends UpdateAttributeCommand<Value> {
		
		private static final long serialVersionUID = 1L;
		
		AttributeType attributeType;
		Map<String, String> valueByField;
		
		private Value toCommandValue() {
			switch(attributeType) {
			case BOOLEAN:
				return new BooleanValue(Boolean.valueOf(valueByField.get("value")));
			case CODE:
				return new CodeValue(valueByField.get("value"));
			case DATE:
				String dateStr = valueByField.get("day") + "/" + valueByField.get("month") + "/" + valueByField.get("year");
				Date date = Dates.parse(dateStr, "dd/mm/yyyy");
				return new DateValue(date);
			case TEXT:
				return new TextValue(valueByField.get("value"));
			case TIME:
				return new TimeValue(Integer.parseInt(valueByField.get("hour")), 
						Integer.parseInt(valueByField.get("minute")));
			default:
				throw new IllegalStateException("Unsupported attribute type: " + attributeType);
			}
		}
		
		public UpdateAttributeCommand<?> toCommand() {
			Class<? extends UpdateAttributeCommand<?>> commandType = toCommandType();
			try {
				@SuppressWarnings("unchecked")
				UpdateAttributeCommand<Value> c = (UpdateAttributeCommand<Value>) commandType.getConstructor().newInstance();
				BeanUtils.copyProperties(this, c, "attributeType", "value");
				c.setValue(toCommandValue());
				return c;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		private Class<? extends UpdateAttributeCommand<?>> toCommandType() {
			switch(attributeType) {
			case BOOLEAN:
				return UpdateBooleanAttributeCommand.class;
			case CODE:
				return UpdateCodeAttributeCommand.class;
			case DATE:
				return UpdateDateAttributeCommand.class;
			case TEXT:
				return UpdateTextAttributeCommand.class;
			case TIME:
				return UpdateTimeAttributeCommand.class;
			default:
				throw new IllegalStateException("Unsupported command type: " + attributeType);
			}
		}
		
		public AttributeType getAttributeType() {
			return attributeType;
		}

		public void setAttributeType(AttributeType attributeType) {
			this.attributeType = attributeType;
		}
		
		public Map<String, String> getValueByField() {
			return valueByField;
		}
		
		public void setValueByField(Map<String, String> valueByField) {
			this.valueByField = valueByField;
		}

	}
	
}
