package com.univ.attendograde2v.faculty.service;

import com.univ.attendograde2v.faculty.entity.FacultyAttendance;
import com.univ.attendograde2v.faculty.repository.FacultyAttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FacultyService {

    @Autowired
    private FacultyAttendanceRepository facultyAttendanceRepository;

    public List<FacultyAttendance> getAttendanceByDate(LocalDate date) {
        return facultyAttendanceRepository.findByDate(date);
    }

    public void markAttendance(String studentUsername, LocalDate date, boolean present) {
        FacultyAttendance attendance = new FacultyAttendance();
        attendance.setStudentUsername(studentUsername);
        attendance.setDate(date);
        attendance.setPresent(present);

        facultyAttendanceRepository.save(attendance);
    }
}
