package com.univ.attendograde2v.faculty.controller;

import com.univ.attendograde2v.student.entity.StudentAttendance;
import com.univ.attendograde2v.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Get logged-in username from session
        String username = (String) session.getAttribute("username");

        // Pass username to Thymeleaf template
        model.addAttribute("username", username);

        return "faculty-dashboard"; // Thymeleaf template
    }

    // Mark attendance form
    @GetMapping("/attendance")
    public String attendanceForm(Model model) {
        LocalDate today = LocalDate.now();
        model.addAttribute("today", today);
        model.addAttribute("attendanceList", studentService.getAttendanceByDate(today));
        return "faculty-attendance";
    }

    // Submit attendance
    @PostMapping("/attendance")
    public String submitAttendance(@RequestParam String studentUsername,
                                   @RequestParam(defaultValue = "false") boolean present,
                                   @RequestParam String today,
                                   Model model) {
        LocalDate date = LocalDate.parse(today);

        // Save attendance
        studentService.markAttendance(studentUsername, present, date);

        // Reload updated list
        List<StudentAttendance> list = studentService.getAttendanceByDate(date);
        model.addAttribute("attendanceList", list);
        model.addAttribute("today", today);

        return "faculty-attendance";
    }

    // View attendance by date (optional)
    @GetMapping("/attendance/view")
    public String viewAttendance(@RequestParam String date, Model model) {
        LocalDate selectedDate = LocalDate.parse(date);
        List<StudentAttendance> list = studentService.getAttendanceByDate(selectedDate);
        model.addAttribute("attendanceList", list);
        model.addAttribute("today", selectedDate);
        return "faculty-attendance";
    }
}
