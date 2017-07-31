import { Component } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';
import { CommandService } from 'app/shared/services';

@Component({
    selector: 'ofc-coordinate-field',
    templateUrl: './coordinate-field.component.html',
    styleUrls: ['./coordinate-field.component.scss']
})
export class CoordinateFieldComponent extends InputFieldComponent {

    constructor(protected commandService: CommandService) {
        super(commandService);
    }

    ngOnInit() {
    }

}
