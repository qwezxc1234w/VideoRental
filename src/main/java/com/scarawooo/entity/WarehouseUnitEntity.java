package com.scarawooo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "warehouse")
public class WarehouseUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "film_id")
    private FilmEntity film;

    @Column(name = "amount")
    private int amount;

    @OneToMany(mappedBy = "warehouseUnit")
    private Set<ReserveUnitEntity> reserves;

    public WarehouseUnitEntity() {}

    public WarehouseUnitEntity(int id, FilmEntity film, int amount) {
        this.id = id;
        this.film = film;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public FilmEntity getFilm() {
        return film;
    }
}
