import { EntityDefinition } from '../survey.model';
import { FormComponentDefinition } from './form-component-definition.model';
import { TabDefinition } from './tab-definition.model';
import { TabContainers } from './tab-containers.model';
import { UIModelObjectDefinition } from './ui-model-object-definition.model';

export class FieldsetDefinition extends UIModelObjectDefinition implements FormComponentDefinition {
    
    items: Array<FormComponentDefinition>;
    tabs: TabDefinition[];
    entityDefinitionId: number;
    label: string;
    column: number;
    columnSpan: number;
    row: number;
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
    
    get entityDefinition(): EntityDefinition {
        return <EntityDefinition>this.parent.survey.schema.getDefinitionById(this.entityDefinitionId);
    }
}