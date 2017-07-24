import { Serializable } from '../serializable.model';
import { TabSetDefinition } from './tabset-definition.model';
import { Survey } from '../survey.model';

export class UIConfiguration extends Serializable {
    
    survey: Survey;
    tabSets: Array<TabSetDefinition>;
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.tabSets = [];
        for (var i = 0; i < jsonObj.tabSets.length; i++) {
            var tabSetJsonObj = jsonObj.tabSets[i];
            var tabSet = new TabSetDefinition(tabSetJsonObj.id, this, null);
            tabSet.fillFromJSON(tabSetJsonObj);
            this.tabSets.push(tabSet);
        }
    }
    
    get mainTabSet(): TabSetDefinition {
        return this.tabSets[0];
    }
}