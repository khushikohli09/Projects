package com.univ.attendograde2v.auth.repository;

import com.univ.attendograde2v.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    
    // Fetch all usernames with role STUDENT
    @Query("SELECT u.username FROM User u WHERE u.role = 'STUDENT'")
    List<String> findAllStudentUsernames();
}
