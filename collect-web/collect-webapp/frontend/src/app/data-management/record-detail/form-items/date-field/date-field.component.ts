import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
    selector: 'ofc-date-field',
    templateUrl: './date-field.component.html',
    styleUrls: ['./date-field.component.scss']
})
export class DateFieldComponent extends InputFieldComponent {

    constructor() {
        super();
    }

    ngOnInit() {
    }

}
