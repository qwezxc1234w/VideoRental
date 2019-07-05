package com.scarawooo.dao;

import com.scarawooo.converters.WarehouseUnitConverter;
import com.scarawooo.dto.WarehouseUnitDTO;
import com.scarawooo.entity.WarehouseUnitEntity;
import com.scarawooo.hibernate.SessionProvider;
import org.hibernate.Session;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseUnitDAO {
    public static WarehouseUnitDTO getById(int id) {
        try (Session session = SessionProvider.getSession()) {
            return WarehouseUnitConverter.convert(session.createQuery("SELECT new WarehouseUnitEntity(wue.id, wue.film, wue.amount) FROM WarehouseUnitEntity AS wue WHERE wue.id = :id", WarehouseUnitEntity.class)
                    .setParameter("id", id)
                    .uniqueResult());
        }
    }

    public static List<WarehouseUnitDTO> getAll() {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("FROM WarehouseUnitEntity", WarehouseUnitEntity.class).getResultList().stream()
                    .map(warehouseUnitEntity -> WarehouseUnitConverter.convert(warehouseUnitEntity))
                    .collect(Collectors.toList());
        }
    }

    public static void update(WarehouseUnitDTO warehouseUnitDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.update(WarehouseUnitConverter.convert(warehouseUnitDTO));
            session.getTransaction().commit();
        }
    }
}
