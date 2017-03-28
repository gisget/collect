import { Component, OnInit } from '@angular/core';

import { MenuItem } from 'primeng/primeng';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

    private items: MenuItem[];

    constructor() {
    }

    ngOnInit() {
        this.items = [
            { label: 'HOME', icon: 'fa-home', routerLink: [''] },
            { label: 'DATA MANAGEMENT', icon: 'fa-table', routerLink: ['/data-management'] }
        ];
    }

}
