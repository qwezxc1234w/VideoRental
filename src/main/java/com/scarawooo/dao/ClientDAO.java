package com.scarawooo.dao;

import com.scarawooo.converters.ClientConverter;
import com.scarawooo.dto.ClientDTO;
import com.scarawooo.entity.ClientEntity;
import com.scarawooo.hibernate.SessionProvider;
import org.hibernate.Session;

public class ClientDAO {
    public static boolean isLoginOccupy(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("SELECT id FROM ClientEntity WHERE login = :login")
                    .setParameter("login", clientDTO.getLogin()).uniqueResult() == null;
        }
    }

    public static boolean isLoginPassPairValid(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return session.createQuery("SELECT id FROM ClientEntity WHERE login = :login AND pass = :pass")
                    .setParameter("login", clientDTO.getLogin())
                    .setParameter("pass", clientDTO.getPass()).uniqueResult() != null;
        }
    }

    public static void insert(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            session.beginTransaction();
            session.save(ClientConverter.convert(clientDTO));
            session.getTransaction().commit();
        }
    }

    public static ClientDTO getByLoginAndPass(ClientDTO clientDTO) {
        try (Session session = SessionProvider.getSession()) {
            return ClientConverter.convert(session.createQuery("SELECT new ClientEntity(id, name, surname, login, pass) FROM ClientEntity WHERE login = :login AND pass = :pass", ClientEntity.class)
                    .setParameter("login", clientDTO.getLogin())
                    .setParameter("pass", clientDTO.getPass()).uniqueResult());
        }
    }
}
