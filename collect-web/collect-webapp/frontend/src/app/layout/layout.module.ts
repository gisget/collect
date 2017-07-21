import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';

import { DropdownModule, BreadcrumbModule, MenuItem } from 'primeng/primeng';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { HeaderComponent, SidebarComponent, SurveySelectorComponent, BreadcrumbComponent } from 'app/shared';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        NgbDropdownModule.forRoot(),
        DropdownModule, BreadcrumbModule,
        LayoutRoutingModule,
        TranslateModule
    ],
    declarations: [
        LayoutComponent,
        HeaderComponent,
        SidebarComponent,
        SurveySelectorComponent,
        BreadcrumbComponent
    ]
})
export class LayoutModule { }
