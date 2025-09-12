package com.univ.attendograde.service;

import com.univ.attendograde.entity.Student;
import com.univ.attendograde.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Add a new student
    public Student addStudent(String name, String rollNo, String email,
                              String department, Integer year,
                              String phone, String bloodGroup) {
        Student s = new Student();
        s.setName(name);
        s.setRollNo(rollNo);
        s.setEmail(email);
        s.setDepartment(department);
        s.setYear(year);
        s.setPhone(phone);
        s.setBloodGroup(bloodGroup);
        return studentRepository.save(s);
    }

    // Update subject marks
    @Transactional
    public boolean updateSubjectMarks(Long studentId, String subject, Integer marks) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getSubjectMarks() == null) {
                student.setSubjectMarks(new HashMap<>());
            }
            student.getSubjectMarks().put(subject, marks);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    // Update attendance
    @Transactional
    public boolean updateAttendance(Long studentId, Integer attendedClasses, Integer totalClasses) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setAttendedClasses(attendedClasses);
            student.setTotalClasses(totalClasses);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

 // Update assignment marks
    @Transactional
    public boolean updateAssignmentMarks(Long studentId, String assignmentName, Integer marks) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getAssignmentMarks() == null) {
                student.setAssignmentMarks(new HashMap<>());
            }
            student.getAssignmentMarks().put(assignmentName, marks);
            studentRepository.save(student);
            return true;
        }
        return false;
    }


    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get a student by ID
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    // Delete a student
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
