package com.example.Note.category;

import javax.persistence.*;

@Entity
@Table(name="categories")
public class Category {

    @Id
    @Column(name="category_name")
    private String 	category_name ;

    /*@Column(name="description")
    private String 	description ;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }*/

    public String getCategoryName() {
        return category_name;
    }

    public void setCategoryName(String name) {
        this.category_name = name;
    }
}
