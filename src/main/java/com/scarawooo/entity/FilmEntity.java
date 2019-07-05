package com.scarawooo.entity;

import javax.persistence.*;

@Entity
@Table (name = "films")
public class FilmEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private int id;

    @Column(name = "film_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @Column(name = "price")
    private double price;

    @OneToOne(mappedBy = "film")
    private WarehouseUnitEntity unit;

    public FilmEntity() {}

    public FilmEntity(int id, String name, AuthorEntity author, GenreEntity genre, double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public GenreEntity getGenre() {
        return genre;
    }
}
