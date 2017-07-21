import { Identifiable } from '../identifiable.model';
import { Serializable } from '../serializable.model';
import { UIConfiguration } from './ui-configuration.model';
import { TabSet } from './tabset.model';
import { Survey } from '../survey.model';

export class UIModelObject extends Serializable implements Identifiable {
    parent: UIModelObject;
    id: number;
    hidden: boolean;
    
    constructor(id: number, parent: UIModelObject) {
        super();
        this.id = id;
        this.parent = parent;
    }
    
    get rootTabSet(): TabSet {
        var currentParent: UIModelObject = this.parent;
        while (currentParent != null) {
            currentParent = currentParent.parent;
        }
        return <TabSet>currentParent;
    }
    
    get uiConfiguration():UIConfiguration {
        return this.rootTabSet.uiConfiguration;
    }
    
    get survey(): Survey {
        return this.rootTabSet.survey;
    }

}
