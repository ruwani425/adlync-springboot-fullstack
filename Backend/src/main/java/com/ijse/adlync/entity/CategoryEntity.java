package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.CategoryEntityNameEnum;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long category_id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoryEntityNameEnum name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostEntity> posts = new ArrayList<>();

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public CategoryEntityNameEnum getName() {
        return name;
    }

    public void setName(CategoryEntityNameEnum name) {
        this.name = name;
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
