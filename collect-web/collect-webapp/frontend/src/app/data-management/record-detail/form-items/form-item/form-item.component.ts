import { Component, OnInit, Input } from '@angular/core';

import { Survey, AttributeDefinition } from 'app/shared/model/survey.model';
import { Attribute, Entity } from 'app/shared/model/record.model';
import { FieldDefinition, FieldsetDefinition, FormComponentDefinition, MultipleFieldsetDefinition,
        TableDefinition } from 'app/shared/model/ui';

@Component({
    selector: 'ofc-form-item',
    templateUrl: './form-item.component.html'
})
export class FormItemComponent implements OnInit {

    _parentEntity: Entity;
    _attribute: Attribute;
    
    constructor() { }
    
    ngOnInit() {
    }
    
    @Input() itemDefinition: FormComponentDefinition;
    @Input() 
    set parentEntity(entity: Entity) {
        this._parentEntity = entity;
        this.attribute = this.determineAttribute();
    }
    
    get parentEntity():Entity {
        return this._parentEntity;
    }
    
    get attribute(): Attribute {
        return this._attribute;
    }

    set attribute(attribute: Attribute) {
        this._attribute = attribute;
    }
    
    get mainType(): string {
        if (this.itemDefinition instanceof FieldDefinition) {
            return "SINGLE_FIELD";
        } else if (this.itemDefinition instanceof FieldsetDefinition) {
            if (this.itemDefinition instanceof MultipleFieldsetDefinition) {
                return "MULTIPLE_FIELDSET";
            } else {
                return "SINGLE_FIELDSET";
            }
        } else if (this.itemDefinition instanceof TableDefinition) {
            return "TABLE";
        } else {
            return null;
        }
    }
    
    get attributeType():string {
        if (this.itemDefinition instanceof FieldDefinition) {
            let attrDef: AttributeDefinition = this.itemDefinition.attributeDefinition;
            return attrDef.attributeType;
        } else {
            return null;
        } 
    }
    
    determineAttribute(): Attribute {
        if (this.parentEntity != null 
                && this.itemDefinition instanceof FieldDefinition) {
            return <Attribute>this.parentEntity.getSingleChild(this.itemDefinition.attributeDefinition.id);
        }  else {
            return null;
        }
    }
}
