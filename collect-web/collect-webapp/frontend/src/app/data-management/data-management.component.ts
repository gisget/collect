import { Injectable, Component, OnInit } from '@angular/core';

import { RecordService, SurveyService, Record } from 'app/shared';

@Component({
    selector: 'app-data-management',
    templateUrl: './data-management.component.html',
    styleUrls: ['./data-management.component.scss']
})
export class DataManagementComponent implements OnInit {

    surveyService: SurveyService;
    
    constructor(private _surveyService: SurveyService, private recordService: RecordService) {
        this.surveyService = _surveyService;
    }
    ngOnInit() {}
    
    onNewClick() {
        let survey = this.surveyService.preferredSurvey;
        let surveyId = survey.id;
        let rootEntityId = survey.schema.defaultRootEntity.id;
        let versionId = null;
        let userId = 1;
        this.recordService.createNewRecord(surveyId, rootEntityId, versionId, userId)
            .subscribe(record => console.log(record));
    }
}
