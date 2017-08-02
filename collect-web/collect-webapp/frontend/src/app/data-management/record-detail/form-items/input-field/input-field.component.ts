import { Component, OnInit, Input } from '@angular/core';
import { FieldDefinition } from 'app/shared/model/ui';
import { Attribute, Entity, Event, RecordEvent } from 'app/shared/model';
import { CommandService } from 'app/shared/services';

@Component({
    selector: 'ofc-input-field',
    templateUrl: './input-field.component.html',
    styleUrls: ['./input-field.component.scss']
})
//abstract
export class InputFieldComponent implements OnInit {

    _fieldDefinition: FieldDefinition;
    _attribute: Attribute;
    
    constructor(protected commandService: CommandService) {
    }
    
    @Input() 
    set fieldDefinition(fieldDef: FieldDefinition) {
        this._fieldDefinition = fieldDef;
    }
    
    @Input() 
    set attribute(attribute: Attribute) {
        this._attribute = attribute;
        this.updateSelectedValue();
    }
    
    ngOnInit() {
    }
    
    updateSelectedValue() {
    }
    
    onSelectedValueChange() {
        this.sendUpdateAttributeCommand();
    }
    
    sendUpdateAttributeCommand() {
        this.commandService.updateAttribute(this.attribute, this.fieldDefinition.attributeType, this.updateCommandValue)
            .subscribe(events => console.log(events));
    }
    
    onEventReceived(event: Event) {
        if (event instanceof RecordEvent) {
            let recordEvent: RecordEvent = <RecordEvent>event;
            if (this.attribute != null && this.attribute.record.id == recordEvent.recordId 
                    && this.attribute.record.step == recordEvent.recordStep 
                    && this.attribute.id == parseInt(recordEvent.nodeId)) {
                console.log("attribute updated");
                this.updateSelectedValue();
            }
        }
    }
    
    get fieldDefinition(): FieldDefinition {
        return this._fieldDefinition;
    }
    
    get attribute(): Attribute {
        return this._attribute;
    }
    
    get updateCommandValue(): Object {
        return null;
    }
}
