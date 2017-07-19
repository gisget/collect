import { Component, ViewChild, OnInit, Input } from '@angular/core';

import { LazyLoadEvent }    from 'primeng/components/common/api';
import { DataTableModule, DataTable }  from 'primeng/primeng';

import { RecordSummary, Survey, AttributeDefinition } from 'app/shared/model';
import { RecordService }    from 'app/shared/services';

@Component({
    selector: 'app-record-table',
    templateUrl: './record-table.component.html',
    styleUrls: ['./record-table.component.scss']
})
export class RecordTableComponent implements OnInit {
    _survey: Survey;
    
     @ViewChild('dt') dt: DataTable;
    
    loading: boolean = false;
    records: RecordSummary[] = null; 
    displayDialog: boolean;    
    selectedRecords: RecordSummary[] = null;    
    newRecord: boolean;
    totalRecords: number = 0;
    keyColumns: any[];
    
    constructor(private recordService: RecordService) { }  
    
    ngOnInit() {
        this.initTable();
    }
    
    get survey(): Survey {
        return this._survey;
    }
    
    @Input()
    set survey(survey: Survey) {
        this._survey = survey;
        this.initTable();
    }

    initTable() {
        console.log('init record table');
        let keyColumns = [];
        if (this.survey) {
            let rootEntity = this.survey.schema.defaultRootEntity;
            rootEntity.keyAttributeDefinitions.forEach(function(keyAttrDef, idx) {
                keyColumns.push({field: keyAttrDef.id, header: keyAttrDef.label, sortable: true});
            });
        }
        this.keyColumns = keyColumns;
        this.dt.reset();
    } 
    
    loadRecordsLazy(event: LazyLoadEvent) {
        if (this.survey) {
            let $this = this;
            this.loading = true;
            let surveyId = this.survey.id;
            let rootEntityDefId = this.survey.schema.defaultRootEntity.id;
            
            this.recordService.getRecordsCount(surveyId, rootEntityDefId)
                .subscribe(totalRecords => this.totalRecords = totalRecords);
            
            let firstKeyAttrDef: AttributeDefinition = this.survey.schema.defaultRootEntity.keyAttributeDefinitions[0];
            
            this.recordService.getRecordSummaries(surveyId, rootEntityDefId, 
                    event.first, event.rows, 
                    event.sortField ? parseInt(event.sortField) : firstKeyAttrDef.id, 
                    event.sortOrder)
                .subscribe(recordLoadResult => {
                    console.log(recordLoadResult["records"]);
                    $this.records = recordLoadResult["records"];
                    $this.loading = false;
                });
        } else {
            this.records = null;
        }
    }
    
    recordsLoaded(recordLoadResult:Object) {
        
    }
}