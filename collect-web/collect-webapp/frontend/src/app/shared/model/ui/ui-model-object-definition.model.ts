import { Identifiable } from '../identifiable.model';
import { Serializable } from '../serializable.model';
import { UIConfiguration } from './ui-configuration.model';
import { TabSetDefinition } from './tabset-definition.model';
import { Survey } from '../survey.model';

export class UIModelObjectDefinition extends Serializable implements Identifiable {
    parent: UIModelObjectDefinition;
    id: number;
    hidden: boolean;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super();
        this.id = id;
        this.parent = parent;
    }
    
    get rootTabSet(): TabSetDefinition {
        let currentObj: UIModelObjectDefinition = this;
        while (currentObj.parent != null) {
            currentObj = currentObj.parent;
        }
        return <TabSetDefinition>currentObj;
    }
    
    get uiConfiguration():UIConfiguration {
        return this.rootTabSet.uiConfiguration;
    }
    
    get survey(): Survey {
        return this.rootTabSet.survey;
    }

}
