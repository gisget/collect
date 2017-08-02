import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

import { TabViewModule } from 'primeng/primeng';

import { CommandService, RecordService, SurveyService } from 'app/shared/services';

import { Attribute, AttributeDefinition, AttributeUpdatedEvent,
    Event, CodeAttributeUpdatedEvent, CoordinateAttributeUpdatedEvent, DateAttributeUpdatedEvent, TextAttributeUpdatedEvent,
    Entity, EntityCreatedEvent, EntityDefinition, NodeDefinition,
    RecordEvent, Record, Survey } from 'app/shared/model';
import { TabSetDefinition } from 'app/shared/model/ui/tabset-definition.model';

import { TabSetComponent } from './tabset/tabset.component';
import { TabComponent } from './tab/tab.component';

@Component({
  selector: 'ofc-record-detail',
  templateUrl: './record-detail.component.html',
  styleUrls: ['./record-detail.component.scss']
})
export class RecordDetailComponent implements OnInit {

    _record: Record;
    tabSet: TabSetDefinition = null;
    
    constructor(private recordService: RecordService, private surveyService: SurveyService, 
        private commandService: CommandService, private route: ActivatedRoute) {
        commandService.eventReceived$.subscribe(event => this.onEventReceived(event));
    }
    
    ngOnInit() {
        const id = +this.route.snapshot.params["id"];
        let survey = this.surveyService.preferredSurvey;
        
        if (survey != null) {
            this.tabSet = survey.uiConfiguration.mainTabSet;
            
            this.recordService.loadRecord(survey, id).subscribe(record => this.record = record);
        }
    }
    
    onEventReceived(event: Event) {
        if (event instanceof RecordEvent) {
            if (this.record.id == event.recordId && this.record.step == event.recordStep) {
                console.log("event received");
                let survey: Survey = this.record.survey;
                let parentEntityId: number = parseInt(event.ancestorIds[event.ancestorIds.length - 1]);
                let parentEntity:Entity = <Entity>this.record.getNodeById(parentEntityId);
                let definition: NodeDefinition = survey.schema.getDefinitionById(parseInt(event.definitionId));
                let nodeId: number = parseInt(event.nodeId);
                
                if (event instanceof EntityCreatedEvent) {
                    let newEntity: Entity = new Entity(this.record, <EntityDefinition>definition, parentEntity);
                    newEntity.id = nodeId;
                    parentEntity.addChild(newEntity);
                } else if (event instanceof AttributeUpdatedEvent) {
                    let attr: Attribute = <Attribute>this.record.getNodeById(nodeId);
                    if (attr == null) {
                        //new attribute
                        attr = new Attribute(this.record, <AttributeDefinition>definition, parentEntity);
                        attr.id = nodeId;
                        parentEntity.addChild(attr);
                    } else {
                    }
                    this.setValueInAttribute(attr, <AttributeUpdatedEvent>event);
                }
            }
        }
    }
    
    private setValueInAttribute(attr: Attribute, event: AttributeUpdatedEvent) {
        if (event instanceof CodeAttributeUpdatedEvent) {
            let e: CodeAttributeUpdatedEvent = <CodeAttributeUpdatedEvent>event;
            attr.setFieldValue(0, e.code);
            attr.setFieldValue(1, e.qualifier);
        } else if (event instanceof CoordinateAttributeUpdatedEvent) {
            let e: CoordinateAttributeUpdatedEvent = <CoordinateAttributeUpdatedEvent>event;
            attr.setFieldValue(0, e.x);
            attr.setFieldValue(1, e.y);
            attr.setFieldValue(2, e.srsId);
        } else if (event instanceof DateAttributeUpdatedEvent) {
            let date: Date = (<DateAttributeUpdatedEvent>event).date;
            attr.setFieldValue(0, date.getFullYear());
            attr.setFieldValue(1, date.getMonth());
            attr.setFieldValue(2, date.getDay());
        } else if (event instanceof TextAttributeUpdatedEvent) {
            attr.setFieldValue(0, (<TextAttributeUpdatedEvent>event).text);
        }
    }
    
    @Input()
    set record(record: Record) {
        this._record = record;
    }

    get record(): Record {
        return this._record;
    }
    
}
