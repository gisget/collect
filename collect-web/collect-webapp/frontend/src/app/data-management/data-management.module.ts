import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { ReactiveFormsModule } from '@angular/forms';

import { TabViewModule } from 'primeng/primeng';
import { InputTextModule } from 'primeng/primeng';
import { CheckboxModule } from 'primeng/primeng';
import { DropdownModule } from 'primeng/primeng';
import { FieldsetModule } from 'primeng/primeng';
import { AccordionModule } from 'primeng/primeng';
import { ToolbarModule } from 'primeng/primeng';
import { ButtonModule } from 'primeng/primeng';
import { CalendarModule } from 'primeng/primeng';
import { RadioButtonModule } from 'primeng/primeng';
import { SpinnerModule } from 'primeng/primeng';
import { ConfirmDialogModule, ConfirmationService } from 'primeng/primeng';
import { DataTableModule, SharedModule } from 'primeng/primeng';

import { DataManagementComponent } from './data-management.component';
import { ChildrenComponent } from './children/children.component';
import { TabsComponent } from './tabs/tabs.component';
import { TabComponent } from './tab/tab.component';
import { FieldNumberComponent } from './field/field-number.component';
import { FieldDateComponent } from './field/field-date.component';
import { FieldBooleanComponent } from './field/field-boolean.component';
import { FieldCodeComponent } from './field/field-code.component';
import { FieldsetComponent } from './fieldset/fieldset.component';
import { MultipleFieldsetComponent } from './multiple-fieldset/multiple-fieldset.component';
import { TabsetComponent } from './tabset/tabset.component';
import { TableComponent } from './table/table.component';
import { CoordsComponent } from './field/field-coords.component';
import { SurveyService } from './shared/survey.service';

@NgModule({
    declarations: [
        DataManagementComponent,
        ChildrenComponent,
        TabsComponent,
        TabComponent,
        FieldCodeComponent,
        FieldBooleanComponent,
        FieldNumberComponent,
        FieldDateComponent,
        FieldsetComponent,
        MultipleFieldsetComponent,
        TabsetComponent,
        TableComponent,
        CoordsComponent
    ],
    exports: [
        DataManagementComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        HttpModule,
        ReactiveFormsModule,
        TabViewModule,
        InputTextModule,
        CheckboxModule,
        DropdownModule,
        FieldsetModule,
        AccordionModule,
        ToolbarModule,
        SpinnerModule,
        ButtonModule,
        CalendarModule,
        RadioButtonModule,
        ConfirmDialogModule,
        DataTableModule,
        SharedModule
    ],
    providers: [
        ConfirmationService,
        SurveyService
    ],
    entryComponents: [
        DataManagementComponent,
        MultipleFieldsetComponent,
        FieldsetComponent
    ]
})
export class DataManagementModule { }
