import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataManagementComponent } from './data-management.component';
import { RecordDetailComponent } from './record-detail/record-detail.component';

const routes: Routes = [
    { 
        path: '', 
        component: DataManagementComponent
    },
    { 
        path: ':id', 
        component: RecordDetailComponent,
        data: {
            breadcrumb: "Record Detail"
        }
//        loadChildren: '../data-management/record-detail/record-detail.module#RecordDetailModule'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DataManagementRoutingModule { }
