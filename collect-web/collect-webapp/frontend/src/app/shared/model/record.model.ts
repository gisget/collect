import { Serializable } from './serializable.model';
import { Survey, NodeDefinition, EntityDefinition } from './survey.model';

export class Record extends Serializable {
    id: number;
    survey: Survey;
    rootEntity: Entity;
    rootEntityKeys: Array<string>;
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.rootEntity = new Entity(this, null);
        this.rootEntity.fillFromJSON(jsonObj.rootEntity);
    }
}

export class Node extends Serializable {
    
    record: Record;
    parent: Entity;
    id: number;
    
    constructor(record: Record, parent: Entity) {
        super();
        this.record = record;
        this.parent = parent;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
    }
}

export class Entity extends Node {
    
    childrenByDefinitionId: Object;
    
    constructor(record: Record, parent: Entity) {
        super(record, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        let $this = this;
        this.childrenByDefinitionId = [];
        for (var defIdStr in jsonObj.childrenByDefinitionId) {
            let defId: number = parseInt(defIdStr);
            let def: NodeDefinition = this.record.survey.schema.getDefinitionById(defId);
            let childrenJsonObj = jsonObj.childrenByDefinitionId[defId];
            for (var i = 0; i < childrenJsonObj.length; i++) {
                let childJsonObj = childrenJsonObj[i];
                let node: Node;
                if (def instanceof EntityDefinition) {
                    node = new Entity(this.record, this);
                } else {
                    node = new Attribute(this.record, this);
                }
                node.fillFromJSON(childJsonObj);
                $this.childrenByDefinitionId[defId] = node;
            }
            
        }
    }
}

export class Attribute extends Node {
    
    fields: Array<Field>;
    
    constructor(record: Record, parent: Entity) {
        super(record, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.fields = [];
        for (var i = 0; i < jsonObj.fields.length; i++) {
            let fieldJsonObj = jsonObj.fields[i];
            let field: Field = new Field();
            field.fillFromJSON(fieldJsonObj);
            this.fields.push(field);
        }
    }
}

export class Field extends Serializable {
    
    value: Object;
    remarks: string;
    
    constructor() {
        super();
    }
}
