import { Component } from '@angular/core';
import { InputFieldComponent } from '../input-field/input-field.component';

@Component({
  selector: 'ofc-code-field',
  templateUrl: './code-field.component.html',
  styleUrls: ['./code-field.component.scss']
})
export class CodeFieldComponent extends InputFieldComponent {

  constructor() {
      super();
  }

  ngOnInit() {
  }

}
