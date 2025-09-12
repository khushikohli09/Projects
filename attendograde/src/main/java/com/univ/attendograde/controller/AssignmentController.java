package com.univ.attendograde.controller;

import com.univ.attendograde.entity.Assignment;
import com.univ.attendograde.entity.Submission;
import com.univ.attendograde.service.AssignmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/faculty/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    /** ---------------- Faculty: Add Assignment ---------------- */
    @GetMapping("/add")
    public String addAssignmentForm() {
        return "add-assignments";
    }

    @PostMapping("/add")
    public String addAssignmentSubmit(@RequestParam String title,
                                      @RequestParam String subject,
                                      @RequestParam String description,
                                      @RequestParam String dueDate,
                                      Model model) {
        assignmentService.addAssignment(title, subject, description, dueDate);
        model.addAttribute("message", "Assignment added successfully!");
        return "add-assignments";
    }

    /** ---------------- Faculty: View All Assignments ---------------- */
    @GetMapping("/all")
    public String viewAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        model.addAttribute("assignments", assignments);
        return "view-assignments";   // faculty view
    }

    /** ---------------- Student: View All Assignments ---------------- */
    @GetMapping("/student/assignments")
    public String viewAllAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        model.addAttribute("assignments", assignments);
        return "view-assignments_student";  // student view
    }

    /** ---------------- Student: Submit Assignment ---------------- */
    @GetMapping("/submit")
    public String submitAssignmentForm(Model model) {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "submit-assignment";
    }

    @PostMapping("/submit")
    public String submitAssignment(@RequestParam Long assignmentId,
                                   @RequestParam Long studentId,
                                   @RequestParam MultipartFile file,
                                   Model model) throws IOException {
        assignmentService.submitAssignment(assignmentId, studentId, file);
        model.addAttribute("message", "Assignment submitted successfully!");
        return "submit-assignment";
    }

    /** ---------------- Faculty: View & Grade Submissions ---------------- */
    @GetMapping("/{assignmentId}/submissions")
    public String viewSubmissions(@PathVariable Long assignmentId, Model model) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        model.addAttribute("submissions", submissions);
        model.addAttribute("assignmentId", assignmentId);
        return "grade-submissions";
    }

    @PostMapping("/{assignmentId}/grade")
    public String gradeSubmission(@RequestParam Long submissionId,
                                  @RequestParam String grade,
                                  @PathVariable Long assignmentId) {
        assignmentService.gradeSubmission(submissionId, grade);  // save grade
        return "redirect:/faculty/assignments/" + assignmentId + "/submissions";
    }

 // Show the form to enter student ID
    @GetMapping("/student/grades")
    public String showGradeForm() {
        return "view-grades"; // displays form
    }

    // Handle form submission
    @PostMapping("/student/grades")
    public String viewGradesById(@RequestParam Long studentId, Model model) {
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(studentId);

        if (submissions.isEmpty()) {
            model.addAttribute("message", "No grades found for Student ID: " + studentId);
        } else {
            model.addAttribute("submissions", submissions);
        }

        model.addAttribute("studentId", studentId); // optional, for showing in view
        return "view-grades";
    }
}
