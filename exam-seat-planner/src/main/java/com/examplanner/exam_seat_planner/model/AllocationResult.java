package com.examplanner.exam_seat_planner.model;

import java.util.List;

public class AllocationResult {

    private boolean success;
    private String message;
    private int totalStudents;
    private int totalSeatsAllocated;
    private int roomsUsed;
    private List<Classroom> allocatedClassrooms;

    // ---- Constructors ----

    public AllocationResult() {
    }

    public AllocationResult(boolean success, String message, int totalStudents,
                            int totalSeatsAllocated, List<Classroom> allocatedClassrooms) {
        this.success = success;
        this.message = message;
        this.totalStudents = totalStudents;
        this.totalSeatsAllocated = totalSeatsAllocated;
        this.roomsUsed = allocatedClassrooms != null ? allocatedClassrooms.size() : 0;
        this.allocatedClassrooms = allocatedClassrooms;
    }

    // ---- Getters & Setters ----

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalSeatsAllocated() {
        return totalSeatsAllocated;
    }

    public void setTotalSeatsAllocated(int totalSeatsAllocated) {
        this.totalSeatsAllocated = totalSeatsAllocated;
    }

    public int getRoomsUsed() {
        return roomsUsed;
    }

    public void setRoomsUsed(int roomsUsed) {
        this.roomsUsed = roomsUsed;
    }

    public List<Classroom> getAllocatedClassrooms() {
        return allocatedClassrooms;
    }

    public void setAllocatedClassrooms(List<Classroom> allocatedClassrooms) {
        this.allocatedClassrooms = allocatedClassrooms;
    }
}
