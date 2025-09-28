package com.univ.attendograde2v.assignment.repository;

import com.univ.attendograde2v.assignment.entity.Assignment;
import com.univ.attendograde2v.assignment.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Get all submissions for a specific assignment
    List<Submission> findByAssignment(Assignment assignment);

    // Get all submissions for a specific student
    List<Submission> findByStudentUsername(String studentUsername);
}
