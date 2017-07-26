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

    _attribute: Attribute;
    
    @Input() fieldDefinition: FieldDefinition;
    @Input() 
    set attribute(attribute: Attribute) {
        this._attribute = attribute;
        this.updateSelectedValue();
    }
    
    get attribute(): Attribute {
        return this._attribute;
    }
    
    constructor() { }

    ngOnInit() {
    }
    
    updateSelectedValue() {
    }
    
}
