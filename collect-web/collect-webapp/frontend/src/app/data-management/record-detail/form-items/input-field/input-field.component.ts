import { Component, OnInit, Input } from '@angular/core';
import { FieldDefinition } from 'app/shared/model/ui';
import { Entity } from 'app/shared/model';

@Component({
    selector: 'app-input-field',
    templateUrl: './input-field.component.html',
    styleUrls: ['./input-field.component.scss']
})
export abstract class InputFieldComponent implements OnInit {

    @Input() fieldDefinition: FieldDefinition;
    //@Input() parentEntity: Entity;

    constructor() { }

    ngOnInit() {
    }

}
