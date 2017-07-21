import { Component, Input } from '@angular/core';

import { Tab } from 'app/shared/model/ui/tab.model';

@Component({
    selector: 'ofc-tab',
    templateUrl: './tab.component.html'
})
export class TabComponent {

    @Input() tab: Tab;

    constructor() { }

}
