export class Tab {

    children: Object[];
    id: number;
    label: string;
    tabs: Tab[];
    totalColumns: number;
    totalRows: number;
    type: string;

    constructor() {
        this.type = "TAB";
    }

}
