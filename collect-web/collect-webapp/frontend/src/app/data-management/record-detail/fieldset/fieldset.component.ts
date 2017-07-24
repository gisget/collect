import { Component, OnInit, Input } from '@angular/core';

import { FieldsetDefinition } from 'app/shared/model/ui/fieldset-definition.model';

@Component({
    selector: 'ofc-fieldset',
    templateUrl: './fieldset.component.html'
})
export class FieldsetComponent implements OnInit {

    @Input() fieldsetDefinition: FieldsetDefinition;

    constructor() { }
    
    ngOnInit() {
    }
}