package com.univ.attendograde2v.student.service;

import com.univ.attendograde2v.student.entity.StudentAttendance;
import com.univ.attendograde2v.student.repository.StudentAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentAttendanceRepository attendanceRepository;

    // Student: fetch own attendance
    public List<StudentAttendance> getAttendanceByUsername(String username) {
        return attendanceRepository.findByStudentUsername(username);
    }
    public List<StudentAttendance> getAttendanceByStudent(String username) {
        return attendanceRepository.findByStudentUsername(username);
    }

    // Faculty: mark attendance
    public void markAttendance(String studentUsername, boolean present, LocalDate date) {
        StudentAttendance attendance = new StudentAttendance();
        attendance.setStudentUsername(studentUsername);
        attendance.setPresent(present);
        attendance.setDate(date);

        attendanceRepository.save(attendance);
    }
    
 // Calculate attendance percentage for a student
    public double getAttendancePercentage(String username) {
        List<StudentAttendance> attendanceList = attendanceRepository.findByStudentUsername(username);

        if (attendanceList.isEmpty()) return 0.0;

        long presentCount = attendanceList.stream()
                .filter(StudentAttendance::isPresent)
                .count();

        return (presentCount * 100.0) / attendanceList.size();
    }


    // Faculty: fetch all attendance for a date
    public List<StudentAttendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }
}
