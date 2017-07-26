import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
  selector: 'ofc-text-field',
  templateUrl: './text-field.component.html',
  styleUrls: ['./text-field.component.scss']
})
export class TextFieldComponent extends InputFieldComponent {

    constructor() {
        super();
    }
    
    ngOnInit() {
    }

}
