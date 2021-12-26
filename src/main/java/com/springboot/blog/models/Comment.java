//package com.springboot.blog.models;
//
//import javax.persistence.*;
//
//@Entity
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private String author;
//    private String comment;
//    private String dated;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    Post post;
//
//    public Comment(){
//    }
//
//    public Comment(String comment, String dated) {
//        this.dated = dated;
//        this.comment = comment;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getDated() {
//        return dated;
//    }
//
//    public void setDated(String dated) {
//        this.dated = dated;
//    }
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//}
