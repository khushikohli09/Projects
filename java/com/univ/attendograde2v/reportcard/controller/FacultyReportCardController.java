package com.univ.attendograde2v.reportcard.controller;

import com.univ.attendograde2v.reportcard.entity.Grade;
import com.univ.attendograde2v.reportcard.service.ReportCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/faculty/report-cards")
public class FacultyReportCardController {

    private final ReportCardService reportCardService;

    public FacultyReportCardController(ReportCardService reportCardService) {
        this.reportCardService = reportCardService;
    }

    // Show empty form
    @GetMapping("/add")
    public String showEnterMarksForm(Model model) {
        List<String> studentUsernames = reportCardService.getAllStudentUsernames();
        model.addAttribute("students", studentUsernames);
        model.addAttribute("selectedStudent", null);
        model.addAttribute("grades", new ArrayList<>()); // empty list for new entry
        return "faculty-enter-marks";
    }

    // Save marks submitted by faculty (multiple subjects)
    @PostMapping("/save")
    public String saveMarks(
            @RequestParam String studentUsername,
            @RequestParam Map<String, String> allRequestParams,
            Model model
    ) {
        if (!reportCardService.isStudentPresent(studentUsername)) {
            model.addAttribute("error", "Student username not found!");
            model.addAttribute("students", reportCardService.getAllStudentUsernames());
            model.addAttribute("grades", new ArrayList<>());
            return "faculty-enter-marks";
        }

        List<ReportCardService.GradeInput> subjects = new ArrayList<>();
        int index = 0;
        while (allRequestParams.containsKey("subjects[" + index + "].subject")) {
            String subject = allRequestParams.get("subjects[" + index + "].subject");
            int marksObtained = Integer.parseInt(allRequestParams.get("subjects[" + index + "].marksObtained"));
            int maxMarks = Integer.parseInt(allRequestParams.get("subjects[" + index + "].maxMarks"));

            subjects.add(new ReportCardService.GradeInput(subject, marksObtained, maxMarks));
            index++;
        }

        reportCardService.saveGrades(studentUsername, subjects);

        // ✅ Redirect properly without trailing `/q`
        return "redirect:/faculty/report-cards/view?studentUsername=" + studentUsername;
    }

    // View -> open the add marks form with student pre-selected + existing grades
    @GetMapping("/view")
    public String viewReportCard(@RequestParam String studentUsername, Model model) {
        List<String> studentUsernames = reportCardService.getAllStudentUsernames();
        List<Grade> grades = reportCardService.getGradesByStudent(studentUsername);

        model.addAttribute("students", studentUsernames);
        model.addAttribute("selectedStudent", studentUsername);
        model.addAttribute("grades", grades);

        return "faculty-enter-marks"; // reuse add marks page
    }
}