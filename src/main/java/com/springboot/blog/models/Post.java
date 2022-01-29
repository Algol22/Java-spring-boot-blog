package com.springboot.blog.models;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_generator")
    @SequenceGenerator(name="post_generator", sequenceName = "post_seq", allocationSize=50)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @JsonManagedReference
    @Column(columnDefinition = "LONGTEXT")
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set <Comment> comments;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set <Favourites> favourites;


    private String title;

    @Column(length=10485760)
    private String anons;

    @Column(length=10485760)
    private String full_text;

    private String tag;

    private int numberofcomments;
    private String photoUrl;

    @Column(nullable = true, length = 64)
    private String photos;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "thedate")
    @DateTimeFormat
    Date thedate;

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

    public Post(String title, String anons, String full_text, String tag, Date thedate,int numberofcomments, String photos, String photoUrl) {
        this.title = title;
        this.anons = anons;
        this.full_text = full_text;
        this.thedate = thedate;
        this.tag = tag;
        this.numberofcomments = numberofcomments;
        this.photos = photos;
        this.photoUrl = photoUrl;
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
                '}';
    }

    public int getNumberofcomments() {
        return numberofcomments;
    }

    public void setNumberofcomments(int numberofcomments) {
        this.numberofcomments = numberofcomments;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
