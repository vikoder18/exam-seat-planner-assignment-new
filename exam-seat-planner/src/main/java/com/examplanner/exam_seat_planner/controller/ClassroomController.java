package com.examplanner.exam_seat_planner.controller;


import com.examplanner.exam_seat_planner.model.AllocationResult;
import com.examplanner.exam_seat_planner.model.Classroom;
import com.examplanner.exam_seat_planner.service.ClassroomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller exposing Classroom API endpoints.
 *
 * Endpoints:
 *   POST   /api/classrooms              → Add a classroom
 *   GET    /api/classrooms              → List all classrooms
 *   POST   /api/classrooms/allocate     → Allocate exam seats (greedy)
 */
@RestController
@RequestMapping("/api/classrooms")
@Validated
@CrossOrigin(origins = {
    "http://localhost:4200",    // Angular dev server
    "http://localhost:80",      // Docker frontend
    "http://localhost"  ,
    "http://16.16.70.54:80" ,
    "http://16.16.70.54"  ,     // Docker frontend (default port 80)
    "http://16.16.70.54:4200"
})
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    // -------------------------------------------------------------------------
    // POST /api/classrooms
    // Body: { roomId, capacity, floorNo, nearWashroom }
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<Classroom> addClassroom(@Valid @RequestBody Classroom classroom) {
        Classroom saved = classroomService.addClassroom(classroom);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // -------------------------------------------------------------------------
    // GET /api/classrooms
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    // -------------------------------------------------------------------------
    // POST /api/classrooms/allocate
    // Body: { totalStudents: <number> }
    // -------------------------------------------------------------------------
    @PostMapping("/allocate")
    public ResponseEntity<AllocationResult> allocateExam(
            @RequestBody Map<String, Integer> requestBody) {

        Integer totalStudents = requestBody.get("totalStudents");

        if (totalStudents == null) {
            throw new IllegalArgumentException("'totalStudents' field is required in the request body.");
        }
        if (totalStudents <= 0) {
            throw new IllegalArgumentException("'totalStudents' must be a positive integer.");
        }

        AllocationResult result = classroomService.allocateExam(totalStudents);
        return ResponseEntity.ok(result);
    }
}
