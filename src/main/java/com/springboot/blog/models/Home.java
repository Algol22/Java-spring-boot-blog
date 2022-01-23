package com.springboot.blog.models;



import javax.persistence.*;

@Entity
public class Home {

    @Id
    private Long id;

    @Column(length=10485760)
    private String home;
    private String photourl;

    public Home(){

    }

    public Home(Long id,String home, String photourl) {
        this.id=id;
        this.home = home;
        this.photourl = photourl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
