import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Classroom {
  id?: number;
  roomId: string;
  capacity: number;
  floorNo: number;
  nearWashroom: boolean;
}

export interface AllocationResult {
  success: boolean;
  message: string;
  totalStudents: number;
  totalSeatsAllocated: number;
  roomsUsed: number;
  allocatedClassrooms: Classroom[];
}

@Injectable({
  providedIn: 'root'
})
export class ClassroomService {
  private readonly API_URL = 'http://16.16.70.54:8080/api/classrooms'; // http://16.16.70.54/add-classroom

  constructor(private http: HttpClient) {}

  addClassroom(classroom: Classroom): Observable<Classroom> {
    return this.http.post<Classroom>(this.API_URL, classroom);
  }

  getAllClassrooms(): Observable<Classroom[]> {
    return this.http.get<Classroom[]>(this.API_URL);
  }

  allocateExam(totalStudents: number): Observable<AllocationResult> {
    return this.http.post<AllocationResult>(`${this.API_URL}/allocate`, { totalStudents });
  }
}