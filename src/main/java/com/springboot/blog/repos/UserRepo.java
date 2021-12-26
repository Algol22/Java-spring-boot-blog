package com.springboot.blog.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.blog.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}