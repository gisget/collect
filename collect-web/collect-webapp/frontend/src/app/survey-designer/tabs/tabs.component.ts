import { Component, Input } from '@angular/core';

import { Tab } from '../tab/tab.model';

@Component({
    selector: 'ofc-tabs',
    templateUrl: './tabs.component.html'
})
export class TabsComponent {

    @Input() tabs: Tab[];

    @Input() parentId: [number, number];

    constructor() { }

}
