package com.examplanner.exam_seat_planner.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "classrooms")
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Room ID must not be blank")
    @Column(unique = true, nullable = false)
    private String roomId;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Column(nullable = false)
    private Integer capacity;

    @NotNull(message = "Floor number is required")
    @Min(value = 0, message = "Floor number must be 0 or greater")
    @Column(nullable = false)
    private Integer floorNo;

    @Column(nullable = false)
    private Boolean nearWashroom = false;

    // ---- Constructors ----

    public Classroom() {}

    public Classroom(String roomId, Integer capacity, Integer floorNo, Boolean nearWashroom) {
        this.roomId = roomId;
        this.capacity = capacity;
        this.floorNo = floorNo;
        this.nearWashroom = nearWashroom;
    }

    // ---- Getters & Setters ----

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getFloorNo() { return floorNo; }
    public void setFloorNo(Integer floorNo) { this.floorNo = floorNo; }

    public Boolean getNearWashroom() { return nearWashroom; }
    public void setNearWashroom(Boolean nearWashroom) { this.nearWashroom = nearWashroom; }

    @Override
    public String toString() {
        return "Classroom{roomId='" + roomId + "', capacity=" + capacity +
                ", floorNo=" + floorNo + ", nearWashroom=" + nearWashroom + "}";
    }
}
