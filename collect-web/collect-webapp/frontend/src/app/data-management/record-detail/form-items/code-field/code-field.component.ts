import { Component } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
    selector: 'ofc-code-field',
    templateUrl: './code-field.component.html',
    styleUrls: ['./code-field.component.scss']
})
export class CodeFieldComponent extends InputFieldComponent {

    options: Array<Object>;

    constructor() {
        super();
        this.options = [
            { value: 1, label: "Option 1" },
            { value: 2, label: "Option 2" },
        ];
    }

    ngOnInit() {
    }

}
