import { Component, OnInit, Input } from '@angular/core';

import { FormComponentDefinition } from 'app/shared/model/ui/form-component-definition.model';
import { FieldsetDefinition } from 'app/shared/model/ui/fieldset-definition.model';

@Component({
    selector: 'ofc-form-item',
    templateUrl: './form-item.component.html'
})
export class FormItemComponent implements OnInit {

    @Input() itemDefinition: FormComponentDefinition;

    constructor() { }
    
    ngOnInit() {
    }
    
    isFieldset(): boolean {
        return this.itemDefinition instanceof FieldsetDefinition;
    }
}
