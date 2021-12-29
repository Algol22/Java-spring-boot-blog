package com.springboot.blog.repo;

import com.springboot.blog.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT u FROM Post u WHERE u.tag = LOWER(:tag)")
    List<Post> retrieveByTag(@Param("tag") String tag);


    @Query("SELECT u FROM Post u ORDER BY u.thedate ASC")
    List<Post> sortByAscDate();

    @Query("SELECT u FROM Post u ORDER BY u.thedate DESC")
    List<Post> sortByDescDate();

    @Query(value = "select p.id, p.title from post p where p.id in (select distinct (p.post_id) as p from favourites p where p.user_id=:id )", nativeQuery = true)
    public List <Post> findByPostId(@Param("id") Long id);
}
