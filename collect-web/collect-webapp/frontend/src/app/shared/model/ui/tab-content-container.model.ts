import { Serializable } from '../serializable.model';
import { UIModelObject } from './ui-model-object.model';
import { Tab } from './tab.model';
import { Field } from './field.model';
import { Fieldset } from './fieldset.model';
import { FormComponent } from './form-component.model';

export class TabContentContainer extends UIModelObject {
	children: Array<FormComponent>;
    tabs: Tab[];
    
    constructor(id: number, parent: UIModelObject) {
        super(id, parent);
        this.children = [];
        this.tabs = [];
    }
    
    fillFromJSON(jsonObj) {
        super.fillFromJSON(jsonObj);
        this.tabs = this.createTabsFromJSON(jsonObj.tabs);
        this.children = this.createChildrenFromJSON(jsonObj.children); 
    }
    
    private createTabsFromJSON(jsonArrObj): Array<Tab> {
        var tabs: Array<Tab> = [];
        for (var i = 0; i < jsonArrObj.length; i++) {
            var itemJsonObj = jsonArrObj[i];
            var item = new TabContentContainer(itemJsonObj.id, this);
            item.fillFromJSON(itemJsonObj);
            tabs.push(item);
        }
        return tabs;
    }
    
    private createChildrenFromJSON(jsonObj):Array<FormComponent> {
        let children = [];
        for (var i = 0; i < jsonObj.length; i++) {
            var itemJsonObj = jsonObj[i];
            var item:Serializable;
            switch(itemJsonObj.type) {
            case 'FIELD':
                item = new Field(itemJsonObj.id, this);
                break;
//            case 'FIELDSET':
//                item = new Fieldset(itemJsonObj.id, this);
//                break;
            default: 
                item = null;
            }
            if (item != null) {
                item.fillFromJSON(itemJsonObj);
                children.push(item);
            }
        }
        return children;
    }
}