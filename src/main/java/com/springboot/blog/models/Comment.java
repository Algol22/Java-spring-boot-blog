package com.springboot.blog.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.springboot.blog.domain.User;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long commentId;
    private String text;
    private String userdb;
    @DateTimeFormat
    private Date commentDate;




    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Post post;

    public Comment() {
    }

    public Comment(String text, Post post, String userdb, Date commentDate) {
        this.post= post;
        this.text = text;
        this.userdb =userdb;
        this.commentDate=commentDate;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", text='" + text + '\'' +
                ", post=" + post +
                '}';
    }

    public Post getPost() {
        return post;
    }
    public Long getId() {
        return commentId;
    }



    public void setPost(Post post) {
        this.post = post;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setId(Long commentId) {
        this.commentId = commentId;
    }

    public String getUserdb() {
        return userdb;
    }

    public void setUserdb(String userdb) {
        this.userdb = userdb;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}