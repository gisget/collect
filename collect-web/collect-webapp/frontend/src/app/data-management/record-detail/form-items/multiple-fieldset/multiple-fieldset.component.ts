import { Component, OnInit, Input } from '@angular/core';
import { SelectItem } from 'primeng/primeng';

import { MultipleFieldsetDefinition } from 'app/shared/model/ui';
import { Entity, EntityDefinition, Record } from 'app/shared/model';
import { CommandService } from 'app/shared/services';

@Component({
    selector: 'ofc-multiple-fieldset',
    templateUrl: './multiple-fieldset.component.html',
    styleUrls: ['./multiple-fieldset.component.scss']
})
export class MultipleFieldsetComponent implements OnInit {

    entityOptions: SelectItem[];
    entities: Array<Entity>;
    selectedEntity: Entity;
    _parentEntity: Entity;
    
    @Input() fieldsetDefinition: MultipleFieldsetDefinition;
    @Input() 
    set parentEntity(entity: Entity) {
        this._parentEntity = entity;
        this.entityOptions = [];
        this.entityOptions.push({label:'Select Entity', value: null});
        if (entity) {
            this.entities = entity.childrenByDefinitionId[this.fieldsetDefinition.entityDefinitionId];
            for (var i = 0; i < this.entities.length; i++) {
                let entity: Entity = this.entities[i];
                this.entityOptions.push({label: entity.summaryLabel, value: entity.id});
            }
        }
    }
    
    constructor(private commandService: CommandService) { }

    ngOnInit() {
    }
    
    get parentEntity(): Entity {
        return this._parentEntity;
    }
    
    onEntityChange(event) {
        if (event.value == null) {
            this.selectedEntity = null;
        } else {
            let entityId: number = parseInt(event.value);
            this.selectedEntity = this.entities.find(entity => entity.id == entityId);
        }
    }
    
    onNewClick() {
        let record: Record = this.parentEntity.record;
        let parentEntityId: number = this.parentEntity.id;
        let entityDef: EntityDefinition = this.fieldsetDefinition.entityDefinition;
        this.commandService.addEntity(record, parentEntityId, entityDef);
    }
    
    onDeleteClick() {
        let record: Record = this.parentEntity.record;
        let parentEntityId: number = this.parentEntity.id;
        let entityDef: EntityDefinition = this.fieldsetDefinition.entityDefinition;
        this.commandService.deleteNode(this.selectedEntity);
    }
}
