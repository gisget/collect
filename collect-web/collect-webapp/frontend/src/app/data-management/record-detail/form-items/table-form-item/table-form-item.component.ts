import { Component, OnInit, Input } from '@angular/core';

import { TableDefinition } from 'app/shared/model/ui';
import { Entity, EntityDefinition, Record } from 'app/shared/model';
import { CommandService } from 'app/shared/services';

@Component({
  selector: 'ofc-table-form-item',
  templateUrl: './table-form-item.component.html',
  styleUrls: ['./table-form-item.component.scss']
})
export class TableFormItemComponent implements OnInit {

    entities: Array<Entity>;
    
    _parentEntity: Entity;
    
    constructor(private commandService: CommandService) { }

    @Input() tableDefinition: TableDefinition;
    @Input() 
    set parentEntity(entity: Entity) {
        this._parentEntity = entity;
        this.entities = entity.childrenByDefinitionId[this.tableDefinition.entityDefinitionId];
    }
    
    ngOnInit() {
    }
    
    get parentEntity(): Entity {
        return this._parentEntity;
    }
    
}
