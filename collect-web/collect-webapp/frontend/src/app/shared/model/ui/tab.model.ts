import { UIModelObject } from './ui-model-object.model';
import { TabContentContainer } from './tab-content-container.model';

export class Tab extends TabContentContainer {

    constructor(id: number, parent: UIModelObject) {
        super(id, parent);
    }
    
}
