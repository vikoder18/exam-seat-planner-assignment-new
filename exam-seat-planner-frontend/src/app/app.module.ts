import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AddClassroomComponent } from './components/add-classroom/add-classroom.component';
import { ViewClassroomsComponent } from './components/view-classrooms/view-classrooms.component';
import { AllocateExamComponent } from './components/allocate-exam/allocate-exam.component';

@NgModule({
  declarations: [
    AppComponent,
    AddClassroomComponent,
    ViewClassroomsComponent,
    AllocateExamComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}