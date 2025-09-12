package com.univ.attendograde.controller;

import com.univ.attendograde.entity.Student;
import com.univ.attendograde.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Optional;

@Controller
public class StudentPageController {

    private final StudentService studentService;

    public StudentPageController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/view-attendance")
    public String viewAttendance(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Optional<Student> student = studentService.getStudentById(id);
            if (student.isPresent()) {
                model.addAttribute("student", student.get());
            } else {
                model.addAttribute("student", null);
                model.addAttribute("errorMessage", "Student not found");
            }
        }
        return "view-attendance";  // loads view-attendance.html
    }

    @GetMapping("/view-reportcard")
    public String viewReportCard(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Optional<Student> studentOpt = studentService.getStudentById(id);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                Map<String, Integer> marks = student.getSubjectMarks(); // map of subject->marks
                int total = marks.values().stream().mapToInt(Integer::intValue).sum();
                int maxTotal = marks.size() * 100; // assuming each subject out of 100
                double percentage = total * 100.0 / maxTotal;
                
                // Compute grade
                String grade;
                if (percentage >= 90) grade = "A+";
                else if (percentage >= 80) grade = "A";
                else if (percentage >= 70) grade = "B";
                else if (percentage >= 60) grade = "C";
                else grade = "D";

                model.addAttribute("studentName", student.getName());
                model.addAttribute("marks", marks);
                model.addAttribute("total", total);
                model.addAttribute("maxTotal", maxTotal);
                model.addAttribute("percentage", String.format("%.1f", percentage));
                model.addAttribute("grade", grade);
                model.addAttribute("attendance", String.format("%.1f", (student.getAttendedClasses() * 100.0 / student.getTotalClasses())));
            } else {
                model.addAttribute("errorMessage", "Student not found!");
            }
        }
        return "view-reportcard";
    }


    @GetMapping("/student-back")
    public String backToMainMenu() {
        return "student";  // goes back to main menu
    }
}
