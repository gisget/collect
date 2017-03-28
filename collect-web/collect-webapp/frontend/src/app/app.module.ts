import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { SurveyDesignerModule } from './survey-designer/survey-designer.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    SurveyDesignerModule
  ],
  entryComponents: [
      AppComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
