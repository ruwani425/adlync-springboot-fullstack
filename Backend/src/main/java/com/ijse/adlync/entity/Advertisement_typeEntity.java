package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.Advertisement_typeEntityTypeEnum;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Advertisement_typeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long ad_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Advertisement_typeEntityTypeEnum type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @OneToMany(mappedBy = "advertisement_type", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

    public Long getAd_id() {
        return ad_id;
    }

    public void setAd_id(Long ad_id) {
        this.ad_id = ad_id;
    }

    public Advertisement_typeEntityTypeEnum getType() {
        return type;
    }

    public void setType(Advertisement_typeEntityTypeEnum type) {
        this.type = type;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

    public List<PostEntity> getPosts() {
        return posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    public void addPost(PostEntity post) {
        this.posts.add(post);
    }

    public void removePost(PostEntity post) {
        this.posts.remove(post);
    }

}
