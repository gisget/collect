import { Component, Input } from '@angular/core';

@Component({
    selector: 'ofc-children',
    templateUrl: './children.component.html'
})
export class ChildrenComponent {

    @Input() children: Object[];

    @Input() parentId: [number, number];

    constructor() { }

}
