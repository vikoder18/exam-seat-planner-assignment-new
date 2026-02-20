package com.examplanner.exam_seat_planner.service;

import com.examplanner.exam_seat_planner.exception.DuplicateRoomException;
import com.examplanner.exam_seat_planner.exception.InsufficientSeatsException;
import com.examplanner.exam_seat_planner.model.AllocationResult;
import com.examplanner.exam_seat_planner.model.Classroom;
import com.examplanner.exam_seat_planner.repository.ClassroomRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ClassroomService {

    private final ClassroomRepository classroomRepository;

    // Constructor injection (preferred over @Autowired)
    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

//    add classrooms
    public Classroom addClassroom(Classroom classroom) {
        // Guard: duplicate roomId
        if (classroomRepository.existsByRoomId(classroom.getRoomId())) {
            throw new DuplicateRoomException("Room ID '" + classroom.getRoomId() + "' already exists.");
        }
        return classroomRepository.save(classroom);
    }

//view Classrooms
    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }


    public AllocationResult allocateExam(int totalStudents) {
        if (totalStudents <= 0) {
            throw new IllegalArgumentException("Total students must be a positive number.");
        }

        // Sorted: floor ASC, capacity DESC
        List<Classroom> floorSorted = classroomRepository.findAllSortedForAllocation();

        if (floorSorted.isEmpty()) {
            throw new InsufficientSeatsException("No classrooms available. Please add classrooms first.");
        }

        int totalAvailable = floorSorted.stream().mapToInt(Classroom::getCapacity).sum();
        if (totalAvailable < totalStudents) {
            throw new InsufficientSeatsException(
                    "Not enough seats available. Required: " + totalStudents +
                            ", Available: " + totalAvailable
            );
        }

        // ---------------------------------------------------------------
        // PHASE 1: Floor-priority greedy
        // Pick rooms in floor ASC, capacity DESC order until seats are met
        // ---------------------------------------------------------------
        List<Classroom> phase1Result = new ArrayList<>();
        int phase1Seats = 0;

        for (Classroom room : floorSorted) {
            phase1Result.add(room);
            phase1Seats += room.getCapacity();
            if (phase1Seats >= totalStudents) break;
        }

        // The minimum floor in phase1 is always index 0 since list is floor ASC
        int minFloor = phase1Result.get(0).getFloorNo();

        // ---------------------------------------------------------------
        // PHASE 2: Optimization — can we use fewer rooms?
        // Rule: new combination MUST include at least one room from minFloor
        // Strategy:
        //   - Lock in the LARGEST room from minFloor (guarantees floor priority)
        //   - Fill remaining seats greedily using largest capacity rooms available
        //   - If this uses fewer rooms than phase1 → use it, else keep phase1
        // ---------------------------------------------------------------

        // Find the largest room on the minimum floor
        Classroom anchorRoom = floorSorted.stream()
                .filter(r -> r.getFloorNo() == minFloor)
                .max(Comparator.comparingInt(Classroom::getCapacity))
                .orElse(phase1Result.get(0));

        List<Classroom> phase2Result = new ArrayList<>();
        phase2Result.add(anchorRoom);
        int phase2Seats = anchorRoom.getCapacity();

        if (phase2Seats < totalStudents) {
            List<Classroom> remaining = floorSorted.stream()
                    .filter(r -> !r.getRoomId().equals(anchorRoom.getRoomId()))
                    .sorted((a, b) -> b.getCapacity() - a.getCapacity())
                    .collect(java.util.stream.Collectors.toList());

            for (Classroom room : remaining) {
                phase2Result.add(room);
                phase2Seats += room.getCapacity();
                if (phase2Seats >= totalStudents) break;
            }
        }

        // Pick whichever solution uses fewer rooms
        List<Classroom> finalResult = (phase2Result.size() < phase1Result.size())
                ? phase2Result
                : phase1Result;
        finalResult.sort(Comparator.comparingInt(Classroom::getFloorNo)
                .thenComparing(Comparator.comparingInt(Classroom::getCapacity).reversed()));

        int finalSeats = finalResult.stream().mapToInt(Classroom::getCapacity).sum();

        return new AllocationResult(
                true,
                "Allocation successful. " + finalResult.size() + " room(s) assigned.",
                totalStudents,
                finalSeats,
                finalResult
        );
    }
}
