import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule, MenubarModule, DataTableModule }      from 'primeng/primeng';

import { TranslateModule }              from '@ngx-translate/core';

import { DataManagementRoutingModule }  from './data-management-routing.module';
import { DataManagementComponent }      from './data-management.component';
import { RecordTableComponent }         from './record-table/record-table.component';
import { DataEntryFormComponent }       from './data-entry/data-entry-form.component';

@NgModule({
    imports: [
        CommonModule,
        DataManagementRoutingModule,
        ButtonModule, DataTableModule, MenubarModule, 
        TranslateModule
    ],
    declarations: [
        DataManagementComponent,
        RecordTableComponent, 
        DataEntryFormComponent
    ]
})
export class DataManagementModule { }
