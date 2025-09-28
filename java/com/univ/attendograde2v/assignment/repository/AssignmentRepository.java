package com.univ.attendograde2v.assignment.repository;

import com.univ.attendograde2v.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // You can add custom queries here if needed in future
}
