package com.counseling.studentapp.repository;

import com.counseling.studentapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email); // For login later
}
