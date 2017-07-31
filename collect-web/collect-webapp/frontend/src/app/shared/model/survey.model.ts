import { Serializable } from './serializable.model';
import { Visitor } from './visitor.model';
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
    codeLists: Array<CodeList>;
    uiConfiguration: UIConfiguration;
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.codeLists = [];
        for (var i = 0; i < jsonObj.codeLists.length; i++) {
            var codeListJsonObj = jsonObj.codeLists[i];
            var codeList = new CodeList(this);
            codeList.fillFromJSON(codeListJsonObj);
            this.codeLists.push(codeList);
        }
        this.schema = new Schema(this);
        this.schema.fillFromJSON(jsonObj.schema);
        this.uiConfiguration = new UIConfiguration(this);
        this.uiConfiguration.fillFromJSON(jsonObj.uiConfiguration);
    }
}

export class CodeList extends Serializable {
    survey: Survey;
    id: number;
    name: string;
    items: Array<CodeListItem>;
    hierarchycal: boolean;
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.items = [];
        for (var i = 0; i < jsonObj.items.length; i++) {
            let itemJsonObj = jsonObj.items[i];
            let codeListItem: CodeListItem = new CodeListItem();
            codeListItem.fillFromJSON(itemJsonObj);
            this.items.push(codeListItem);
        }
    }
}

export class CodeListItem extends Serializable {
    
    code: string;
    label: string;
    color: string;
    
    constructor() {
        super();
    }
}

export class Schema extends Serializable {
    survey: Survey;
    rootEntities: Array<EntityDefinition>;
    definitions: Array<NodeDefinition>; //cache
    
    constructor(survey: Survey) {
        super();
        this.survey = survey;
        this.definitions = [];
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        let $this = this;
        
        this.rootEntities = [];
        for (var i = 0; i < jsonObj.rootEntities.length; i++) {
            var rootEntityJsonObj = jsonObj.rootEntities[i];
            var rootEntity = new EntityDefinition(rootEntityJsonObj.id, this.survey, null);
            rootEntity.fillFromJSON(rootEntityJsonObj);
            this.rootEntities.push(rootEntity);
            rootEntity.traverse(function(nodeDef) {
                $this.definitions[nodeDef.id] = nodeDef;
            });
        }
    }
    
    public get defaultRootEntity(): EntityDefinition {
        return this.rootEntities[0];
    }
    
    getDefinitionById(id: number) {
        return this.definitions[id];
    }
}

export class NodeDefinition extends SurveyObject {
    survey: Survey;
    parent: EntityDefinition;
    name: string;
    label: string;
    multiple: boolean;
    
    constructor(id: number, survey: Survey, parent: EntityDefinition) {
        super(id);
        this.survey = survey;
        this.parent = parent;
    }
}

export class EntityDefinition extends NodeDefinition {
    children: Array<NodeDefinition>
    
    constructor(id: number, survey: Survey, parent: EntityDefinition) {
        super(id, survey, parent);
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        
        this.children = [];
        for (var i = 0; i < jsonObj.children.length; i++) {
            let nodeJsonObj = jsonObj.children[i];
            let nodeDef;
            if (nodeJsonObj.type == 'ENTITY') { 
                nodeDef = new EntityDefinition(nodeJsonObj.id, this.survey, this);
            } else {
                switch (nodeJsonObj.attributeType) {
                case 'CODE':
                    nodeDef = new CodeAttributeDefinition(nodeJsonObj.id, this.survey, this);
                    break;
                case 'NUMERIC':
                    nodeDef = new NumericAttributeDefinition(nodeJsonObj.id, this.survey, this);
                    break;
                default:
                    nodeDef = new AttributeDefinition(nodeJsonObj.id, this.survey, this);
                }
            }
            nodeDef.fillFromJSON(nodeJsonObj);
            this.children.push(nodeDef);
        }
    }
    
    public traverse(visitor: Function) {
        let stack: Array<NodeDefinition> = [];
        stack.push(this);
        while (stack.length > 0) {
            let nodeDef: NodeDefinition = stack.pop();
            visitor(nodeDef);
            if (nodeDef instanceof EntityDefinition) {
                let children = (nodeDef as EntityDefinition).children;
                for(var i = 0; i < nodeDef.children.length; i++) {
                    let child: NodeDefinition = nodeDef.children[i];
                    stack.push(child);
                }
            }
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
    attributeType: string;
    
    constructor(id: number, survey: Survey, parent: EntityDefinition) {
        super(id, survey, parent);
    }
}

export class CodeAttributeDefinition extends AttributeDefinition {
    
    codeListId: number;
    parentCodeAttributeDefinitionId: number;
    
    constructor(id: number, survey: Survey, parent: EntityDefinition) {
        super(id, survey, parent);
    }
}

export class NumericAttributeDefinition extends AttributeDefinition {
    
    numericType: string;
    
    constructor(id: number, survey: Survey, parent: EntityDefinition) {
        super(id, survey, parent);
    }
}