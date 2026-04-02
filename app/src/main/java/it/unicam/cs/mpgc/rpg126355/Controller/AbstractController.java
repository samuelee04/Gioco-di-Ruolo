package it.unicam.cs.mpgc.rpg126355.Controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


public abstract class AbstractController<T> implements Controller<T> {


    protected static SessionFactory sessionFactory;

    @Override
    public void add(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }
    }
}
