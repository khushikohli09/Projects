package com.univ.attendograde.controller;

import com.univ.attendograde.entity.Student;
import com.univ.attendograde.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Add a new student
    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestParam String name,
                                              @RequestParam String rollNo,
                                              @RequestParam String email,
                                              @RequestParam String department,
                                              @RequestParam Integer year,
                                              @RequestParam String phone,
                                              @RequestParam String bloodGroup) {
        Student student = studentService.addStudent(
                name, rollNo, email, department, year, phone, bloodGroup
        );
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    // Update subject marks
    @PutMapping("/{id}/marks")
    public ResponseEntity<String> updateSubjectMarks(@PathVariable Long id,
                                                     @RequestParam String subject,
                                                     @RequestParam Integer marks) {
        try {
            studentService.updateSubjectMarks(id, subject, marks);
            return ResponseEntity.ok("Subject marks updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Update attendance
    @PutMapping("/{id}/attendance")
    public ResponseEntity<String> updateAttendance(@PathVariable Long id,
                                                   @RequestParam Integer attended,
                                                   @RequestParam Integer total) {
        try {
            studentService.updateAttendance(id, attended, total);
            return ResponseEntity.ok("Attendance updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get all students
    @GetMapping("/all")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get report card (subject-wise marks)
    @GetMapping("/{id}/report")
    public ResponseEntity<Map<String, Integer>> getReportCard(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(s -> ResponseEntity.ok(s.getSubjectMarks()))
                      .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            return ResponseEntity.ok("Student deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found");
        }
    }
}
