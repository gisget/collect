import { Component, Input } from '@angular/core';

import { TabSet } from 'app/shared/model/ui/tabset.model';
import { Tab } from 'app/shared/model/ui/tab.model';

@Component({
    selector: 'ofc-tabset',
    templateUrl: './tabset.component.html'
})
export class TabSetComponent {

    @Input() tabSet: TabSet;

    constructor() { }

}
