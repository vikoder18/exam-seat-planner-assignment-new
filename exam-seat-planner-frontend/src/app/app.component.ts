import { Component, OnInit } from '@angular/core';
import { ClassroomService } from './services/classroom.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  totalRooms = 0;
  totalSeats = 0;

  navLinks = [
    { path: '/add-classroom',   label: 'Add Classroom', icon: '➕' },
    { path: '/view-classrooms', label: 'View Classrooms', icon: '📋' },
    { path: '/allocate-exam',   label: 'Allocate Exam', icon: '🎯' }
  ];

  constructor(private classroomService: ClassroomService) {}

  ngOnInit(): void {
    this.refreshStats();
    // Refresh stats every time routing happens by listening periodically
    setInterval(() => this.refreshStats(), 5000);
  }

  refreshStats(): void {
    this.classroomService.getAllClassrooms().subscribe({
      next: (rooms) => {
        this.totalRooms = rooms.length;
        this.totalSeats = rooms.reduce((sum, r) => sum + r.capacity, 0);
      },
      error: () => {}
    });
  }
}