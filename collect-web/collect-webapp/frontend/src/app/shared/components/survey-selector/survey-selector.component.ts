import { Component, OnInit } from '@angular/core';
import { SurveyService } from 'app/shared/services';

@Component({
    selector: 'app-survey-selector',
    templateUrl: './survey-selector.component.html',
    styleUrls: ['./survey-selector.component.scss']
})
export class SurveySelectorComponent implements OnInit {
    publishedSurveys = [];
    options = [];
    selectedSurvey = null;
    
     constructor(private surveyService: SurveyService) {
        this.surveyService.publishedSurveySummaries$
            .subscribe(summaries => {
                this.publishedSurveys = summaries;
                for(let survey of summaries) {
                    this.options.push({
                        value: survey.id, 
                        label: survey.name
                    });
                };
            });
    };
    
    ngOnInit() { }
    
    onSurveyChange(event) {
        let surveyId = Number(event.value);
        this.surveyService.loadSurvey(surveyId)
            .subscribe(survey => this.surveyService.preferredSurvey = survey);
    }

}