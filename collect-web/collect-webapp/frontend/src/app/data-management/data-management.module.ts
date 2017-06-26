import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DataTableModule }              from 'primeng/primeng';
import { TranslateModule }              from '@ngx-translate/core';

import { DataManagementRoutingModule }  from './data-management-routing.module';
import { DataManagementComponent }      from './data-management.component';
import { RecordTableComponent }         from './record-table/record-table.component';

@NgModule({
    imports: [
        CommonModule,
        DataManagementRoutingModule,
        DataTableModule,
        TranslateModule
    ],
    declarations: [
        DataManagementComponent,
        RecordTableComponent
    ]
})
export class DataManagementModule { }
