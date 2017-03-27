import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { FormGroup, FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Rx';

import { Survey } from './survey.model';
import { CodeList } from './code-list.model';
import { Record } from './record.model';

@Injectable()
export class SurveyService {

    private surveyUrl: string = 'assets/sample_survey_5.json';
    private recordUrl: string = 'assets/record1.json';

    private surveyForm: FormGroup;

    private codeLists: CodeList[];
    private parsedCodeLists: Object;

    private recordChildren: Object;

    constructor(private http: Http) { }

    getSurvey(): Promise<Survey> {
        return this.http.get(this.surveyUrl).map(res => res.json()).toPromise();
    }

    getRecord(): Promise<Record> {
        return this.http.get(this.recordUrl).map(res => res.json()).toPromise();
    }

    setRecord(recordChildren: Object): void {
        this.recordChildren = recordChildren;
    }

    getRecordChild(id: number): any {
        return this.recordChildren.hasOwnProperty(id) ? this.recordChildren[id] : null;
    }

    getForm(): FormGroup {
        return this.surveyForm;
    }

    setForm(surveyForm: FormGroup) {
        this.surveyForm = surveyForm;
    }

    setCodeLists(codeLists: CodeList[]) {
        this.codeLists = codeLists;
        this.parseCodeLists();
    }

    getCodeList(id: number): Object[] {
        return this.parsedCodeLists.hasOwnProperty(id) ? this.parsedCodeLists[id] : [];
    }

    private parseCodeLists():void {
        this.parsedCodeLists = {};
        for (let codeList of this.codeLists) {
            let values = [];
            for (let item of codeList.items) {
                values.push({
                    label: item['label'],
                    value: item['id']
                });
            }
            this.parsedCodeLists[codeList.id] = values;
        }
    }

}
