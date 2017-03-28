import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { Routes } from '@angular/router';

import { MenuModule } from 'primeng/primeng';

import { AppComponent } from './app.component';

import { DataManagementModule } from './data-management/data-management.module';
import { DataManagementComponent } from './data-management/data-management.component';

const routes: Routes = [
    { path: 'data-management', component: DataManagementComponent }
];

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    DataManagementModule,
    MenuModule,
    RouterModule.forRoot(routes)
  ],
  entryComponents: [
      AppComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
