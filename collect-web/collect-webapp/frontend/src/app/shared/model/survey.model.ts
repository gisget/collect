import { Serializable } from './serializable.model';
import { UIConfiguration } from './ui/ui-configuration.model';

export class SurveyObject extends Serializable {
    id: number;
    
    constructor(id: number) {
        super();
        this.id = id;
    }
}    

export class Survey extends Serializable {
    id: number;
    name: string;
    uri: string;
    schema: Schema;
    uiConfiguration: UIConfiguration;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.schema = new Schema();
        this.schema.fillFromJSON(jsonObj.schema);
        this.uiConfiguration = new UIConfiguration(this);
        this.uiConfiguration.fillFromJSON(jsonObj.uiConfiguration);
    }
}

export class Schema extends Serializable {
    rootEntities: Array<EntityDefinition>;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.rootEntities = [];
        for (var i = 0; i < jsonObj.rootEntities.length; i++) {
            var rootEntityJsonObj = jsonObj.rootEntities[i];
            var rootEntity = new EntityDefinition(rootEntityJsonObj.id, null);
            rootEntity.fillFromJSON(rootEntityJsonObj);
            this.rootEntities.push(rootEntity);
        }
    }
    
    public get defaultRootEntity(): EntityDefinition {
        return this.rootEntities[0];
    }
}

export class NodeDefinition extends SurveyObject {
    parent: EntityDefinition;
    name: string;
    label: string;
    multiple: boolean;
    
    constructor(id: number, parent: EntityDefinition) {
        super(id);
        this.parent = parent;
    }
}

export class EntityDefinition extends NodeDefinition {
    children: Array<NodeDefinition>
    
    constructor(id: number, parent: EntityDefinition) {
        super(id, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.children = [];
        for (var i = 0; i < jsonObj.children.length; i++) {
            var nodeJsonObj = jsonObj.children[i];
            let nodeDef;
            if (nodeJsonObj.type == 'ENTITY') { 
                nodeDef = new EntityDefinition(nodeJsonObj.id, this);
            } else {
                nodeDef = new AttributeDefinition(nodeJsonObj.id, this);
            }
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
    
    constructor(id: number, parent: EntityDefinition) {
        super(id, parent);
    }
}
