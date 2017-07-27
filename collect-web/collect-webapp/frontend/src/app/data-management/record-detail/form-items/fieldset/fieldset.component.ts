import { Component, OnInit, Input } from '@angular/core';

import { FieldsetDefinition } from 'app/shared/model/ui/fieldset-definition.model';
import { Entity } from 'app/shared/model';

@Component({
    selector: 'ofc-fieldset',
    templateUrl: './fieldset.component.html'
})
export class FieldsetComponent implements OnInit {

    @Input() fieldsetDefinition: FieldsetDefinition;
    @Input() parentEntity: Entity;

    constructor() { }
    
    ngOnInit() {
    }
}