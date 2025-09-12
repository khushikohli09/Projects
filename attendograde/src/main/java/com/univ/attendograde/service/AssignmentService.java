package com.univ.attendograde.service;

import com.univ.attendograde.entity.Assignment;
import com.univ.attendograde.entity.Student;
import com.univ.attendograde.entity.Submission;
import com.univ.attendograde.repository.AssignmentRepository;
import com.univ.attendograde.repository.StudentRepository;
import com.univ.attendograde.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepo;
    private final SubmissionRepository submissionRepo;
    private final StudentRepository studentRepo;

    public AssignmentService(AssignmentRepository assignmentRepo,
                             SubmissionRepository submissionRepo,
                             StudentRepository studentRepo) {
        this.assignmentRepo = assignmentRepo;
        this.submissionRepo = submissionRepo;
        this.studentRepo = studentRepo;
    }

    // Add new assignment
    public Assignment addAssignment(String title, String subject, String description, String dueDateStr) {
        Assignment assignment = new Assignment();
        assignment.setTitle(title);
        assignment.setSubject(subject);
        assignment.setDescription(description);
        assignment.setDueDate(java.time.LocalDate.parse(dueDateStr));
        return assignmentRepo.save(assignment);
    }

    // Get all assignments
    public List<Assignment> getAllAssignments() {
        return assignmentRepo.findAll();
    }

    // Submit assignment (file upload)
    @Transactional
    public Submission submitAssignment(Long assignmentId, Long studentId, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found!"));

        // Define absolute path for uploads
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;
        File dir = new File(uploadDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Failed to create upload directory: " + uploadDir);
        }

        // Save file with unique name
        String filePath = uploadDir + student.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFileName(filePath);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepo.save(submission);
    }

    // Get all submissions for a given assignment
    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));
        return submissionRepo.findByAssignment(assignment);
    }

    // Grade a submission
    @Transactional
    public void gradeSubmission(Long submissionId, String grade) {
        Submission submission = submissionRepo.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        submission.setGrade(grade);  // ✅ store grade instead of marks
        submissionRepo.save(submission);
    }

    // 🔹 Extra: Get all submissions (grades) of a student (for "View My Grades")
    public List<Submission> getSubmissionsByStudent(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return submissionRepo.findByStudent(student);
    }
    public Assignment getAssignmentById(Long assignmentId) {
        return assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found!"));
    }

}
