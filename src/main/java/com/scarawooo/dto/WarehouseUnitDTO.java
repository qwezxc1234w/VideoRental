package com.scarawooo.dto;

import java.io.Serializable;

public class WarehouseUnitDTO implements Serializable {
    private int id;
    private FilmDTO film;
    private int amount;

    public WarehouseUnitDTO(int id, FilmDTO film, int amount) {
        this.id = id;
        this.amount = amount;
        this.film = film;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof WarehouseUnitDTO && ((WarehouseUnitDTO) obj).id == this.id;
    }

    public int getAmount() {
        return amount;
    }

    public FilmDTO getFilm() {
        return film;
    }

    public int getId() {
        return id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return film.getName();
    }
}
