import { Component, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';

import { Observable } from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/toPromise';

import { ConfirmationService } from 'primeng/primeng';
import { SurveyService } from './shared/survey.service';

@Component({
    selector: 'data-management',
    templateUrl: './data-management.component.html'
})
export class DataManagementComponent implements OnInit {

    private tabSets: Object[];
    public values: any;
    private surveyForm: FormGroup;

    constructor(private fb: FormBuilder, private confirmationService: ConfirmationService, private surveyService: SurveyService) {
        this.surveyForm = this.fb.group({});
        this.surveyService.setForm(this.surveyForm);
    }

    confirm() {
        this.confirmationService.confirm({
            message: 'Are you sure that you want to perform this action?',
            accept: () => {
                this.onSubmit(this.surveyForm);
            }
        });
    }

    ngOnInit() {
        this.surveyService.getSurvey().then(survey => {
            console.log('survey: ', survey);
            this.surveyService.getRecord().then(record => {
                console.log('record: ', record);
                this.values = record['rootEntity'];
                this.surveyService.setCodeLists(survey.codeLists);
                this.tabSets = survey.uiConfiguration.tabSets;
            }, err => {
                console.log(err);
            });
        }, err => {
            console.log(err);
        });

    }

    onSubmit(form: FormGroup) {
        console.log(form.value, form.valid);
    }

}
