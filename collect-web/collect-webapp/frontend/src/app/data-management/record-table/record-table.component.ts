import { Component, ViewChild, OnInit, OnChanges, Input } from '@angular/core';

import { LazyLoadEvent }    from 'primeng/components/common/api';
import { DataTableModule }  from 'primeng/primeng';

import { RecordSummary, Survey } from 'app/shared/model';
import { RecordService }    from 'app/shared/services';

@Component({
    selector: 'app-record-table',
    templateUrl: './record-table.component.html',
    styleUrls: ['./record-table.component.scss']
})
export class RecordTableComponent implements OnInit, OnChanges {
    @Input() survey: Survey;
    
    records: RecordSummary[]; 
    displayDialog: boolean;    
    selectedRecords: RecordSummary[];    
    newRecord: boolean;
    totalRecords: number = 0;
    keyColumns: any[];
    
    constructor(private recordService: RecordService) { }  
    
    ngOnInit() {
        this.initTable();
    }

    ngOnChanges(changes: any) {
        this.initTable();
    }
    
    initTable() {
        console.log('init record table');
        this.keyColumns = [];
        let rootEntity = this.survey.schema.getDefaultRootEntity();
        console.log(rootEntity);
        rootEntity.children.forEach(function(entity, idx) {
            this.keyColumns.push({field: 'rootEntityKey' + (idx+1), header: entity.label, sortable: true});
        });
    } 
    
    loadRecordsLazy(event: LazyLoadEvent) {
        let surveyId = this.survey.id;
        let rootEntityDefId = this.survey.schema.getDefaultRootEntity().id;
        
        this.recordService.getRecordsCount(surveyId, rootEntityDefId)
            .subscribe(totalRecords => this.totalRecords = totalRecords);
        
        this.recordService.getRecordSummaries(surveyId, rootEntityDefId, 
                event.first, event.rows, 
                event.sortField ? event.sortField : "rootEnityKey1", event.sortOrder)
            .subscribe(records => this.records = records);
    }
}