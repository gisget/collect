import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ButtonModule, MenubarModule, DataTableModule, TabViewModule, FieldsetModule } from 'primeng/primeng';

import { TranslateModule }              from '@ngx-translate/core';

import { DataManagementRoutingModule }  from './data-management-routing.module';
import { DataManagementComponent }      from './data-management.component';
import { RecordTableComponent }         from './record-table/record-table.component';
import { RecordDetailComponent }         from './record-detail/record-detail.component';
import { FieldsetComponent, FormItemsComponent, FormItemComponent, 
        TabSetComponent, TabComponent } from './record-detail';

@NgModule({
    imports: [
        CommonModule,
        DataManagementRoutingModule,
        ButtonModule, DataTableModule, MenubarModule, TabViewModule, FieldsetModule,
        TranslateModule
    ],
    declarations: [
        DataManagementComponent,
        RecordTableComponent,
        RecordDetailComponent,
        FieldsetComponent, FormItemsComponent, FormItemComponent, TabSetComponent, TabComponent
    ]
})
export class DataManagementModule { }
