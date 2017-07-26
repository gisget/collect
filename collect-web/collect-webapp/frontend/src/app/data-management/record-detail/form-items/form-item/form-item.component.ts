import { Component, OnInit, Input } from '@angular/core';

import { Survey } from 'app/shared/model/survey.model';
import { FormComponentDefinition } from 'app/shared/model/ui/form-component-definition.model';
import { FieldsetDefinition } from 'app/shared/model/ui/fieldset-definition.model';
import { FieldDefinition } from 'app/shared/model/ui/field-definition.model';
import { AttributeDefinition } from 'app/shared/model/survey.model';

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
    
    get attributeType():string {
        if (this.itemDefinition instanceof FieldDefinition) {
            let attrDef: AttributeDefinition = this.itemDefinition.attributeDefinition;
            return attrDef.attributeType;
        } else {
            return null;
        } 
    }
}
