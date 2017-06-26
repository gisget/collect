import { Component, Input, OnInit, AfterViewInit } from '@angular/core';
import { Validators, FormGroup, FormControl } from '@angular/forms';

import { SurveyService } from '../shared/survey.service';
import { FieldCode } from './field-code.model';

@Component({
    selector: 'ofc-field-code',
    templateUrl: './field-code.component.html'
})
export class FieldCodeComponent implements OnInit, AfterViewInit {

    @Input() field: FieldCode;

    @Input() parentId: [number, number];

    @Input() values: any;

    private fieldId: string;
    private formControl: FormControl;
    private formGroup: FormGroup;
    private options: Object[];

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
        this.options = this.surveyService.getCodeList(this.field.codeListId);
    }

    ngAfterViewInit() {
        if (this.values !== undefined) {
            window.setTimeout(() => {
                this.formControl.setValue(this.values[0]['fields'][0]['value']);
            });
        }
    }

}
