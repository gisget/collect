import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DataTableModule } from 'primeng/primeng';
import { DropdownModule } from 'primeng/primeng';
import { TranslateModule } from '@ngx-translate/core';

import { HomePageRoutingModule }  from './home-page-routing.module';
import { HomePageComponent }      from './home-page.component';


@NgModule({
    imports: [
        CommonModule,
        HomePageRoutingModule,
        TranslateModule
    ],
    declarations: [
        HomePageComponent
    ]
})
export class HomePageModule { }
