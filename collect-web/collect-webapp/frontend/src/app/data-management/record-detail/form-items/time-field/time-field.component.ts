import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';
import { Attribute } from 'app/shared/model';
import { CommandService } from 'app/shared/services';

@Component({
    selector: 'ofc-time-field',
    templateUrl: './time-field.component.html',
    styleUrls: ['./time-field.component.scss']
})
export class TimeFieldComponent extends InputFieldComponent {

    _value: Date;
    
    constructor(protected commandService: CommandService) {
        super(commandService);
    }
    
    get value(): Date {
        return this._value;
    }
    
    set value(value: Date) {
        this._value = value;
    }

    ngOnInit() {
    }

    updateSelectedValue() {
        let attr: Attribute = this.attribute;
        if (attr != null && attr.allFieldsFilled) {
            let val: Date = new Date();
            val.setHours(attr.fields[0].intValue, attr.fields[1].intValue);
            this._value = val;
        } else {
            this._value = null;
        }
    }
}
