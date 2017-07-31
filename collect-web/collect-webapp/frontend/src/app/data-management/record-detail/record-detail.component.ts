import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

import { TabViewModule } from 'primeng/primeng';

import { CommandService, RecordService, SurveyService } from 'app/shared/services';

import { Event, RecordEvent, Record } from 'app/shared/model';
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
                switch (event.eventType) {
                    case "CodeAttributeUpdatedEvent":
                        break;
                    case "DateAttributeUpdatedEvent":
                        break;
                }
            }
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
