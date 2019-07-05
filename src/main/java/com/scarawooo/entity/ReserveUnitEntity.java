package com.scarawooo.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reserves")
public class ReserveUnitEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reserve_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private WarehouseUnitEntity warehouseUnit;

    @Column(name = "amount")
    private int amount;

    @Column(name = "begin_date")
    private Date beginDate;

    @Column(name = "end_date")
    private Date endDate;

    public ReserveUnitEntity() {}

    public ReserveUnitEntity(int id, ClientEntity client, WarehouseUnitEntity warehouseUnit, int amount, Date beginDate, Date endDate) {
        this.id = id;
        this.client = client;
        this.warehouseUnit = warehouseUnit;
        this.amount = amount;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public ClientEntity getClient() {
        return client;
    }

    public WarehouseUnitEntity getWarehouseUnit() {
        return warehouseUnit;
    }
}
