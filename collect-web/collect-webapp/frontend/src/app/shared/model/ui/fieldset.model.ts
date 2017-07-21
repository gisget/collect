import { UIModelObject } from './ui-model-object.model';
import { TabContentContainer } from './tab-content-container.model';
import { FormComponent } from './form-component.model';
import { EntityDefinition } from '../survey.model';

export class Fieldset extends TabContentContainer implements FormComponent {
    entityDefinitionId: number;
    column: number;
    columnSpan: number;
    row: number;
    entityDefinition: EntityDefinition;

    constructor(id: number, parent: UIModelObject) {
        super(id, parent);
    }
    
}