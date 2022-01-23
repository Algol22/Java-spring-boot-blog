package com.springboot.blog.models;

import javax.persistence.*;

@Entity
public class About {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=10485760)
    private String about;
    private String photourl;

    public About(Long id, String about, String photourl) {
        this.id = id;
        this.about = about;
        this.photourl = photourl;
    }

    public About(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
