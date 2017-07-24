import { Component, Input } from '@angular/core';

import { TabDefinition } from 'app/shared/model/ui/tab-definition.model';

@Component({
    selector: 'ofc-tab',
    templateUrl: './tab.component.html'
})
export class TabComponent {

    @Input() tab: TabDefinition;

    constructor() { }

}
