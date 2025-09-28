package com.univ.attendograde2v.assignment.controller;

import com.univ.attendograde2v.assignment.entity.Assignment;
import com.univ.attendograde2v.assignment.entity.Submission;
import com.univ.attendograde2v.assignment.service.AssignmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // ------------------- FACULTY -------------------

    // Show form to add a new assignment
    @GetMapping("/faculty/add")
    public String addAssignmentForm() {
        return "faculty-assignment-add"; // matches the template name
    }

    // Handle form submission for adding assignment
    @PostMapping("/faculty/add")
    public String addAssignment(@RequestParam String title,
                                @RequestParam String subject,
                                @RequestParam String description,
                                @RequestParam String dueDate) {
        assignmentService.addAssignment(title, subject, description, dueDate);
        return "redirect:/assignment/faculty/list";
    }

    // List all assignments (faculty view)
    @GetMapping("/faculty/list")
    public String facultyAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        model.addAttribute("assignments", assignments);
        return "faculty-assignment-list"; // matches template
    }

    // View all submissions for a particular assignment
    @GetMapping("/faculty/submissions/{assignmentId}")
    public String viewSubmissions(@PathVariable Long assignmentId, Model model) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        model.addAttribute("submissions", submissions);
        return "faculty-submission-list"; // matches template
    }

    // Grade a submission
    @PostMapping("/faculty/grade/{submissionId}")
    public String gradeSubmission(@PathVariable Long submissionId,
                                  @RequestParam String grade) {
        assignmentService.gradeSubmission(submissionId, grade);
        return "redirect:/assignment/faculty/list";
    }

    // ------------------- STUDENT -------------------

    // List all assignments for students
    @GetMapping("/student/list")
    public String studentAssignments(Model model) {
        List<Assignment> assignments = assignmentService.getAllAssignments();
        model.addAttribute("assignments", assignments);
        return "student-assignment-list"; // matches template
    }

    // Submit an assignment
    @PostMapping("/student/submit/{assignmentId}")
    public String submitAssignment(@PathVariable Long assignmentId,
                                   @RequestParam("file") MultipartFile file,
                                   HttpSession session,
                                   Model model) {
        String username = (String) session.getAttribute("username");

        try {
            assignmentService.submitAssignment(assignmentId, username, file);
        } catch (IOException e) {
            model.addAttribute("error", "File upload failed: " + e.getMessage());
            return "student-assignment-list";
        }

        return "redirect:/assignment/student/list";
    }

    // View student's own submissions
    @GetMapping("/student/my-submissions")
    public String mySubmissions(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        List<Submission> submissions = assignmentService.getSubmissionsByStudent(username);
        model.addAttribute("submissions", submissions);
        return "student-my-submissions"; // matches template
    }
}
