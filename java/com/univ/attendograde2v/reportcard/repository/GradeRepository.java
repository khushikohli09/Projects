package com.univ.attendograde2v.reportcard.repository;

import com.univ.attendograde2v.reportcard.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentUsername(String studentUsername);
}
