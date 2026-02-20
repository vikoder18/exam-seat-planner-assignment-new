import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddClassroomComponent } from './components/add-classroom/add-classroom.component';
import { ViewClassroomsComponent } from './components/view-classrooms/view-classrooms.component';
import { AllocateExamComponent } from './components/allocate-exam/allocate-exam.component';

const routes: Routes = [
  { path: '',            redirectTo: 'add-classroom', pathMatch: 'full' },
  { path: 'add-classroom',      component: AddClassroomComponent },
  { path: 'view-classrooms',    component: ViewClassroomsComponent },
  { path: 'allocate-exam',      component: AllocateExamComponent },
  { path: '**',          redirectTo: 'add-classroom' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}