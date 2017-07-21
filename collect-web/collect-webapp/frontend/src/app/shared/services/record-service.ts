import { Injectable, Injector }       from '@angular/core';
import { JsonPipe } from '@angular/common';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from 'app/shared/services';
import { Record, RecordSummary }   from 'app/shared/model';

@Injectable()
export class RecordService extends AbstractService {
        
    constructor(injector: Injector) {
        super(injector);
    } 
    
    getRecordsCount(surveyId : number, rootEntityDefId : number): Observable<number> {
        let url = this.contextPath + 'survey/' + surveyId + '/data/records/count.json';
        
        let params = new URLSearchParams();
        params.set('rootEntityDefinitionId', rootEntityDefId.toString());
        params.set('step', "1");
        
        return this.http.get(url, { search : params })
                    .map(res => res.text())
                    .catch(this.handleError);
    }
    
    getRecordSummaries(surveyId : number, rootEntityName : string, 
            offset : number, maxNumberOfRows : number,
            sortField : string, descendingOrder : boolean)
            : Observable<RecordSummary[]> {
        let url = this.contextPath + 'survey/' + surveyId + '/data/records/summary.json';

        
        let params = new URLSearchParams();
        params.append("offset", offset.toString());
        params.append("maxNumberOfRows", maxNumberOfRows.toString());
        params.append("rootEntityName", rootEntityName);
        params.append("sortFields[0].field", sortField);
        params.append("sortFields[0].descending", String(descendingOrder));
        
        return this.http.get(url, { search : params })
                    .map(res => res.json() as RecordSummary[])
                    .catch(this.handleError);
    }
    
    createNewRecord(surveyId: number, rootEntityId: number, versionId: number, userId: number) {
        let url = this.contextPath + 'survey/' + surveyId + '/data/records';
        let params = {
            rootEntityId: rootEntityId,
            versionId: versionId,
            userId: userId
        };
        return this.http.post(url, params)
                    .map(res => res.json() as Record)
                    .catch(this.handleError);
    }
    
    loadRecord(surveyId: number, recordId: number) {
        let url = this.contextPath + 'survey/' + surveyId + '/data/records/' + recordId + '/content.json';
        return this.http.get(url)
                    .map(res => res.json() as Record)
                    .catch(this.handleError);
    }
}