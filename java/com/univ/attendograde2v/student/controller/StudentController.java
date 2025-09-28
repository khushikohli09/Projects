package com.univ.attendograde2v.student.controller;

import com.univ.attendograde2v.student.entity.StudentAttendance;
import com.univ.attendograde2v.student.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Student dashboard
    @GetMapping("/student/home")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "student-dashboard";
    }

    // View logged-in student's attendance
    @GetMapping("/student/attendance")
    public String viewAttendance(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        if (username != null) {
            List<StudentAttendance> attendanceList = studentService.getAttendanceByUsername(username);
            double percentage = studentService.getAttendancePercentage(username);

            model.addAttribute("attendanceList", attendanceList);
            model.addAttribute("attendancePercentage", percentage);
        } else {
            model.addAttribute("attendanceList", List.of());
            model.addAttribute("attendancePercentage", 0.0);
        }

        return "student-attendance"; // Thymeleaf template
    }
}
