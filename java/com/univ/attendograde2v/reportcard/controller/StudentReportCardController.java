package com.univ.attendograde2v.reportcard.controller;

import com.univ.attendograde2v.reportcard.entity.Grade;
import com.univ.attendograde2v.reportcard.service.ReportCardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.List;

@Controller
public class StudentReportCardController {

    private final ReportCardService reportCardService;

    public StudentReportCardController(ReportCardService reportCardService) {
        this.reportCardService = reportCardService;
    }

    @GetMapping("/student/report-card")
    public String viewReportCard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");

        // Fetch grades
        List<Grade> grades = reportCardService.getGradesByStudent(username);

        // Calculate overall stats
        double percentage = reportCardService.calculatePercentage(grades);
        double gpa = reportCardService.calculateGPA(grades);

        // Identify top and weak subjects
        Grade topSubject = grades.stream()
                .max(Comparator.comparingDouble(Grade::getMarksObtained))
                .orElse(null);
        Grade weakSubject = grades.stream()
                .min(Comparator.comparingDouble(Grade::getMarksObtained))
                .orElse(null);

        // Pass to view
        model.addAttribute("studentUsername", username);
        model.addAttribute("grades", grades);
        model.addAttribute("percentage", percentage);
        model.addAttribute("gpa", gpa);
        model.addAttribute("topSubject", topSubject);
        model.addAttribute("weakSubject", weakSubject);

        return "student-report-card"; // Thymeleaf template
    }
}
