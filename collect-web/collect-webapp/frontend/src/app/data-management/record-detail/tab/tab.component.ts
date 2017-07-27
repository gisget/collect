import { Component, Input } from '@angular/core';

import { TabDefinition } from 'app/shared/model/ui/tab-definition.model';
import { Entity } from 'app/shared/model';

@Component({
    selector: 'ofc-tab',
    templateUrl: './tab.component.html'
})
export class TabComponent {

    _parentEntity: Entity;
    
    @Input() tab: TabDefinition;
    @Input() 
    set parentEntity(entity: Entity) {
        this._parentEntity = entity;
    }
    
    get parentEntity(): Entity {
        return this._parentEntity;
    }

    constructor() { }
    
    
}
