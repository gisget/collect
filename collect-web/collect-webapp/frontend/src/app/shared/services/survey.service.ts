import { Injectable, Injector } from '@angular/core';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

import { AbstractService } from './abstract-service';
import { SurveySummary, Survey } from 'app/shared/model';

@Injectable()
export class SurveyService extends AbstractService {
    
    publishedSurveySummaries$: Observable<SurveySummary[]> = null;
    preferredSurvey: Survey = null;
    
    constructor(injector: Injector) {
        super(injector);
    } 
    
    init() {
        this.publishedSurveySummaries$ = this.loadPublishedSurveySummaries();
    }
    
    setPreferredSurvey(survey: Survey) {
        this.preferredSurvey = survey;
    }

    private loadPublishedSurveySummaries():Observable<SurveySummary[]> {
        let url = this.contextPath + 'survey/summaries.json';
        return this.http.get(url)
                    .map(res => res.json() as SurveySummary[])
                    .catch(this.handleError);
    }
    
    public loadSurvey(id: Number):Observable<Survey> {
        let url = this.contextPath + 'survey/'+ id + '.json';
        return this.http.get(url)
                    .map(res => {
                        let survey = new Survey();
                        survey.fillFromJSON(res.json());
                        return survey;
                    })
                    .catch(this.handleError);
    }
}