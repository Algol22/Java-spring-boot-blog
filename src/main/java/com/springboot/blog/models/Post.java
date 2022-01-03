package com.springboot.blog.models;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.tomcat.jni.Address;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set <Comment> comments;

    private String title, anons, full_text, tag;
    private boolean isfavourite;
    private int numberofcomments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thedate")
    @DateTimeFormat
    Date thedate;

    private int views;

    public Post(Long id, String title){
        this.title = title;
        this.id =id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnons() {
        return anons;
    }

    public void setAnons(String anons) {
        this.anons = anons;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Date getThedate() {
        return thedate;
    }

    public void setThedate(Date thedate) {
        this.thedate = thedate;
    }

    public Post() {
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Post(String title, String anons, String full_text, String tag, Date thedate,boolean isfavourite, int numberofcomments) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.thedate = thedate;
        this.tag = tag;
        this.isfavourite =isfavourite;
        this.numberofcomments = numberofcomments;
    }

    public boolean getIsfavourite() {
        return isfavourite;
    }

    public void setIsfavourite(boolean isfavourite) {
        isfavourite = isfavourite;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", comments=" + comments +
                ", title='" + title + '\'' +
                ", anons='" + anons + '\'' +
                ", full_text='" + full_text + '\'' +
                ", tag='" + tag + '\'' +
                ", thedate=" + thedate +
                ", views=" + views +
                '}';
    }

    public int getNumberofcomments() {
        return numberofcomments;
    }

    public void setNumberofcomments(int numberofcomments) {
        this.numberofcomments = numberofcomments;
    }
}
