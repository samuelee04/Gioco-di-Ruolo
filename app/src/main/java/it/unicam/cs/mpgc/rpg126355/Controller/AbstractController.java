package it.unicam.cs.mpgc.rpg126355.Controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public abstract class AbstractController<T> implements Controller<T> {


    protected static SessionFactory sessionFactory;


    private final Class<T> entityClass;


    protected AbstractController(Class<T> entityClass) {
        this.entityClass = entityClass;
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
    }


    @Override
    public void add(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }
    }
}
