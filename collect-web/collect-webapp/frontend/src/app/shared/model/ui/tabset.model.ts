import { UIConfiguration } from './ui-configuration.model';
import { UIModelObject } from './ui-model-object.model';
import { Tab } from './tab.model';
import { TabContentContainer } from './tab-content-container.model';
import { Survey } from '../survey.model';

export class TabSet extends TabContentContainer {

    _uiConfiguration: UIConfiguration;
    totalColumns: number;
    totalRows: number;

    constructor(id: number, uiConfiguration: UIConfiguration, parent: UIModelObject) {
        super(id, parent);
        this._uiConfiguration = uiConfiguration;
    }
    
    fillFromJSON(jsonObj:Object) {
        super.fillFromJSON(jsonObj);
    }
    
    get uiConfiguration(): UIConfiguration {
        return this._uiConfiguration;
    }
    
    get survey(): Survey {
        return this.uiConfiguration.survey;
    }

}
