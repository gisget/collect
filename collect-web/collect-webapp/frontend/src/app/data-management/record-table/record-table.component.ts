import { Component, ViewChild, OnInit, Input } from '@angular/core';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';

import { LazyLoadEvent }    from 'primeng/components/common/api';
import { DataTableModule, DataTable }  from 'primeng/primeng';

import { Record, RecordSummary, Survey } from 'app/shared/model';
import { RecordService }    from 'app/shared/services';

@Component({
    selector: 'ofc-record-table',
    templateUrl: './record-table.component.html',
    styleUrls: ['./record-table.component.scss']
})
export class RecordTableComponent implements OnInit {
    @ViewChild('dt') dt: DataTable;
    
    _survey: Survey;
    
    loading: boolean = false;
    records: RecordSummary[] = null; 
    displayDialog: boolean;    
    selectedRecords: RecordSummary[] = null;    
    newRecord: boolean;
    totalRecords: number = 0;
    keyColumns: any[];
    
    constructor(private router: Router,  private route: ActivatedRoute, private recordService: RecordService) { }  
    
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
        let keyColumns = [];
        if (this.survey) {
            let rootEntity = this.survey.schema.defaultRootEntity;
            rootEntity.keyAttributeDefinitions.forEach(function(keyAttrDef, idx) {
                keyColumns.push({field: 'KEY'+(idx+1), keyIndex: idx, header: keyAttrDef.label, sortable: true});
            });
        }
        this.keyColumns = keyColumns;
        this.dt.reset();
    } 
    
    loadRecordsLazy(event: LazyLoadEvent) {
        if (this.survey) {
            this.loading = true;
            let surveyId = this.survey.id;
            let rootEntityDef = this.survey.schema.defaultRootEntity;
            
            this.recordService.getRecordsCount(surveyId, rootEntityDef.id)
                .subscribe(totalRecords => this.totalRecords = totalRecords);
            
            let sortField;
            if (event.sortField) {
                switch(event.sortField) {
                    case "creationDate":
                        sortField = "DATE_CREATED";
                        break;
                    case "modifiedDate":
                        sortField = "DATE_MODIFIED";
                        break;
                    default:
                        sortField = "KEY1";
                }
            } else {
                sortField = "KEY1";
            }
            
            this.recordService.getRecordSummaries(surveyId, rootEntityDef.name, 
                    event.first, event.rows, 
                    sortField, event.sortOrder < 0)
                .subscribe(this.recordsLoaded.bind(this));
        } else {
            this.records = null;
        }
    }
    
    recordsLoaded(recordLoadResult:Object) {
        this.records = recordLoadResult["records"];
        this.loading = false;
    }
    
    editRecord(record:Record) {
        this.router.navigate(['records', record.id], { relativeTo: this.route });
    }
}