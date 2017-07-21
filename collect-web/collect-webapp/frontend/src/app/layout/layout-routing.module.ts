import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LayoutComponent } from './layout.component';

const routes: Routes = [
    {
        path: '', component: LayoutComponent,
        children: [
            { 
                path: 'home-page', 
                loadChildren: '../home-page/home-page.module#HomePageModule'
            },
            { 
                path: 'dashboard', 
                loadChildren: '../dashboard/dashboard.module#DashboardModule',
                data: {
                    breadcrumb: "Dashboard"
                }
           },
            { 
                path: 'data-management', 
                loadChildren: '../data-management/data-management.module#DataManagementModule',
                data: {
                    breadcrumb: "Data Management"
                }
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
