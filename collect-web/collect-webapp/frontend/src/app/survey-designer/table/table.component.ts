import {Component, Input, OnInit} from '@angular/core';

import { Table } from './table.model';

@Component({
    selector: 'ofc-table',
    templateUrl: './table.component.html'
})
export class TableComponent implements OnInit {

    @Input() table: Table;

    @Input() parentId: [number, number];

    private values: Object[];

    constructor() { }

    ngOnInit() {

        let header: Object[];
        let rowSpan: number[];
        let colSpan: number;
        let deph = 0;
/*
        this.table.headingComponents.forEach((item, i) => {
            this.calculateSpan(item, i, rowSpan, colSpan, deph);
        });
        console.log(deph);*/

    }
/*
    private calculateSpan(item: Object, i: number, rowSpan: number[], colSpan: number, deph: number): void {
        if (item['type'] == 'COLUMN') {
        } else if (item['type'] == 'COLUMN_GROUP') {
            item['headingComponents'].forEach((item_r, i_r) => {
                deph++;
                console.log(deph);
                this.calculateSpan(item_r, i_r, rowSpan, colSpan, deph);
            });
        }
    }*/

}
