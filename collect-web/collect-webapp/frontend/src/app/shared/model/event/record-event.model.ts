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

export class RecordEventWrapper extends Serializable {
    
    event: RecordEvent;
    eventType: string;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.event = new RecordEvent();
        this.event.fillFromJSON(jsonObj.event);
    }
}