import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
    selector: 'ofc-file-field',
    templateUrl: './file-field.component.html',
    styleUrls: ['./file-field.component.scss']
})
export class FileFieldComponent extends InputFieldComponent {

    constructor() {
        super();
    }

    ngOnInit() {
    }

}
