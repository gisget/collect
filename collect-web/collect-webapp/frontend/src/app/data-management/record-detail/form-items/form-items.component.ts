import { Component, OnInit, Input } from '@angular/core';

import { FormComponentDefinition } from 'app/shared/model/ui/form-component-definition.model';

@Component({
    selector: 'ofc-form-items',
    templateUrl: './form-items.component.html'
})
export class FormItemsComponent implements OnInit {

    @Input() itemDefinitions: Array<FormComponentDefinition>;
    @Input() totalColumns: number;
    @Input() totalRows: number;

    constructor() { }
    
    ngOnInit() {
    }
}
