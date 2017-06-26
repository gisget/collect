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

    constructor() {
    }

    ngOnInit() {
        this.values = [
            {142:1, 264:2, 120:3, 223:4, 166:5, 219:6, 148:7, 1280:8, 1281:9, 56:10, 207:11}
        ];
    }

}
