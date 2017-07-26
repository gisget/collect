import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
    selector: 'ofc-boolean-field',
    templateUrl: './boolean-field.component.html',
    styleUrls: ['./boolean-field.component.scss']
})
export class BooleanFieldComponent extends InputFieldComponent {

    value: boolean;
    
    constructor() {
        super();
    }

    ngOnInit() {
    }

    updateSelectedValue() {
        if (this.attribute == null) {
            this.value = false;
        } else {
            this.value = this.attribute.fields[0].value as boolean;
        }

    }
}
