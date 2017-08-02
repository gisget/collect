import { Event } from './event.model';
import { Serializable } from '../serializable.model';

export class RecordEvent extends Event {
	
	eventType: string;
	surveyName: string;
	recordId: number;
	recordStep: string;
	definitionId: string;
	ancestorIds: Array<string>;
	nodeId: string;
	timestamp: Date;
	userName: string;
	
	constructor() {
		super();
	}
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
    }
}

export class AttributeEvent extends RecordEvent {
}

export class AttributeUpdatedEvent extends AttributeEvent {
    
}

export class CodeAttributeUpdatedEvent extends AttributeUpdatedEvent {
    code: string;
    qualifier: string;
}

export class CoordinateAttributeUpdatedEvent extends AttributeUpdatedEvent {
    x: number;
    y: number;
    srsId: string;
}

export class DateAttributeUpdatedEvent extends AttributeUpdatedEvent {
    date: Date;
}

export class DoubleAttributeUpdatedEvent extends AttributeUpdatedEvent {
    value: number;
}

export class DoubleRangeAttributeUpdatedEvent extends AttributeUpdatedEvent {
    from: number;
    to: number;
}

export class IntegerAttributeUpdatedEvent extends AttributeUpdatedEvent {
    value: number;
}

export class IntegerRangeAttributeUpdatedEvent extends AttributeUpdatedEvent {
    from: number;
    to: number;
}

export class TaxonAttributeUpdatedEvent extends AttributeUpdatedEvent {
    code: string;
    scientificName: string;
    vernacularName: string;
    languageCode: string;
    languageVariety: string;
}

export class TextAttributeUpdatedEvent extends AttributeUpdatedEvent {
    text: string;
}

export class TimeAttributeUpdatedEvent extends AttributeUpdatedEvent {
    time: Date;
}

export class RelevanceChangedEvent extends RecordEvent {
    childDefId: String;
    relevant: boolean;
}

export class EntityCreatedEvent extends RecordEvent {
    
}

export class RecordEventWrapper extends Serializable {
    
    event: RecordEvent;
    eventType: string;
    
   
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        switch(jsonObj.eventType) {
        case "EntityCreatedEvent":
            this.event = new EntityCreatedEvent();
            break;
        case "RelevanceChangedEvent":
            this.event = new RelevanceChangedEvent();
            break;
        case "CodeAttributeUpdatedEvent":
            this.event = new CodeAttributeUpdatedEvent();
            break;
        case "CoordinateAttributeUpdatedEvent":
            this.event = new CoordinateAttributeUpdatedEvent();
            break;
        case "DateAttributeUpdatedEvent":
            this.event = new DateAttributeUpdatedEvent();
            break;
        case "DoubleAttributeUpdatedEvent":
            this.event = new DoubleAttributeUpdatedEvent();
            break;
        case "DoubleRangeAttributeUpdatedEvent":
            this.event = new DoubleRangeAttributeUpdatedEvent();
            break;
        case "IntegerAttributeUpdatedEvent":
            this.event = new IntegerAttributeUpdatedEvent();
            break;
        case "IntegerRangeAttributeUpdatedEvent":
            this.event = new IntegerRangeAttributeUpdatedEvent();
            break;
        case "TaxonAttributeUpdatedEvent":
            this.event = new TaxonAttributeUpdatedEvent();
            break;
        case "TextAttributeUpdatedEvent":
            this.event = new TextAttributeUpdatedEvent();
            break;
        case "TimeAttributeUpdatedEvent":
            this.event = new TimeAttributeUpdatedEvent();
            break;
        default: 
            console.log("Unsupported event type: " + jsonObj.eventType);
            return; //TODO throw error?
        }
        this.event.fillFromJSON(jsonObj.event);
    }
}