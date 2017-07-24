import { UIModelObjectDefinition } from './ui-model-object-definition.model';
import { TabContainers } from './tab-containers.model';
import { FormComponentDefinition } from './form-component-definition.model';

export class TabDefinition extends UIModelObjectDefinition {

    items: Array<FormComponentDefinition>;
    tabs: TabDefinition[];
    totalColumns: number;
    totalRows: number;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
        this.items = [];
        this.tabs = [];
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.tabs = TabContainers.createTabsFromJSON(jsonObj.tabs, this);
        this.items = TabContainers.createItemsFromJSON(jsonObj.children, this);
    }
    
}
