import { Identifiable } from '../identifiable.model';

export interface FormComponentDefinition extends Identifiable {

    column: number;
    columnSpan: number;
    row: number;
    
}
