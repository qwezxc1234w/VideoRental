package com.scarawooo.dao;

import com.scarawooo.converters.ReserveUnitConverter;
import com.scarawooo.dto.ClientDTO;
import com.scarawooo.dto.ReserveUnitDTO;
import com.scarawooo.entity.ReserveUnitEntity;
import com.scarawooo.hibernate.SessionProvider;
import org.hibernate.Session;
import java.util.List;
import java.util.stream.Collectors;

public class ReserveUnitDAO {
    public static List<ReserveUnitDTO> getClientReserves(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("FROM ReserveUnitEntity WHERE client.login = :login", ReserveUnitEntity.class)
                    .setParameter("login", clientDTO.getLogin()).getResultList().stream()
                    .map(reserveUnitEntity -> ReserveUnitConverter.convert(reserveUnitEntity))
                    .collect(Collectors.toList());
        }
    }

    public static void insert(ReserveUnitDTO reserveUnitDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.save(ReserveUnitConverter.convert(reserveUnitDTO));
            session.getTransaction().commit();
        }
    }

    public static void delete(ReserveUnitDTO reserveUnitDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.delete(ReserveUnitConverter.convert(reserveUnitDTO));
            session.getTransaction().commit();
        }
    }
}
