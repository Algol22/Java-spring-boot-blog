package com.springboot.blog.repo;

import com.springboot.blog.models.About;
import com.springboot.blog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface AboutRepo extends CrudRepository<About, Long> {
}
