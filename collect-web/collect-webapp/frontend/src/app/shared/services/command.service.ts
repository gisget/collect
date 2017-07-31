import { Injectable, Injector, EventEmitter }       from '@angular/core';
import { JsonPipe } from '@angular/common';
import { Headers, Http, Response, Jsonp, RequestOptions, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from 'app/shared/services';
import { Attribute } from 'app/shared/model';
import { Event, RecordEventWrapper, RecordEvent } from 'app/shared/model/event';

@Injectable()
export class CommandService extends AbstractService {
    
    public eventReceived$: EventEmitter<Event>;
    
    constructor(injector: Injector) {
        super(injector);
        this.eventReceived$ = new EventEmitter();
    }
    
    sendUpdateAttributeCommand(username: string, attribute: Attribute, attributeType: string, valueByField: Object)
            : Observable<RecordEvent[]> {
        let url = this.contextPath + 'command/record/attribute';
        
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

        let $this = this;
        
        return this.http.patch(url, command, config) 
                    .map(res => {
                        let eventsJsonObjs: Array<Object> = res.json() as Object[];
                        eventsJsonObjs.map(eventJsonObj => {
                            let eventWrapper: RecordEventWrapper = new RecordEventWrapper();
                            eventWrapper.fillFromJSON(eventJsonObj);
                            $this.eventReceived$.emit(eventWrapper.event);
                        });
                    })
                    .catch(this.handleError);
    }
    
}