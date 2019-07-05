package com.scarawooo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "genres")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private int id;

    @Column(name = "genre_name")
    private String name;

    @OneToMany(mappedBy = "genre")
    private Set<FilmEntity> film;

    public GenreEntity() {}

    public GenreEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
