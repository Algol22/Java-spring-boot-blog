package com.springboot.blog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.springboot.blog.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "SELECT u.id FROM usr u WHERE u.username = LOWER(:name)", nativeQuery = true)
    public List<User> findIdByName(@Param("name") String name);
}