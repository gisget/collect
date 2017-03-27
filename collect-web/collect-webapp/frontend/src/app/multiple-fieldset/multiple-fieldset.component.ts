import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';

import { MultipleFieldset } from './multiple-fieldset.model';
import { Fieldset } from '../fieldset/fieldset.model';
import { SurveyService } from '../shared/survey.service';

@Component({
    selector: 'ofc-multiple-fieldset',
    templateUrl: './multiple-fieldset.component.html'
})
export class MultipleFieldsetComponent implements OnInit {

    @Input() multipleFieldset: MultipleFieldset;

    @Input() parentId: [number, number];

    private fieldsets: Fieldset[];

    constructor(private surveyService: SurveyService) { }

    ngOnInit() {
        this.fieldsets = [];
        this.addFieldset();
    }

    private addFieldset(): void {
        let id = this.fieldsets.length + 1;
        let fieldset = new Fieldset();
        fieldset.label = this.multipleFieldset.label + " #" + id;
        fieldset.children = this.multipleFieldset.children;
        fieldset.tabs = this.multipleFieldset.tabs;
        let surveyForm: FormGroup = this.surveyService.getForm();
        let formGroup2: FormGroup = new FormGroup({});
        if (id != 1) {
            let formGroup1: FormGroup = surveyForm.controls[this.multipleFieldset.entityDefinitionId] as FormGroup;
            formGroup1.addControl(id + '', formGroup2);
        } else {
            let formGroup1: FormGroup = new FormGroup({});
            formGroup1.addControl(id + '', formGroup2);
            surveyForm.addControl(this.multipleFieldset.entityDefinitionId + '', formGroup1);
        }
        this.parentId = [this.multipleFieldset.entityDefinitionId, id];
        this.fieldsets.push(fieldset);
    }

    private removeFieldset(): void {
        if (this.fieldsets.length != 1) {
            let surveyForm: FormGroup  = this.surveyService.getForm();
            let formGroup1: FormGroup = surveyForm['controls'][this.multipleFieldset.entityDefinitionId] as FormGroup;
            formGroup1.removeControl(this.fieldsets.length + '');
            this.fieldsets.pop();
        }
    }

}
