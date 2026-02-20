import { Component, OnInit } from '@angular/core';
import { ClassroomService, Classroom } from '../../services/classroom.service';

@Component({
  selector: 'app-view-classrooms',
  templateUrl: './view-classrooms.component.html',
  styleUrls: ['./view-classrooms.component.scss']
})
export class ViewClassroomsComponent implements OnInit {
  classrooms: Classroom[] = [];
  isLoading = false;
  errorMessage = '';

  constructor(private classroomService: ClassroomService) {}

  ngOnInit(): void {
    this.loadClassrooms();
  }

  loadClassrooms(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.classroomService.getAllClassrooms().subscribe({
      next: (data) => {
        this.classrooms = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to load classrooms.';
        this.isLoading = false;
      }
    });
  }

  get totalSeats(): number {
    return this.classrooms.reduce((sum, c) => sum + c.capacity, 0);
  }
}