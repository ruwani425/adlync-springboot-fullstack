package com.ijse.adlync.entity;

import com.ijse.adlync.entity.enums.AnimalEntityGenderEnum;
import jakarta.persistence.*;

@Entity
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long animal_id;

    private String species;

    private String breed;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private AnimalEntityGenderEnum gender;

    private String vaccination_status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public Long getAnimal_id() {
        return animal_id;
    }

    public void setAnimal_id(Long animal_id) {
        this.animal_id = animal_id;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public AnimalEntityGenderEnum getGender() {
        return gender;
    }

    public void setGender(AnimalEntityGenderEnum gender) {
        this.gender = gender;
    }

    public String getVaccination_status() {
        return vaccination_status;
    }

    public void setVaccination_status(String vaccination_status) {
        this.vaccination_status = vaccination_status;
    }

    public PostEntity getPost() {
        return post;
    }

    public void setPost(PostEntity post) {
        this.post = post;
    }

}
