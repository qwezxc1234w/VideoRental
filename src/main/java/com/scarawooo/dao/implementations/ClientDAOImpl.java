package com.scarawooo.dao.implementations;

import com.scarawooo.converters.ClientConverter;
import com.scarawooo.dao.ClientDAO;
import com.scarawooo.dto.ClientDTO;
import com.scarawooo.entity.ClientEntity;
import com.scarawooo.hibernate.SessionProvider;
import org.hibernate.Session;

public class ClientDAOImpl implements ClientDAO {
    @Override
    public boolean isLoginOccupy(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("SELECT id FROM ClientEntity WHERE login = :login")
                    .setParameter("login", clientDTO.getLogin()).uniqueResult() == null;
        }
    }

    @Override
    public void insert(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.save(ClientConverter.convert(clientDTO));
            session.getTransaction().commit();
        }
    }

    @Override
    public ClientDTO getByLoginAndPass(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            ClientEntity clientEntity = session.createQuery("SELECT new ClientEntity(id, name, surname, login, pass) FROM ClientEntity WHERE login = :login AND pass = :pass", ClientEntity.class)
                    .setParameter("login", clientDTO.getLogin())
                    .setParameter("pass", clientDTO.getPass()).uniqueResult();
            return clientEntity == null ? null : ClientConverter.convert(clientEntity);
        }
    }
}
