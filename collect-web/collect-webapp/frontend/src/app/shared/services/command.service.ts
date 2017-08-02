import { Injectable, Injector, EventEmitter }       from '@angular/core';
import { JsonPipe } from '@angular/common';
import { Headers, Http, Response, Jsonp, RequestOptions, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from 'app/shared/services';
import { Attribute, AttributeDefinition, EntityDefinition, Node, Record, Survey } from 'app/shared/model';
import { Event, RecordEventWrapper, RecordEvent } from 'app/shared/model/event';

@Injectable()
export class CommandService extends AbstractService {
    
    public eventReceived$: EventEmitter<Event>;
    
    constructor(injector: Injector) {
        super(injector);
        this.eventReceived$ = new EventEmitter();
    }
    
    addAttribute(record: Record, parentEntityId: number, attrDef: AttributeDefinition)
            : Observable<RecordEvent[]> {
        let url = this.contextPath + 'command/record/attribute';
        
        let username: string = "admin";

        let command: Object = {
            username: username,
            surveyId: record.survey.id,
            recordId: record.id,
            parentEntityId: parentEntityId,
            nodeDefId: attrDef.id
        };
        
        let headers: Headers = new Headers();
        headers.set('Content-Type', 'application/json; charset=utf-8');
        
        let config: RequestOptions = new RequestOptions({
            headers: headers
        });

        return this.http.post(url, command, config) 
                    .map(this.responseToEvents.bind(this))
                    .catch(this.handleError);
    }
    
    addEntity(record: Record, parentEntityId: number, entityDef: EntityDefinition)
            : Observable<RecordEvent[]> {
        let url = this.contextPath + 'command/record/entity';
        
        let username: string = "admin";

        let command: Object = {
            username: username,
            surveyId: record.survey.id,
            recordId: record.id,
            parentEntityId: parentEntityId,
            nodeDefId: entityDef.id
        };
        
        let headers: Headers = new Headers();
        headers.set('Content-Type', 'application/json; charset=utf-8');
        
        let config: RequestOptions = new RequestOptions({
            headers: headers
        });

        return this.http.post(url, command, config) 
                    .map(this.responseToEvents.bind(this))
                    .catch(this.handleError);
    }
    
    updateAttribute(attribute: Attribute, attributeType: string, valueByField: Object)
            : Observable<RecordEvent[]> {
        let url = this.contextPath + 'command/record/attribute';
        
        let username: string = "admin";
        
        let command: Object = {
            username: username,
            surveyId: attribute.record.survey.id,
            recordId: attribute.record.id,
            parentEntityId: attribute.parent.id,
            nodeDefId: attribute.definition.id,
            nodeId: attribute.id,
            attributeType: attributeType,
            valueByField: valueByField
        };
        
        let headers: Headers = new Headers();
        headers.set('Content-Type', 'application/json; charset=utf-8');
        
        let config: RequestOptions = new RequestOptions({
            headers: headers
        });

        return this.http.patch(url, command, config) 
                    .map(this.responseToEvents.bind(this))
                    .catch(this.handleError);
    }

    deleteNode(node: Node)
            : Observable<RecordEvent[]> {
        let url = this.contextPath + 'command/record/delete_node';
        
        let username: string = "admin";
        
        let command: Object = {
            username: username,
            surveyId: node.record.survey.id,
            recordId: node.record.id,
            nodeId: node.id
        };
        
        let headers: Headers = new Headers();
        headers.set('Content-Type', 'application/json; charset=utf-8');
        
        let config: RequestOptions = new RequestOptions({
            headers: headers
        });

        return this.http.post(url, command, config) 
                    .map(this.responseToEvents.bind(this))
                    .catch(this.handleError);
    }
    
    private responseToEvents(res) {
        let $this = this;
        let eventsJsonObjs: Array<Object> = res.json() as Object[];
        eventsJsonObjs.map(eventJsonObj => {
            let eventWrapper: RecordEventWrapper = new RecordEventWrapper();
            eventWrapper.fillFromJSON(eventJsonObj);
            let recordEvent: RecordEvent = eventWrapper.event;
            $this.eventReceived$.emit(recordEvent);
        });
    }
    
}