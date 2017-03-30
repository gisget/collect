import { Component, Input } from '@angular/core';

import { Tabset } from './tabset.model';

@Component({
    selector: 'ofc-tabset',
    templateUrl: './tabset.component.html'
})
export class TabsetComponent {

    @Input() tabSet: Tabset;

    @Input() values: any;

    constructor() { }

}
