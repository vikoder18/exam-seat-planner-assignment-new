import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClassroomService } from '../../services/classroom.service';

@Component({
  selector: 'app-add-classroom',
  templateUrl: './add-classroom.component.html',
  styleUrls: ['./add-classroom.component.scss']
})
export class AddClassroomComponent {
  classroomForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  isLoading = false;

  constructor(private fb: FormBuilder, private classroomService: ClassroomService) {
    this.classroomForm = this.fb.group({
      roomId: ['', [Validators.required, Validators.minLength(1)]],
      capacity: [null, [Validators.required, Validators.min(1)]],
      floorNo: [null, [Validators.required, Validators.min(0)]],
      nearWashroom: [false]
    });
  }

  onSubmit(): void {
    if (this.classroomForm.invalid) return;

    this.isLoading = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.classroomService.addClassroom(this.classroomForm.value).subscribe({
      next: (classroom) => {
        this.successMessage = `Classroom "${classroom.roomId}" added successfully!`;
        this.classroomForm.reset({ nearWashroom: false });
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Failed to add classroom. Please try again.';
        this.isLoading = false;
      }
    });
  }

  // Helper to easily access form controls in template
  get f() { return this.classroomForm.controls; }
}