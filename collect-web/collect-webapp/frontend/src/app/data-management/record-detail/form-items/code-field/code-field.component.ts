import { Component, Input } from '@angular/core';
import {SelectItem} from 'primeng/primeng'

import { InputFieldComponent } from '../input-field/input-field.component';
import { FieldDefinition, CodeFieldDefinition } from 'app/shared/model/ui';
import { Attribute } from 'app/shared/model';
import { CodeListService, CommandService } from 'app/shared/services';
import { CodeAttributeDefinition } from 'app/shared/model';

@Component({
    selector: 'ofc-code-field',
    templateUrl: './code-field.component.html',
    styleUrls: ['./code-field.component.scss']
})
export class CodeFieldComponent extends InputFieldComponent {

    options: SelectItem[];
    _codeValue: string;
    layout: string;

    constructor(protected commandService: CommandService, private codeListService: CodeListService) {
        super(commandService);
        this.options = [];
    }

    ngOnInit() {
    }
    
    set fieldDefinition(fieldDef:FieldDefinition) {
        this._fieldDefinition = fieldDef;
        let codeFieldDef: CodeFieldDefinition = <CodeFieldDefinition>fieldDef;
        this.layout = codeFieldDef.layout;
    } 
    
    get fieldDefinition(): FieldDefinition {
        return this._fieldDefinition;
    }
    
    @Input()
    set attribute(attribute: Attribute) {
        this._attribute = attribute;
        this.options = [];
        if (attribute != null) {
            let attrDef: CodeAttributeDefinition = <CodeAttributeDefinition>attribute.definition;
            this.codeListService.loadAvailableItems(attrDef.survey.id, attrDef.codeListId, 
                attribute.record.id, attribute.record.stepNumber, 
                attribute.parent.id, attrDef.id)
                .subscribe(items => {
                    let codeFieldDef: CodeFieldDefinition = <CodeFieldDefinition>this.fieldDefinition;
                    this.options = items.map(item => {
                        let label: string = '';
                        if (codeFieldDef.showCode) {
                            label += item.code + ' - ';
                        }
                        label += item.label == null ? '---' : item.label;
                        return {label: label, value: item.code};
                    });
                });
        }
        this.updateSelectedValue();
    }
    
    get attribute(): Attribute {
        return this._attribute;
    }
    
    updateSelectedValue() {
        if (this.attribute == null) {
            this._codeValue = null;
        } else {
            this._codeValue = this.attribute.fields[0].value as string;
        }
    }
    
    get updateCommandValue(): Object {
        return this._codeValue == null ? null : {code: this._codeValue};
    }
    
    get codeValue(): string {
        return this._codeValue;
    }
    
    set codeValue(value: string) {
        this._codeValue = value;
        this.sendUpdateAttributeCommand();
    }
    
}
