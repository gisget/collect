import { Component, OnInit } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';
import { Attribute } from 'app/shared/model';
import { CommandService } from 'app/shared/services';

@Component({
    selector: 'ofc-date-field',
    templateUrl: './date-field.component.html',
    styleUrls: ['./date-field.component.scss']
})
export class DateFieldComponent extends InputFieldComponent {
    
    _value: Date;
    
    constructor(protected commandService: CommandService) {
        super(commandService);
    }

    ngOnInit() {
    }

    get value(): Date {
        return this._value;
    }
    
    set value(value: Date) {
        this._value = value;
        this.onSelectedValueChange();
    }
    
    get updateCommandValue(): Object {
        return this._value == null ? null : 
            {year: this._value.getFullYear(), month: this._value.getMonth(), day: this._value.getDay()};
    }
    
    updateSelectedValue() {
        let attr: Attribute = this.attribute;
        if (attr != null && attr.allFieldsFilled) {
            this._value = new Date(attr.fields[0].intValue, 
                attr.fields[1].intValue, attr.fields[2].intValue);
        } else {
            this._value = null;
        }
    }
    
}

