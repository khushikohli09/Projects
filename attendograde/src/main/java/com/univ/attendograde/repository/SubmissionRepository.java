package com.univ.attendograde.repository;

import com.univ.attendograde.entity.Submission;
import com.univ.attendograde.entity.Assignment;
import com.univ.attendograde.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Get all submissions for a given assignment
    List<Submission> findByAssignment(Assignment assignment);

    // Get a specific submission of a student for an assignment
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, Student student);

    // 🔹 Get all submissions of a particular student (useful for "View My Grades")
    List<Submission> findByStudent(Student student);

    // 🔹 Get a submission by assignment id + student id (in case you don’t have entity objects handy)
    Optional<Submission> findByAssignmentIdAndStudentId(Long assignmentId, Long studentId);
}
