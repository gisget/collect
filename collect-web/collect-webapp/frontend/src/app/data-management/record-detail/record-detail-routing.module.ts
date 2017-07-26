import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RecordDetailComponent } from './record-detail.component';

const routes: Routes = [
    { 
        path: ':id', 
        component: RecordDetailComponent,
        data: {
          breadcrumb: "Record Detail"
        }
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class RecordDetailRoutingModule { }
