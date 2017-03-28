import { Component, Input, OnInit } from '@angular/core';
import { Validators, FormGroup, FormControl } from '@angular/forms';

import { SurveyService } from '../shared/survey.service';
import { FieldCode } from './field-code.model';

@Component({
    selector: 'ofc-field-code',
    templateUrl: './field-code.component.html'
})
export class FieldCodeComponent implements OnInit {

    @Input() field: FieldCode;

    @Input() parentId: [number, number];

    private fieldId: string;
    private formControl: FormControl;
    private formGroup: FormGroup;
    private values: Object[];
    private value: any;

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
        this.values = this.surveyService.getCodeList(this.field.codeListId);
    }

}
