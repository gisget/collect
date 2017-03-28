import { Tab } from '../tab/tab.model';

export class Tabset {

    children: Object[];
    id: number;
    tabs: Tab[];
    totalColumns: number;
    totalRows: number;
    type: string;

    constructor() {
        this.tabs = [];
        this.type = "TABSET";
    }

}
