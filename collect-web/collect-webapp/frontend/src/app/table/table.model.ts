export class Table {

    headingComponents: Object[];
    id: number;
    direction : string;
    columnSpan : number;
    showRowNumbers: boolean;
    countInSummaryList: boolean;
    entityDefinitionId: number;
    row: number;
    column: number;
    type: string;

    constructor() {
        this.direction = "BY_ROWS";
        this.type = "TABLE";
    }

}
