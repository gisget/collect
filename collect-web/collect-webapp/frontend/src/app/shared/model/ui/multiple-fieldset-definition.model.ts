import { FieldsetDefinition } from './fieldset-definition.model';
import { UIModelObjectDefinition } from './ui-model-object-definition.model';

export class MultipleFieldsetDefinition extends FieldsetDefinition {
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
}