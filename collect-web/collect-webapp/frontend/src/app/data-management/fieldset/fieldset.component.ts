import { Component, Input } from '@angular/core';

import { Fieldset } from './fieldset.model';

@Component({
    selector: 'ofc-fieldset',
    templateUrl: './fieldset.component.html'
})
export class FieldsetComponent {

    @Input() fieldset: Fieldset;

    @Input() parentId: [number, number];

    @Input() values: any;

    constructor() { }

}
