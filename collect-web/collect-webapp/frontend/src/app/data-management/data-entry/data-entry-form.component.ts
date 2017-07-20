import { Component, OnInit, Input } from '@angular/core';

import { Record } from 'app/shared/model';

@Component({
  selector: 'app-data-entry-form',
  templateUrl: './data-entry-form.component.html',
  styleUrls: ['./data-entry-form.component.scss']
})
export class DataEntryFormComponent implements OnInit {

    _record: Record;
    
    constructor() { }

    ngOnInit() {
    }
    
    get record(): Record {
        return this._record;
    }
    
    @Input()
    set record(record: Record) {
        this._record = record;
    }

}
