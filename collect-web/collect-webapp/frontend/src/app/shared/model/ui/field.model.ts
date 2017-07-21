import { UIModelObject } from './ui-model-object.model';
import { FormComponent } from './form-component.model';

export class Field extends UIModelObject implements FormComponent {
    
    attributeType: string;
    attributeDefinitionId: number;
    label: string;
    column: number;
    columnSpan: number;
    row: number;
    
    constructor(id: number, parent: UIModelObject) {
        super(id, parent);
    }
    
}    