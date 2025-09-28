package com.univ.attendograde2v.student.repository;

import com.univ.attendograde2v.student.entity.StudentAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Long> {

    // For student view
    List<StudentAttendance> findByStudentUsername(String username);

    // Optional: For faculty view to check today's attendance
    List<StudentAttendance> findByDate(LocalDate date);
}
