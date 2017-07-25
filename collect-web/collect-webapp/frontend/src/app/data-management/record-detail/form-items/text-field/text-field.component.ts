import { Component, OnInit, Input } from '@angular/core';

import { FieldDefinition } from 'app/shared/model/ui';
import { Entity } from 'app/shared/model';

@Component({
  selector: 'ofc-text-field',
  templateUrl: './text-field.component.html',
  styleUrls: ['./text-field.component.scss']
})
export class TextFieldComponent implements OnInit {

    @Input() fieldDefinition: FieldDefinition;
    @Input() parentEntity: Entity;
    
    constructor() { }

    
    ngOnInit() {
    }

}
