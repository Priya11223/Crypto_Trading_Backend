package com.email_test.EmailCheck.Repository;

import com.email_test.EmailCheck.Modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}