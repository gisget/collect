import { Serializable } from '../serializable.model';
import { CodeFieldDefinition } from './code-field-definition.model';
import { FieldDefinition } from './field-definition.model';
import { FieldsetDefinition } from './fieldset-definition.model';
import { FormComponentDefinition } from './form-component-definition.model';
import { MultipleFieldsetDefinition } from './multiple-fieldset-definition.model';
import { TabDefinition } from './tab-definition.model';
import { UIModelObjectDefinition } from './ui-model-object-definition.model';

export class TabContainers {
	
    public static createTabsFromJSON(jsonArrObj, parentUIModelObject: UIModelObjectDefinition): Array<TabDefinition> {
        var tabs: Array<TabDefinition> = [];
        for (var i = 0; i < jsonArrObj.length; i++) {
            var itemJsonObj = jsonArrObj[i];
            var item = new TabDefinition(itemJsonObj.id, parentUIModelObject);
            item.fillFromJSON(itemJsonObj);
            tabs.push(item);
        }
        return tabs;
    }
    
    public static createItemsFromJSON(jsonObj, parentUIModelObject: UIModelObjectDefinition): Array<FormComponentDefinition> {
        let items = [];
        for (var i = 0; i < jsonObj.length; i++) {
            var itemJsonObj = jsonObj[i];
            var item:Serializable;
            switch(itemJsonObj.type) {
            case 'FIELD':
                if (itemJsonObj.attributeType == 'CODE') {
                    item = new CodeFieldDefinition(itemJsonObj.id, parentUIModelObject);
                } else {
                    item = new FieldDefinition(itemJsonObj.id, parentUIModelObject);
                }
                break;
            case 'FIELDSET':
                item = new FieldsetDefinition(itemJsonObj.id, parentUIModelObject);
                break;
            case 'MULTIPLE_FIELDSET':
                item = new MultipleFieldsetDefinition(itemJsonObj.id, parentUIModelObject);
                break;
            default:
                item = null;
            }
            if (item != null) {
                item.fillFromJSON(itemJsonObj);
                items.push(item);
            }
        }
        return items;
    }
}