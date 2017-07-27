import { Component } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';
import { FieldDefinition, CodeFieldDefinition } from 'app/shared/model/ui';
import { Attribute } from 'app/shared/model';
import { CodeListService } from 'app/shared/services';
import { CodeAttributeDefinition } from 'app/shared/model';

@Component({
    selector: 'ofc-code-field',
    templateUrl: './code-field.component.html',
    styleUrls: ['./code-field.component.scss']
})
export class CodeFieldComponent extends InputFieldComponent {

    options: Array<Object>;
    codeValue: string;
    layout: string;

    constructor(private codeListService: CodeListService) {
        super();
        this.options = [
            { value: 1, label: "Option 1" },
            { value: 2, label: "Option 2" },
        ];
    }

    ngOnInit() {
    }
    
    set fieldDefinition(fieldDef:FieldDefinition) {
        this._fieldDefinition = fieldDef;
        let codeFieldDef: CodeFieldDefinition = <CodeFieldDefinition>fieldDef;
        this.layout = codeFieldDef.layout;
    } 

    set attribute(attribute: Attribute) {
        this._attribute = attribute;
        this.options = [];
        if (attribute != null) {
            let attrDef: CodeAttributeDefinition = <CodeAttributeDefinition>attribute.definition;
            this.codeListService.loadAvailableItems(attrDef.survey.id, attrDef.codeListId, 
                attribute.record.id, attribute.record.stepNumber, 
                attribute.parent.id, attrDef.id)
                .subscribe(items => this.options = items.map(item => {
                    return {label: item.label, code: item.code};
                }));
        }
    }
    
    updateSelectedValue() {
        if (this.attribute == null) {
            this.codeValue = null;
        } else {
            this.codeValue = this.attribute.fields[0].value as string;
        }
    }
}
