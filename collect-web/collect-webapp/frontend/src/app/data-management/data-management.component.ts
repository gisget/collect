import { Injectable, Component, OnInit } from '@angular/core';

import { SurveyService } from 'app/shared';

@Component({
    selector: 'app-data-management',
    templateUrl: './data-management.component.html',
    styleUrls: ['./data-management.component.scss']
})
export class DataManagementComponent implements OnInit {

    surveyService: SurveyService;
    
    constructor(private _surveyService: SurveyService) {
        this.surveyService = _surveyService;
    }
    ngOnInit() {}
}
