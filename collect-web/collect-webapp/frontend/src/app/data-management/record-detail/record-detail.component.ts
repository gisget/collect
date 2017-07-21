import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute } from "@angular/router";

import { TabViewModule } from 'primeng/primeng';

import { Record } from 'app/shared/model';
import { TabSet } from 'app/shared/model/ui/tabset.model';

import { RecordService, SurveyService } from 'app/shared/services';

import { TabSetComponent } from './tabset/tabset.component';
import { TabComponent } from './tab/tab.component';

@Component({
  selector: 'ofc-record-detail',
  templateUrl: './record-detail.component.html',
  styleUrls: ['./record-detail.component.scss']
})
export class RecordDetailComponent implements OnInit {

    _record: Record;
    tabSet: TabSet = null;
    
    constructor(private recordService: RecordService, private surveyService: SurveyService, 
        private route: ActivatedRoute) { }

    ngOnInit() {
        const id = +this.route.snapshot.params["id"];
        let survey = this.surveyService.preferredSurvey;
        
        this.tabSet = survey.uiConfiguration.mainTabSet;
        
        let surveyId: number = survey.id;
        this.recordService.loadRecord(surveyId, id).subscribe(record => this.record = record);
    }
    
    get record(): Record {
        return this._record;
    }
    
    @Input()
    set record(record: Record) {
        this._record = record;
    }

}
