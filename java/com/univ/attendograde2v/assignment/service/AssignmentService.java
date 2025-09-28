package com.univ.attendograde2v.assignment.service;

import com.univ.attendograde2v.assignment.entity.Assignment;
import com.univ.attendograde2v.assignment.entity.Submission;
import com.univ.attendograde2v.assignment.repository.AssignmentRepository;
import com.univ.attendograde2v.assignment.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepo;
    private final SubmissionRepository submissionRepo;

    public AssignmentService(AssignmentRepository assignmentRepo,
                             SubmissionRepository submissionRepo) {
        this.assignmentRepo = assignmentRepo;
        this.submissionRepo = submissionRepo;
    }

    // Add new assignment
    public Assignment addAssignment(String title, String subject, String description, String dueDateStr) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setSubject(subject);
        assignment.setDescription(description);
        assignment.setDueDate(LocalDate.parse(dueDateStr));
        return assignmentRepo.save(assignment);
    }

    // Get all assignments
    public List<Assignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    // Submit assignment
    @Transactional
    public Submission submitAssignment(Long assignmentId, String studentUsername, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));

        // Upload directory
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create upload directory: " + uploadDir);
        }

        String filePath = uploadDir + studentUsername + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudentUsername(studentUsername); // ✅ Store username
        submission.setFileName(filePath);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepo.save(submission);
    }

    // Get submissions by assignment
    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));
        return submissionRepo.findByAssignment(assignment);
    }

    // Grade submission
    @Transactional
    public void gradeSubmission(Long submissionId, String grade) {
        Submission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setGrade(grade);
        submissionRepo.save(submission);
    }

    // Get all submissions by a student
    public List<Submission> getSubmissionsByStudent(String studentUsername) {
        return submissionRepo.findByStudentUsername(studentUsername);
    }
}
