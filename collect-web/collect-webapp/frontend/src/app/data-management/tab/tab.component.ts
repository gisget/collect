import { Component, Input } from '@angular/core';

import { Tab } from './tab.model';

@Component({
    selector: 'ofc-tab',
    templateUrl: './tab.component.html'
})
export class TabComponent {

    @Input() tab: Tab;

    @Input() parentId: [number, number];

    @Input() values: any;

    constructor() { }

}
