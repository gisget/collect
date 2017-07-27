import { Component, OnInit, Input } from '@angular/core';

import { FormComponentDefinition } from 'app/shared/model/ui/form-component-definition.model';
import { Entity } from 'app/shared/model';

@Component({
    selector: 'ofc-form-items',
    templateUrl: './form-items.component.html'
})
export class FormItemsComponent implements OnInit {

    @Input() itemDefinitions: Array<FormComponentDefinition>;
    @Input() parentEntity: Entity;
    @Input() totalColumns: number;
    @Input() totalRows: number;

    constructor() { }
    
    ngOnInit() {
    }
}
