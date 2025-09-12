package com.univ.attendograde.controller;

import com.univ.attendograde.entity.Student;
import com.univ.attendograde.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/faculty")
public class FacultyPageController {

    private final StudentService studentService;

    @Autowired
    public FacultyPageController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Faculty Menu
    @GetMapping("/menu")
    public String facultyMenu() {
        return "faculty"; // faculty.html
    }

    // Add Student Page
    @GetMapping("/addstudent")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "addstudent"; // addstudent.html
    }

    @PostMapping("/addstudent")
    public String addStudentSubmit(@ModelAttribute Student student) {
        studentService.addStudent(
                student.getName(),
                student.getRollNo(),
                student.getEmail(),
                student.getDepartment(),
                student.getYear(),
                student.getPhone(),
                student.getBloodGroup()
        );
        return "redirect:/faculty/menu";
    }

    // Update Marks Page
    @GetMapping("/update-marks")
    public String updateMarksForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "update-marks";
    }

    @PostMapping("/update-marks")
    public String updateMarksSubmit(@RequestParam Long studentId,
                                    @RequestParam String subject,
                                    @RequestParam Integer marks) {
        studentService.updateSubjectMarks(studentId, subject, marks);
        return "redirect:/faculty/menu";
    }

    // Update Attendance Page
    @GetMapping("/update-attendance")
    public String updateAttendanceForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "update-attendance";
    }

    @PostMapping("/update-attendance")
    public String updateAttendanceSubmit(@RequestParam Long studentId,
                                         @RequestParam Integer attended,
                                         @RequestParam Integer total) {
        studentService.updateAttendance(studentId, attended, total);
        return "redirect:/faculty/menu";
    }

    // Update Assignment Marks Page
    @GetMapping("/update-assignment")
    public String updateAssignmentForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "update-assignment"; // update-assignment.html
    }

    @PostMapping("/update-assignment")
    public String updateAssignmentSubmit(@RequestParam Long studentId,
                                         @RequestParam String assignmentName,
                                         @RequestParam Integer marks) {
        studentService.updateAssignmentMarks(studentId, assignmentName, marks);
        return "redirect:/faculty/menu";
    }

    // View All Students
    @GetMapping("/view-all")
    public String viewAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "view-all";
    }

    // Delete Student
    @GetMapping("/delete-student")
    public String deleteStudentForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "delete-student";
    }

    @PostMapping("/delete-student")
    public String deleteStudentSubmit(@RequestParam Long studentId) {
        studentService.deleteStudent(studentId);
        return "redirect:/faculty/menu";
    }
}
