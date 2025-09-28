package com.univ.attendograde2v.faculty.repository;

import com.univ.attendograde2v.faculty.entity.FacultyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FacultyAttendanceRepository extends JpaRepository<FacultyAttendance, Long> {
    List<FacultyAttendance> findByDate(LocalDate date);
}
