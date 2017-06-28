import { Component, OnInit } from '@angular/core';

import { SurveyService } from 'app/shared';

@Component({
    selector: 'app-data-management',
    templateUrl: './data-management.component.html',
    styleUrls: ['./data-management.component.scss']
})
export class DataManagementComponent implements OnInit {

    constructor(private surveyService: SurveyService) {
    }
    ngOnInit() {}
}
