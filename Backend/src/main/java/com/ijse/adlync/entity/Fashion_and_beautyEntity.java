package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.Fashion_and_beautyEntityGenderEnum;
import jakarta.persistence.*;

@Entity
public class Fashion_and_beautyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long fashion_id;

    private String item_type;

    private String brand;

    private String size;

    @Enumerated(EnumType.STRING)
    private Fashion_and_beautyEntityGenderEnum gender;

    private String condition;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public Long getFashion_id() {
        return fashion_id;
    }

    public void setFashion_id(Long fashion_id) {
        this.fashion_id = fashion_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Fashion_and_beautyEntityGenderEnum getGender() {
        return gender;
    }

    public void setGender(Fashion_and_beautyEntityGenderEnum gender) {
        this.gender = gender;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}
