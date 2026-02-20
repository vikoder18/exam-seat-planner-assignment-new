package com.examplanner.exam_seat_planner.repository;

import com.examplanner.exam_seat_planner.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, Long> {

    /**
     * Check if a room with the given roomId already exists.
     */
    boolean existsByRoomId(String roomId);

    /**
     * Find a classroom by its roomId.
     */
    Optional<Classroom> findByRoomId(String roomId);

    /**
     * Fetch all classrooms sorted by floor (ascending) then by capacity (descending).
     * This ordering enables the greedy allocation algorithm to:
     *  - Prefer lower-floor rooms first
     *  - Within the same floor, use larger rooms first (minimises total rooms used)
     */
    @Query("SELECT c FROM Classroom c ORDER BY c.floorNo ASC, c.capacity DESC")
    List<Classroom> findAllSortedForAllocation();
}
