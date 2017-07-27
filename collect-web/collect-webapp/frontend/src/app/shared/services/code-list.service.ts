import { Injectable, Injector }       from '@angular/core';
import { JsonPipe } from '@angular/common';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from 'app/shared/services';
import { CodeListItem } from 'app/shared/model';

@Injectable()
export class CodeListService extends AbstractService {
    
    constructor(injector: Injector) {
        super(injector);
    } 
    
    loadAvailableItems(surveyId : number, codeListId: number,
            recordId : number, recordStepNumber: number, 
            parentEntityId: number, codeAttributeDefinitionId: number)
            : Observable<CodeListItem[]> {
        let url = this.contextPath + 'survey/' + surveyId + '/codelists/' + codeListId + '/items';
        
        let params = new URLSearchParams();
        params.append("recordId", recordId.toString());
        params.append("recordStepNumber", recordStepNumber.toString());
        params.append("parentEntityId", parentEntityId.toString());
        params.append("codeAttributeDefinitionId", codeAttributeDefinitionId.toString());
        
        return this.http.get(url, { search : params })
                    .map(res => res.json() as CodeListItem[])
                    .catch(this.handleError);
    }
}