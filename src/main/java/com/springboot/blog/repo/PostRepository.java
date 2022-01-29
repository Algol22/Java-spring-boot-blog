package com.springboot.blog.repo;

import com.springboot.blog.models.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {


    Page <Post> findAll(Pageable page);


    @Query("SELECT u FROM Post u ORDER BY u.thedate DESC")
    List<Post> findAllPosts();


    @Query("SELECT u FROM Post u WHERE u.tag = LOWER(:tag)")
    Page <Post>  retrieveByTag(@Param("tag") String tag, Pageable page );


    @Query("SELECT u FROM Post u ORDER BY u.thedate ASC")
    Page <Post> sortByAscDate(Pageable page);

    @Query("SELECT u FROM Post u ORDER BY u.thedate DESC")
    Page <Post> sortByDescDate(Pageable page);

    @Query(value = "select p.id, p.title from post p where p.id in (select distinct (p.post_id) as p from favourites p where p.user_id=:id )", nativeQuery = true)
    public List <Post> findByPostId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value ="update post set anons=:anons, full_text=:full_text, tag=:tag, thedate=:thedate, title=:title where id =:id", nativeQuery = true)
    public void updateById(@Param("id") Long id, @Param("anons") String anons, @Param("full_text") String full_text, @Param("tag") String tag, @Param("thedate") Date thedate, @Param("title") String title);


    @Modifying
    @Transactional
    @Query(value ="update post set numberofcomments=:numberofcomments where id=:id", nativeQuery = true)
    public void updateComments(@Param("id") Long id, @Param("numberofcomments") int numberofcomments);

    @Modifying
    @Transactional
    @Query(value ="update post set photos=:photo where id=:id", nativeQuery = true)
    public void updatePhoto(@Param("id") Long id, @Param("photo") String photo);

    @Modifying
    @Transactional
    @Query(value ="update post set photos=null where id=:id", nativeQuery = true)
    public void deletePhoto(@Param("id") Long id);

    @Transactional
    @Query(value ="select photos from post where id=:id", nativeQuery = true)
    public String findPhotoById(@Param("id") Long id);

}
