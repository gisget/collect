import { Injectable, Component, OnInit } from '@angular/core';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';

import { RecordService, SurveyService, Record } from 'app/shared';

@Component({
    selector: 'app-data-management',
    templateUrl: './data-management.component.html',
    styleUrls: ['./data-management.component.scss']
})
export class DataManagementComponent implements OnInit {

    constructor(private surveyService: SurveyService, private recordService: RecordService, 
            private router: Router,  private route: ActivatedRoute) {
    }
    ngOnInit() {}
    
    onNewClick() {
        let $this = this;
        let survey = this.surveyService.preferredSurvey;
        let surveyId = survey.id;
        let rootEntityId = survey.schema.defaultRootEntity.id;
        let versionId = null;
        let userId = 1;
        this.recordService.createNewRecord(surveyId, rootEntityId, versionId, userId)
            .subscribe(record => {
                $this.router.navigate(['records', record.id], { relativeTo: $this.route });
        });
    }
}
