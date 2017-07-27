import { Component, OnInit } from '@angular/core';

import { InputFieldComponent } from '../input-field/input-field.component';
import { FieldDefinition } from 'app/shared/model/ui';
import { NumericAttributeDefinition } from 'app/shared/model';

@Component({
    selector: 'ofc-number-field',
    templateUrl: './number-field.component.html',
    styleUrls: ['./number-field.component.scss']
})
export class NumberFieldComponent extends InputFieldComponent {

    numericType: string;

    constructor() {
        super();
    }

    ngOnInit() {
    }

    set fieldDefinition(fieldDef: FieldDefinition) {
        this._fieldDefinition = fieldDef;
        if (this.fieldDefinition == null) {
            this.numericType = null;
        } else {
            let attrDef: NumericAttributeDefinition = <NumericAttributeDefinition>this.fieldDefinition.attributeDefinition;
            this.numericType = attrDef.numericType;
        }
    }

}
