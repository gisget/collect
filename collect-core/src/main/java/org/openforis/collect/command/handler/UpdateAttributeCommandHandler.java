package org.openforis.collect.command.handler;

import java.util.List;

import org.openforis.collect.command.UpdateAttributeCommand;
import org.openforis.collect.command.UpdateBooleanAttributeCommand;
import org.openforis.collect.command.UpdateCodeAttributeCommand;
import org.openforis.collect.command.UpdateDateAttributeCommand;
import org.openforis.collect.command.UpdateTextAttributeCommand;
import org.openforis.collect.command.UpdateTimeAttributeCommand;
import org.openforis.collect.command.value.DateValue;
import org.openforis.collect.command.value.TimeValue;
import org.openforis.collect.event.EventProducer;
import org.openforis.collect.event.RecordEvent;
import org.openforis.collect.manager.RecordManager;
import org.openforis.collect.manager.RecordProvider;
import org.openforis.collect.manager.SurveyManager;
import org.openforis.collect.model.CollectRecord;
import org.openforis.collect.model.NodeChangeSet;
import org.openforis.idm.model.Attribute;
import org.openforis.idm.model.BooleanValue;
import org.openforis.idm.model.Code;
import org.openforis.idm.model.Date;
import org.openforis.idm.model.TextValue;
import org.openforis.idm.model.Time;
import org.openforis.idm.model.Value;

public class UpdateAttributeCommandHandler<C extends UpdateAttributeCommand<?>> extends NodeCommandHandler<C> {

	public UpdateAttributeCommandHandler(SurveyManager surveyManager, RecordProvider recordProvider, RecordManager recordManager) {
		super(surveyManager, recordProvider, recordManager);
	}

	@Override
	public List<RecordEvent> execute(C command) {
		CollectRecord record = findRecord(command);
		Attribute<?, Value> attribute = findAttribute(command, record);
		Value value = extractValue(command);
		NodeChangeSet changeSet = recordUpdater.updateAttribute(attribute, value);
		
		recordManager.save(record);
		
		List<RecordEvent> events = new EventProducer().produceFor(changeSet, command.getUsername());
		return events;
	}
	
	private Value extractValue(UpdateAttributeCommand<?> command) {
		if (command instanceof UpdateBooleanAttributeCommand) {
			return new BooleanValue(((UpdateBooleanAttributeCommand) command).getValue().getValue());
		} else if (command instanceof UpdateCodeAttributeCommand) {
			return new Code(((UpdateCodeAttributeCommand) command).getValue().getValue());
		} else if (command instanceof UpdateDateAttributeCommand) {
			DateValue commandValue = ((UpdateDateAttributeCommand) command).getValue();
			return Date.parse(commandValue.getValue());
		} else if (command instanceof UpdateTextAttributeCommand) {
			return new TextValue(((UpdateTextAttributeCommand) command).getValue().getValue());
		} else if (command instanceof UpdateTimeAttributeCommand) {
			TimeValue commandValue = ((UpdateTimeAttributeCommand) command).getValue();
			return new Time(commandValue.getHour(), commandValue.getMinute());
		} else {
			throw new IllegalArgumentException("Unsupported update attribute command type: " + command);
		}
	}
}
