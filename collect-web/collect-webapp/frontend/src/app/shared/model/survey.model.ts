import { Serializable } from './serializable.model';

export class Survey extends Serializable {
    id: number;
    name: string;
    uri: string;
    schema: Schema;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.schema = new Schema();
        this.schema.fillFromJSON(jsonObj.schema);
    }
}

export class Schema extends Serializable {
    rootEntities: Array<EntityDefinition>;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.rootEntities = [];
        for (var i = 0; i < jsonObj.rootEntities.length; i++) {
            var rootEntityJsonObj = jsonObj.rootEntities[i];
            var rootEntity = new EntityDefinition(null);
            rootEntity.fillFromJSON(rootEntityJsonObj);
            this.rootEntities.push(rootEntity);
        }
    }
    
    public get defaultRootEntity(): EntityDefinition {
        return this.rootEntities[0];
    }
}

export class SurveyObject extends Serializable {
    id: number;
}    

export class NodeDefinition extends SurveyObject {
    parent: EntityDefinition;
    name: string;
    label: string;
    multiple: boolean;
    
    constructor(parent: EntityDefinition) {
        super();
        this.parent = parent;
    }
}

export class EntityDefinition extends NodeDefinition {
    children: Array<NodeDefinition>
    
    constructor(parent: EntityDefinition) {
        super(parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.children = [];
        for (var i = 0; i < jsonObj.children.length; i++) {
            var nodeJsonObj = jsonObj.children[i];
            var nodeDef = nodeJsonObj.type == 'ENTITY' ? new EntityDefinition(this): new AttributeDefinition(this);
            nodeDef.fillFromJSON(nodeJsonObj);
            this.children.push(nodeDef);
        }
    }
    
    public get keyAttributeDefinitions(): Array<AttributeDefinition> {
        let result: Array<AttributeDefinition> = [];
        let queue: Array<NodeDefinition> = [];
        
        for (var i = 0; i < this.children.length; i++) {
            queue.push(this.children[i]);
        }
        while (queue.length > 0) {
            let item: NodeDefinition = queue.shift();
            if (item instanceof AttributeDefinition) {
                let attrDef: AttributeDefinition = item;
                if (attrDef.key) {
                    result.push(item);
                }
            }
            if (item instanceof EntityDefinition && ! item.multiple) {
                for (var i = 0; i < item.children.length; i++) {
                    queue.push(item.children[i]);
                }
            }
        }
        return result;
    }
}

export class AttributeDefinition extends NodeDefinition {
    key: boolean;
    
    constructor(parent: EntityDefinition) {
        super(parent);
    }
}
