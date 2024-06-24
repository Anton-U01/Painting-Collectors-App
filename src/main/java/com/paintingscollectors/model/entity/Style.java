package com.paintingscollectors.model.entity;


import javax.persistence.*;

@Entity
@Table(name = "styles")
public class Style extends BaseEntity{
    @Enumerated(EnumType.STRING)
    @Column(unique = true,nullable = false)
    private StyleEnum name;
    @Column(nullable = false)
    private String description;

    public Style() {
    }

    public Style(StyleEnum name, String description) {
        this.name = name;
        this.description = description;
    }

    public StyleEnum getName() {
        return name;
    }

    public void setName(StyleEnum name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
