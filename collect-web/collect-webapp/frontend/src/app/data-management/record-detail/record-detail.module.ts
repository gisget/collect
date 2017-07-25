import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TabViewModule } from 'primeng/primeng';

import { TranslateModule } from '@ngx-translate/core';

import { RecordDetailComponent } from './record-detail.component';
import { TabSetComponent } from './tabset/tabset.component';
import { TabComponent } from './tab/tab.component';
import { TextFieldComponent } from './form-items/text-field/text-field.component';

@NgModule({
    imports: [
        CommonModule,
        TranslateModule,
        TabViewModule
    ],
    declarations: [
        RecordDetailComponent,
        TabSetComponent,
        TabComponent,
        TextFieldComponent
    ]
})
export class RecordDetailModule { }
