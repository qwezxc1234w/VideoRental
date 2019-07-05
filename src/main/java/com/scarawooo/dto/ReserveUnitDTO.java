package com.scarawooo.dto;

import java.io.Serializable;
import java.util.Date;

public class ReserveUnitDTO implements Serializable {
    private int id;
    private ClientDTO client;
    private WarehouseUnitDTO warehouseUnit;
    private int amount;
    private Date beginDate;
    private Date endDate;

    public ReserveUnitDTO(WarehouseUnitDTO warehouseUnit, int amount, Date beginDate, Date endDate) {
        this.amount = amount;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.warehouseUnit = warehouseUnit;
    }

    public ReserveUnitDTO(int id, ClientDTO client, WarehouseUnitDTO warehouseUnit, int amount, Date beginDate, Date endDate) {
        this.id = id;
        this.amount = amount;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.warehouseUnit = warehouseUnit;
        this.client = client;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public WarehouseUnitDTO getWarehouseUnit() {
        return warehouseUnit;
    }

    @Override
    public String toString() {
        return warehouseUnit.getFilm().getName();
    }

    public String getInfo() {
        return "Наименование: " + warehouseUnit.getFilm().getName() + "\n" +
                "Автор: " + warehouseUnit.getFilm().getAuthor().getName() + "\n" +
                "Жанр: " + warehouseUnit.getFilm().getGenre().getName() + "\n" +
                "Количество: " + amount + "\n" +
                "Сумма: " + amount * warehouseUnit.getFilm().getPrice() + "\n" +
                "Дата выдачи: " + beginDate + "\n" +
                "Дата возврата: " + endDate;
    }
}
