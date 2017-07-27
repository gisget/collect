import { Component, Input } from '@angular/core';

import { TabSetDefinition } from 'app/shared/model/ui/tabset-definition.model';
import { TabDefinition } from 'app/shared/model/ui/tab-definition.model';
import { Record } from 'app/shared/model';

@Component({
    selector: 'ofc-tabset',
    templateUrl: './tabset.component.html'
})
export class TabSetComponent {

    @Input() tabSet: TabSetDefinition;
    @Input() record: Record;

    constructor() { }

}
