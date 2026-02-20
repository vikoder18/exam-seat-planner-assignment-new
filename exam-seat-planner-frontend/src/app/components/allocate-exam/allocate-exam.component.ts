import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClassroomService, AllocationResult } from '../../services/classroom.service';

@Component({
  selector: 'app-allocate-exam',
  templateUrl: './allocate-exam.component.html',
  styleUrls: ['./allocate-exam.component.scss']
})
export class AllocateExamComponent {
  allocateForm: FormGroup;
  result: AllocationResult | null = null;
  errorMessage = '';
  isLoading = false;

  constructor(private fb: FormBuilder, private classroomService: ClassroomService) {
    this.allocateForm = this.fb.group({
      totalStudents: [null, [Validators.required, Validators.min(1)]]
    });
  }

  onAllocate(): void {
    if (this.allocateForm.invalid) return;

    this.isLoading = true;
    this.result = null;
    this.errorMessage = '';

    const totalStudents = this.allocateForm.value.totalStudents;

    this.classroomService.allocateExam(totalStudents).subscribe({
      next: (data) => {
        this.result = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Allocation failed. Please try again.';
        this.isLoading = false;
      }
    });
  }

  get f() { return this.allocateForm.controls; }

  reset(): void {
    this.allocateForm.reset();
    this.result = null;
    this.errorMessage = '';
  }
}