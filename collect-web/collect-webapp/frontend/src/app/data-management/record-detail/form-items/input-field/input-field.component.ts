import { Component, OnInit, Input } from '@angular/core';
import { FieldDefinition } from 'app/shared/model/ui';
import { Attribute, Entity } from 'app/shared/model';

@Component({
    selector: 'ofc-input-field',
    templateUrl: './input-field.component.html',
    styleUrls: ['./input-field.component.scss']
})
//abstract
export class InputFieldComponent implements OnInit {

    _fieldDefinition: FieldDefinition;
    _attribute: Attribute;
    
    @Input() 
    set fieldDefinition(fieldDef: FieldDefinition) {
        this._fieldDefinition = fieldDef;
    }
    
    @Input() 
    set attribute(attribute: Attribute) {
        this._attribute = attribute;
        this.updateSelectedValue();
    }
    
    constructor() { }

    ngOnInit() {
    }
    
    updateSelectedValue() {
    }
    
    get fieldDefinition(): FieldDefinition {
        return this._fieldDefinition;
    }
    
    get attribute(): Attribute {
        return this._attribute;
    }
}
