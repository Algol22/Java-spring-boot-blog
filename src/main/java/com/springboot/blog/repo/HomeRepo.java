package com.springboot.blog.repo;

import com.springboot.blog.models.Home;
import com.springboot.blog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface HomeRepo extends CrudRepository<Home, Long> {
}
