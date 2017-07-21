import { Serializable } from '../serializable.model';
import { TabSet } from './tabset.model';
import { Survey } from '../survey.model';

export class UIConfiguration extends Serializable {
    
    survey: Survey;
    tabSets: Array<TabSet>;
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.tabSets = [];
        for (var i = 0; i < jsonObj.tabSets.length; i++) {
            var tabSetJsonObj = jsonObj.tabSets[i];
            var tabSet = new TabSet(tabSetJsonObj.id, this, null);
            tabSet.fillFromJSON(tabSetJsonObj);
            this.tabSets.push(tabSet);
        }
    }
    
    get mainTabSet(): TabSet {
        return this.tabSets[0];
    }
}