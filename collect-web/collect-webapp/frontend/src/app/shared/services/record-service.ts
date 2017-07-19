import { Injectable, Injector }       from '@angular/core';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from 'app/shared/services';
import { RecordSummary }   from 'app/shared/model';

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
    
    getRecordSummaries(surveyId : number, rootEntityDefId : number, 
            offset : number, maxNumberOfRecords : number,
            sortFieldId : number, sortOrder : number)
            : Observable<RecordSummary[]> {
        let url = this.contextPath + 'survey/' + surveyId + '/data/records/summary.json';

        let params = new URLSearchParams();
        params.set('rootEntityDefinitionId', rootEntityDefId.toString());
        params.set('params.offset', offset.toString());
        params.set('params.maxNumberOfRows', maxNumberOfRecords.toString());
//        params.set('params.sortFields', sortFields);
//        params.set('sortOrder', sortOrder.toString());
        
        return this.http.get(url, { search : params })
                    .map(res => res.json() as RecordSummary[])
                    .catch(this.handleError);
    }
    
}