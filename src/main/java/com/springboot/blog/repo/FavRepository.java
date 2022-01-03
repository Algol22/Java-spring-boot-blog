package com.springboot.blog.repo;

import com.springboot.blog.domain.User;
import com.springboot.blog.models.Comment;
import com.springboot.blog.models.Favourites;
import com.springboot.blog.models.Post;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface FavRepository extends CrudRepository<Favourites, Long> {


    @Query(value = "select count (distinct u.post_id) from favourites u where u.user_id= :id", nativeQuery = true)
    public List<Long> countFavourites(@Param("id") Long id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from favourites where post_id= :id", nativeQuery = true)
    public void deleteFav(@Param("id") Long id);

    @Transactional
    @Query(value = "select post_id from favourites where user_id= :id", nativeQuery = true)
    public List <Long> findFavourites(@Param("id") Long id);
}
