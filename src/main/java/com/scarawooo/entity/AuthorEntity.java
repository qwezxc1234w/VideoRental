package com.scarawooo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private int id;

    @Column(name = "author_name")
    private String name;

    @OneToMany(mappedBy = "author")
    private Set<FilmEntity> filmEntity;

    public AuthorEntity() {}

    public AuthorEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
