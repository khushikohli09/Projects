package com.univ.attendograde.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Student name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Roll number cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Roll number must be alphanumeric")
    private String rollNo;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Department cannot be blank")
    private String department;

    @NotNull(message = "Year is required")
    @Min(value = 1, message = "Year must be at least 1")
    @Max(value = 4, message = "Year cannot be greater than 4")
    private Integer year;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Blood group cannot be blank")
    @Pattern(
        regexp = "^(A|B|AB|O)[+-]$",
        message = "Invalid blood group format (e.g., A+, O-, AB+)"
    )
    private String bloodGroup;

    @Min(value = 0, message = "Total classes cannot be negative")
    private Integer totalClasses = 0;

    @Min(value = 0, message = "Attended classes cannot be negative")
    private Integer attendedClasses = 0;

    // Subject-wise marks
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "student_subject_marks",
        joinColumns = @JoinColumn(name = "student_id")
    )
    @MapKeyColumn(name = "subject")
    @Column(name = "marks")
    private Map<String, @Min(value = 0) @Max(value = 100) Integer> subjectMarks = new HashMap<>();

    // Assignment-wise marks
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "student_assignment_marks",
        joinColumns = @JoinColumn(name = "student_id")
    )
    @MapKeyColumn(name = "assignment_name")
    @Column(name = "marks")
    private Map<String, @Min(value = 0) @Max(value = 100) Integer> assignmentMarks = new HashMap<>();

    // Attendance %
    public double getAttendancePercentage() {
        if (totalClasses == null || totalClasses == 0) return 0.0;
        return (attendedClasses * 100.0) / totalClasses;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public Integer getTotalClasses() { return totalClasses; }
    public void setTotalClasses(Integer totalClasses) { this.totalClasses = totalClasses; }

    public Integer getAttendedClasses() { return attendedClasses; }
    public void setAttendedClasses(Integer attendedClasses) { this.attendedClasses = attendedClasses; }

    public Map<String, Integer> getSubjectMarks() { return subjectMarks; }
    public void setSubjectMarks(Map<String, Integer> subjectMarks) { this.subjectMarks = subjectMarks; }

    public Map<String, Integer> getAssignmentMarks() { return assignmentMarks; }
    public void setAssignmentMarks(Map<String, Integer> assignmentMarks) { this.assignmentMarks = assignmentMarks; }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", year=" + year +
                ", phone='" + phone + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", totalClasses=" + totalClasses +
                ", attendedClasses=" + attendedClasses +
                ", subjectMarks=" + subjectMarks +
                ", assignmentMarks=" + assignmentMarks +
                '}';
    }
}
