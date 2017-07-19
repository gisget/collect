import { Injector, Injectable }       from '@angular/core';
import { Http, Response, Jsonp, URLSearchParams }   from '@angular/http'; 
import { Observable }       from 'rxjs/Rx';

export class AbstractService {
    
    protected contextPath: string;
    protected http: Http;
    
    constructor(injector: Injector) {
        this.contextPath = 'http://127.0.0.1:8480/collect/';
        this.http = injector.get(Http);
    } 
    
    ngOnInit() {
    }
    
    protected handleError(error: any) {
        // In a real world app, we might use a remote logging infrastructure
        // We'd also dig deeper into the error to get a better message
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';
        console.error(errMsg); // log to console instead
        return Observable.throw(errMsg);
    }
}