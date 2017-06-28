import { Serializable } from './serializable.model';

export class Survey extends Serializable {
    id: number;
    name: string;
    uri: string;
    schema: Schema;
}

export class Schema {
    rootEntities: Array<EntityDefinition>;
    
    public getDefaultRootEntity(): EntityDefinition {
        return this.rootEntities[0];
    }
}

export class SurveyObject {
    id: number;
}    

export class NodeDefinition extends SurveyObject {
    name: string;
    label: string;
    
    constructor() {
        super();
    }
}

export class EntityDefinition extends NodeDefinition {
    children: Array<NodeDefinition>
    
    constructor() {
        super();
    }
}

export class AttributeDefinition extends NodeDefinition {
    
    constructor() {
        super();
    }
}
