package com.univ.attendograde.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_marks")
public class StudentMarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer subject1marks;
    private Integer subject2marks;
    private Integer subject3marks;

    public StudentMarks() {}

    public StudentMarks(Integer subject1marks, Integer subject2marks, Integer subject3marks) {
        this.subject1marks = subject1marks;
        this.subject2marks = subject2marks;
        this.subject3marks = subject3marks;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public Integer getSubject1marks() {
        return subject1marks;
    }

    public void setSubject1marks(Integer subject1marks) {
        this.subject1marks = subject1marks;
    }

    public Integer getSubject2marks() {
        return subject2marks;
    }

    public void setSubject2marks(Integer subject2marks) {
        this.subject2marks = subject2marks;
    }

    public Integer getSubject3marks() {
        return subject3marks;
    }

    public void setSubject3marks(Integer subject3marks) {
        this.subject3marks = subject3marks;
    }
}
