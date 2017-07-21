import { Identifiable } from '../identifiable.model';

export interface FormComponent extends Identifiable {

    column: number;
    columnSpan: number;
    row: number;
    
}
