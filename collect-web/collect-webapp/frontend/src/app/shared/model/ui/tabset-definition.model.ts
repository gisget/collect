import { Survey } from '../survey.model';
import { FormComponentDefinition } from './form-component-definition.model';
import { TabContainers } from './tab-containers.model';
import { TabDefinition } from './tab-definition.model';
import { UIConfiguration } from './ui-configuration.model';
import { UIModelObjectDefinition } from './ui-model-object-definition.model';

export class TabSetDefinition extends UIModelObjectDefinition {

    _uiConfiguration: UIConfiguration;
    items: Array<FormComponentDefinition>;
    tabs: TabDefinition[];
    totalColumns: number;
    totalRows: number;

    constructor(id: number, uiConfiguration: UIConfiguration, parent: UIModelObjectDefinition) {
        super(id, parent);
        this._uiConfiguration = uiConfiguration;
        this.tabs = [];
        this.items = [];
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.tabs = TabContainers.createTabsFromJSON(jsonObj.tabs, this);
        this.items = TabContainers.createItemsFromJSON(jsonObj.children, this);
    }
    
    get uiConfiguration(): UIConfiguration {
        return this._uiConfiguration;
    }
    
    get survey(): Survey {
        return this.uiConfiguration.survey;
    }

}
