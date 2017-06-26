import { Component, Input, OnInit } from '@angular/core';
import { Validators, FormControl, FormGroup, FormBuilder } from '@angular/forms';

import { SurveyService } from '../shared/survey.service';
import { Field } from './field.model';

@Component({
    selector: 'ofc-field-number',
    templateUrl: './field-number.component.html'
})
export class FieldNumberComponent implements OnInit {

    @Input() field: Field;

    @Input() parentId: [number, number];

    private fieldId: string;
    private formControl: FormControl;
    private formGroup: FormGroup;

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

}
