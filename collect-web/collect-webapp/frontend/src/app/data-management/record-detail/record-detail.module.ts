import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule }   from '@angular/forms';

import { ButtonModule, CalendarModule, CheckboxModule, DataTableModule, DropdownModule, FieldsetModule, InputTextModule, 
    MenubarModule, RadioButtonModule, SpinnerModule, TabViewModule } from 'primeng/primeng';
import { TranslateModule } from '@ngx-translate/core';

import { RecordDetailRoutingModule } from './record-detail-routing.module';
import { RecordDetailComponent } from './record-detail.component';
import { TabComponent } from './tab/tab.component';
import { TabSetComponent } from './tabset/tabset.component';
import { BooleanFieldComponent, CodeFieldComponent, CoordinateFieldComponent, DateFieldComponent, 
    FieldsetComponent, FileFieldComponent, FormItemComponent, FormItemsComponent,
    InputFieldComponent, MultipleFieldsetComponent, NumberFieldComponent, 
    TextFieldComponent, TimeFieldComponent } from './form-items';

@NgModule({
    imports: [
        CommonModule, FormsModule,
        RecordDetailRoutingModule,
        ButtonModule, CalendarModule, CheckboxModule, DataTableModule, DropdownModule, FieldsetModule, InputTextModule, 
        MenubarModule, RadioButtonModule, SpinnerModule, TabViewModule,
        TranslateModule
    ],
    declarations: [
        RecordDetailComponent,
        BooleanFieldComponent, CodeFieldComponent, CoordinateFieldComponent, DateFieldComponent, 
        FieldsetComponent, FileFieldComponent, FormItemComponent, FormItemsComponent,
        InputFieldComponent, NumberFieldComponent,
        TextFieldComponent, TimeFieldComponent,
        TabComponent, TabSetComponent, MultipleFieldsetComponent
    ]
})
export class RecordDetailModule { }
