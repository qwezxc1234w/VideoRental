package com.scarawooo.hibernate;

import com.scarawooo.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionProvider {
    private static SessionFactory sessionFactory;

    public static Session getSession() {
        synchronized (SessionProvider.class) {
            if (sessionFactory == null) {
                try {
                    Configuration configuration = new Configuration()
                            .addAnnotatedClass(AuthorEntity.class)
                            .addAnnotatedClass(ClientEntity.class)
                            .addAnnotatedClass(FilmEntity.class)
                            .addAnnotatedClass(GenreEntity.class)
                            .addAnnotatedClass(ReserveUnitEntity.class)
                            .addAnnotatedClass(WarehouseUnitEntity.class)
                            .configure();
                    ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
                    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return sessionFactory.openSession();
    }
}