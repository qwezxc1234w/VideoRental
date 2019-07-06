package com.scarawooo.dao.implementations;

import com.scarawooo.converters.ReserveUnitConverter;
import com.scarawooo.dao.ReserveUnitDAO;
import com.scarawooo.dto.ClientDTO;
import com.scarawooo.dto.ReserveUnitDTO;
import com.scarawooo.entity.ReserveUnitEntity;
import com.scarawooo.hibernate.SessionProvider;
import org.hibernate.Session;
import java.util.List;
import java.util.stream.Collectors;

public class ReserveUnitDAOImpl implements ReserveUnitDAO {
    @Override
    public List<ReserveUnitDTO> getClientReserves(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("FROM ReserveUnitEntity WHERE client.login = :login", ReserveUnitEntity.class)
                    .setParameter("login", clientDTO.getLogin()).getResultList().stream()
                    .map(reserveUnitEntity -> ReserveUnitConverter.convert(reserveUnitEntity))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void insert(ReserveUnitDTO reserveUnitDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.save(ReserveUnitConverter.convert(reserveUnitDTO));
            session.getTransaction().commit();
        }
    }

    public void delete(ReserveUnitDTO reserveUnitDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.delete(ReserveUnitConverter.convert(reserveUnitDTO));
            session.getTransaction().commit();
        }
    }
}
