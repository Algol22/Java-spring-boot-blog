package com.springboot.blog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.blog.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}