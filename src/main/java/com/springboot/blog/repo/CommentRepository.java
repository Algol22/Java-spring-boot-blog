package com.springboot.blog.repo;

import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {


    @Transactional
    @Query(value="SELECT count(comment_id) FROM comment where id=:id", nativeQuery = true)
    int countComments(@Param("id") Long id);

}
