package com.example.Note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="categories")
public class Category{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Category(Long uid, String category_name) {
        this.uid = uid;
        this.category_name = category_name;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category() {
    }

    public Category(Long id, Long uid, String category_name) {
        this.id = id;
        this.uid = uid;
        this.category_name = category_name;
    }

    @Column(name="uid")
    private Long uid;

    @Column(name="category_name")
    private String category_name;


    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String name) {
        this.category_name=name;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
