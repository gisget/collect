import { Injectable }       from '@angular/core';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { RecordSummary }    from '../model/record-summary';

@Injectable()
export class RecordService {
        
    constructor(private http: Http) {} 
    
    getRecordsCount(surveyId : number, rootEntityDefId : number): Observable<number> {
        let contextPath = 'http://127.0.0.1:8380/collect/';
        let url = contextPath + 'surveys/' + surveyId + '/records/count.json';
        
        let params = new URLSearchParams();
        params.set('rootEntityDefinitionId', rootEntityDefId.toString());
        params.set('step', "1");
        
        return this.http.get(url, { search : params })
                    .map(res => res.text())
                    .catch(this.handleError);
    }
    
    getRecordSummaries(surveyId : number, rootEntityDefId : number, 
            offset : number, maxNumberOfRecords : number,
            sortField : string, sortOrder : number)
            : Observable<RecordSummary[]> {
        let url = './surveys/' + surveyId + '/records/list.json';

        let params = new URLSearchParams();
        params.set('rootEntityDefinitionId', rootEntityDefId.toString());
        params.set('offset', offset.toString());
        params.set('maxNumberOfRecords', maxNumberOfRecords.toString());
        params.set('sortField', sortField);
        params.set('sortOrder', sortOrder.toString());
        
        return this.http.get(url, { search : params })
                    .map(this.extractData)
                    .catch(this.handleError);
    }
    
    private extractData(res: Response):RecordSummary[] {
        return res.json() as RecordSummary[];
    }
    
    private handleError(error: any) {
        // In a real world app, we might use a remote logging infrastructure
        // We'd also dig deeper into the error to get a better message
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
    
}