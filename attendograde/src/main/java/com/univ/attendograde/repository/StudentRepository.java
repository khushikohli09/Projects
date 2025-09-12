package com.univ.attendograde.repository;

import com.univ.attendograde.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
