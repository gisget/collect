import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
    selector: 'ofc-time-field',
    templateUrl: './time-field.component.html',
    styleUrls: ['./time-field.component.scss']
})
export class TimeFieldComponent extends InputFieldComponent {

    constructor() { 
        super(); 
    }

    ngOnInit() {
    }

}
