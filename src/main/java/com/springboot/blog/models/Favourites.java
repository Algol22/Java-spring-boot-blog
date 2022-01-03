package com.springboot.blog.models;

import javax.persistence.*;

@Entity
public class Favourites {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    public Favourites(Long userId, Post post) {
        this.userId = userId;
        this.post = post;
    }

    public Long getUserId() {
        return userId;
    }

    public Favourites(){

    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
