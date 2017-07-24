import { UIModelObjectDefinition } from './ui-model-object-definition.model';
import { FormComponentDefinition } from './form-component-definition.model';

export class FieldDefinition extends UIModelObjectDefinition implements FormComponentDefinition {
    
    attributeType: string;
    attributeDefinitionId: number;
    label: string;
    column: number;
    columnSpan: number;
    row: number;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
    
}    