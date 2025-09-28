package com.univ.attendograde2v.student.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_attendance")
public class StudentAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentUsername; // Student's username

    private boolean present; // true if present

    private LocalDate date; // Date of attendance

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
