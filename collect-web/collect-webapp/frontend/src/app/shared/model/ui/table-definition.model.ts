import { UIModelObjectDefinition } from './ui-model-object-definition.model';
import { TabContainers } from './tab-containers.model';
import { FormComponentDefinition } from './form-component-definition.model';
import { AttributeDefinition, EntityDefinition } from '../survey.model';

export class TableDefinition extends UIModelObjectDefinition implements FormComponentDefinition {

    headingComponents: Array<TableHeadingComponentDefinition> = [];
    headingRows: Array<Array<ColumnDefinition>> = [];
    headingColumns: Array<ColumnDefinition> = [];
    entityDefinitionId: number;
    totalHeadingColumns: number;
    totalHeadingRows: number;
    row: number;
    column: number;
    columnSpan: number;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.headingComponents = [];
        let jsonArrObj = jsonObj.headingComponents;
        for (var i = 0; i < jsonArrObj.length; i++) {
            var itemJsonObj = jsonArrObj[i];
            let item: TableHeadingComponentDefinition;
            switch(itemJsonObj.type) {
            case 'COLUMN_GROUP':
                item = new ColumnGroupDefinition(itemJsonObj.id, this);
                break;
            default:
                item = new ColumnDefinition(itemJsonObj.id, this);
            }
            item.fillFromJSON(itemJsonObj);
            this.headingComponents.push(item);
        }
        
        this.headingRows = this.extractHeadingRowsFromJson(jsonObj.headingRows);
        this.headingColumns = this.extractHeadingColumnsFromJson(jsonObj.headingColumns);
    }
    
    private extractHeadingRowsFromJson(jsonArr): Array<Array<ColumnDefinition>> {
        let rows = [];
        for (var i = 0; i < jsonArr.length; i++) {
            var jsonRow = jsonArr[i];
            let row: Array<ColumnDefinition> = [];
            for (var j = 0; j < jsonRow.length; j++) {
                let jsonCol = jsonRow[j];
                let col: ColumnDefinition = new ColumnDefinition(jsonCol.id, this);
                col.fillFromJSON(jsonCol);
                row.push(col);
            }
            rows.push(row);
        }
        return rows;
    }
    
    private extractHeadingColumnsFromJson(jsonArr): Array<ColumnDefinition> {
        let columns = [];
        for (var i = 0; i < jsonArr.length; i++) {
            var jsonCol = jsonArr[i];
            let col: ColumnDefinition = new ColumnDefinition(jsonCol.id, this);
            col.fillFromJSON(jsonCol);
            columns.push(col);
        }
        return columns;
    }
    
    get entityDefinition(): EntityDefinition {
        return <EntityDefinition>this.survey.schema.getDefinitionById(this.entityDefinitionId);
    }
}

export class TableHeadingComponentDefinition extends UIModelObjectDefinition {
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
}

export class ColumnGroupDefinition extends TableHeadingComponentDefinition {
    
    headingComponents: Array<TableHeadingComponentDefinition> = [];
    entityDefinitionId: number;
    label: string;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.headingComponents = [];
        let jsonArrObj = jsonObj.headingComponents;
        for (var i = 0; i < jsonArrObj.length; i++) {
            var itemJsonObj = jsonArrObj[i];
            let item: TableHeadingComponentDefinition;
            switch(itemJsonObj.type) {
            case 'COLUMN_GROUP':
                item = new ColumnGroupDefinition(itemJsonObj.id, this);
                break;
            default:
                item = new ColumnDefinition(itemJsonObj.id, this);
            }
            item.fillFromJSON(itemJsonObj);
            this.headingComponents.push(item);
        }
    }
    
    get entityDefinition(): EntityDefinition {
        return <EntityDefinition>this.survey.schema.getDefinitionById(this.entityDefinitionId);
    }
}

export class ColumnDefinition extends TableHeadingComponentDefinition {
    attributeDefinitionId: number;
    
    constructor(id: number, parent: UIModelObjectDefinition) {
        super(id, parent);
    }
    
    get attributeDefinition(): AttributeDefinition {
        return <AttributeDefinition>this.survey.schema.getDefinitionById(this.attributeDefinitionId);
    }
}

