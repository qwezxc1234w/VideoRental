package com.scarawooo.dto;

import java.io.Serializable;

public class FilmDTO implements Serializable {
    private int id;
    private String name;
    private AuthorDTO author;
    private GenreDTO genre;
    private double price;

    public FilmDTO(int id, String name, AuthorDTO author, GenreDTO genre, double price) {
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

    public AuthorDTO getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public GenreDTO getGenre() {
        return genre;
    }
}
