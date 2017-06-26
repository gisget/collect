import { Component, Input, OnInit } from '@angular/core';

@Component({
    selector: 'ofc-children',
    templateUrl: './children.component.html'
})
export class ChildrenComponent implements OnInit {

    @Input() children: Object[];

    @Input() parentId: [number, number];

    @Input() values: any;

    constructor() { }

    ngOnInit() {
        if (this.values !== undefined) {
            this.values = this.values['childrenByDefinitionId'];
        }
    }

    private getChildValues(id: number): any {
        return (this.values !== undefined && this.values.hasOwnProperty(id)) ? this.values[id] : undefined;
    }

}
