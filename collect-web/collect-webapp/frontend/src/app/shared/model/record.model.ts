import { Serializable } from './serializable.model';
import { Survey, NodeDefinition, EntityDefinition } from './survey.model';

export class Record extends Serializable {
    id: number;
    survey: Survey;
    stepNumber: number;
    rootEntity: Entity;
    rootEntityKeys: Array<string>;
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        let defId: number = parseInt(jsonObj.definitionId);
        let rootEntityDef: EntityDefinition = <EntityDefinition>this.survey.schema.getDefinitionById(defId);
            
        this.rootEntity = new Entity(this, rootEntityDef, null);
        this.rootEntity.fillFromJSON(jsonObj.rootEntity);
    }
}

export class Node extends Serializable {
    
    record: Record;
    parent: Entity;
    definition: NodeDefinition;
    id: number;
    
    constructor(record: Record, definition: NodeDefinition, parent: Entity) {
        super();
        this.record = record;
        this.definition = definition;
        this.parent = parent;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
    }
}

export class Entity extends Node {
    
    childrenByDefinitionId: Object;
    
    constructor(record: Record, definition: NodeDefinition, parent: Entity) {
        super(record, definition, parent);
    }
    
    get summaryLabel(): string {
        return "Entity " + this.id;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        let $this = this;
        this.childrenByDefinitionId = [];
        for (var defIdStr in jsonObj.childrenByDefinitionId) {
            let defId: number = parseInt(defIdStr);
            let def: NodeDefinition = this.record.survey.schema.getDefinitionById(defId);
            let childrenJsonObj = jsonObj.childrenByDefinitionId[defId];
            let children: Array<Node> = [];
            for (var i = 0; i < childrenJsonObj.length; i++) {
                let childJsonObj = childrenJsonObj[i];
                let node: Node;
                if (def instanceof EntityDefinition) {
                    node = new Entity(this.record, def, this);
                } else {
                    node = new Attribute(this.record, def, this);
                }
                node.fillFromJSON(childJsonObj);
                children.push(node);
            }
            $this.childrenByDefinitionId[defId] = children;
        }
    }
    
    getDescendants(ancestorDefIds: Array<number>): Array<Node> {
        let currentEntity: Entity = this;
        let descendants: Array<Node>;
        for (var ancestorDefId in ancestorDefIds) {
            descendants = currentEntity.childrenByDefinitionId[ancestorDefId];
        }
        return descendants;
    } 
    
    getSingleChild(defId: number): Node {
        let children: Array<Node> = this.childrenByDefinitionId[defId];
        return children == null || children.length == 0 ? null : children[0];
    }
}

export class Attribute extends Node {
    
    fields: Array<Field>;
    
    constructor(record: Record, definition: NodeDefinition, parent: Entity) {
        super(record, definition, parent);
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
