import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }   from '@angular/forms';

import { ButtonModule, CheckboxModule, DataTableModule, DropdownModule, FieldsetModule, InputTextModule, 
    MenubarModule, RadioButtonModule, TabViewModule } from 'primeng/primeng';
import { TranslateModule } from '@ngx-translate/core';

import { RecordDetailRoutingModule } from './record-detail-routing.module';
import { RecordDetailComponent } from './record-detail.component';
import { TabComponent } from './tab/tab.component';
import { TabSetComponent } from './tabset/tabset.component'; 
import { BooleanFieldComponent, CodeFieldComponent, CoordinateFieldComponent, DateFieldComponent, 
    FieldsetComponent, FileFieldComponent, FormItemComponent, FormItemsComponent,
    InputFieldComponent, NumericFieldComponent, NumericIntegerFieldComponent, NumericRealFieldComponent, 
    TextFieldComponent, TimeFieldComponent } from './form-items';

@NgModule({
    imports: [
        CommonModule, FormsModule,
        RecordDetailRoutingModule,
        ButtonModule, CheckboxModule, DataTableModule, DropdownModule, FieldsetModule, InputTextModule, 
        MenubarModule, RadioButtonModule, TabViewModule,
        TranslateModule
    ],
    declarations: [
        RecordDetailComponent,
        BooleanFieldComponent, CodeFieldComponent, CoordinateFieldComponent, DateFieldComponent, 
        FieldsetComponent, FileFieldComponent, FormItemComponent, FormItemsComponent,
        InputFieldComponent, NumericFieldComponent, NumericIntegerFieldComponent, NumericRealFieldComponent, 
        TextFieldComponent, TimeFieldComponent,
        TabComponent, TabSetComponent
    ]
})
export class RecordDetailModule { }
