import { UIModelObjectDefinition } from './ui-model-object-definition.model';
import { FieldDefinition } from './field-definition.model';

export class CodeFieldDefinition extends FieldDefinition {
    
    layout: string;
    itemsOrientation: string;
    showCode: boolean;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
}