import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule, MenubarModule, DataTableModule, TabViewModule, FieldsetModule } from 'primeng/primeng';

import { TranslateModule }              from '@ngx-translate/core';

import { DataManagementRoutingModule }  from './data-management-routing.module';
import { DataManagementComponent }      from './data-management.component';
import { RecordTableComponent }         from './record-table/record-table.component';

@NgModule({
    imports: [
        CommonModule,
        DataManagementRoutingModule,
        ButtonModule, DataTableModule, MenubarModule, TabViewModule, FieldsetModule,
        TranslateModule
    ],
    declarations: [
        DataManagementComponent,
        RecordTableComponent
    ]
})
export class DataManagementModule { }
