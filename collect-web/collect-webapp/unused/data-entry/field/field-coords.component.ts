import { Component, Input, OnInit } from '@angular/core';
import { Validators, FormGroup, FormControl } from '@angular/forms';

import { SurveyService } from '../shared/survey.service';
import { Field } from './field.model';

@Component({
    selector: 'ofc-field-coords',
    templateUrl: './field-coords.component.html'
})
export class CoordsComponent implements OnInit {

    @Input() field: Field;

    @Input() parentId: [number, number];

    private val1: number;
    private val2: number;
    private val3: string;

    private fieldId: string;
    private formGroup: FormGroup;
    private formControl: FormControl;

    constructor(private surveyService: SurveyService) { }

    ngOnInit() {
        let validators = [Validators.required];
        this.formControl = new FormControl('', validators);
        this.fieldId = this.field.attributeDefinitionId + '';
        let surveyForm = this.surveyService.getForm();
        if (this.parentId !== undefined) {
            this.formGroup = surveyForm['controls'][this.parentId[0]]['controls'][this.parentId[1]];
        } else {
            this.formGroup = surveyForm;
        }
        this.formGroup.addControl(this.fieldId, this.formControl);
    }

    private onChange() {
        this.formControl.setValue([this.val1, this.val2, this.val3]);
    }

}
