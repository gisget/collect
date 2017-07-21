import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule, MenubarModule, DataTableModule, TabViewModule } from 'primeng/primeng';

import { TranslateModule }              from '@ngx-translate/core';

import { DataManagementRoutingModule }  from './data-management-routing.module';
import { DataManagementComponent }      from './data-management.component';
import { RecordTableComponent }         from './record-table/record-table.component';
import { RecordDetailComponent }         from './record-detail/record-detail.component';
import { TabSetComponent, TabComponent } from './record-detail';

@NgModule({
    imports: [
        CommonModule,
        DataManagementRoutingModule,
        ButtonModule, DataTableModule, MenubarModule, TabViewModule, 
        TranslateModule
    ],
    declarations: [
        DataManagementComponent,
        RecordTableComponent,
        RecordDetailComponent,
        TabSetComponent, TabComponent
    ]
})
export class DataManagementModule { }
