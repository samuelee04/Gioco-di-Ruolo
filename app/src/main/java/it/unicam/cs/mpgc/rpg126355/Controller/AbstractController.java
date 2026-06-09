package it.unicam.cs.mpgc.rpg126355.Controller;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import java.util.List;

/**
 * Classe astratta che implementa l'interfaccia {@link Controller} per fornire
 * le operazioni CRUD (Create, Read, Update, Delete) di base.
 * Utilizza il framework Hibernate per gestire la persistenza dei dati sul database.
 *
 * @param <T> Il tipo dell'entità del Model (es. Hero, Enemy, Weapon) gestita dal controller.
 */
public abstract class AbstractController<T> implements Controller<T> {

    protected static SessionFactory sessionFactory;

    private final Class<T> entityClass;

    /**
     * Costruttore base per il controller.
     * Inizializza il riferimento alla classe dell'entità e, se non è ancora stata creata,
     * costruisce la {@link SessionFactory} leggendo le configurazioni di Hibernate.
     *
     * @param entityClass La classe (Model) che questo controller dovrà gestire.
     */
    protected AbstractController(Class<T> entityClass) {
        this.entityClass = entityClass;
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
    }

    /**
     * Salva una nuova entità all'interno del database.
     * L'operazione viene eseguita all'interno di una transazione per garantire
     * l'integrità dei dati.
     *
     * @param entity L'oggetto da salvare nel database.
     */
    @Override
    public void add(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(entity);
            tx.commit();
        }
    }

    /**
     * Rimuove definitivamente un'entità dal database ricercandola tramite il suo ID.
     * Utilizzato per gestire la meccanica di Permadeath o l'eliminazione manuale.
     *
     * @param id L'identificatore univoco (Primary Key) dell'entità da rimuovere.
     */
    @Override
    public void remove(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            T entity = session.find(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
            tx.commit();
        }
    }

    /**
     * Aggiorna i dati di un'entità già esistente nel database.
     * Esegue un'operazione di merge (fusione) tra lo stato attuale dell'oggetto
     * e quello salvato nel DB.
     *
     * @param entity L'entità contenente le informazioni aggiornate.
     */
    @Override
    public void update(T entity) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
        }
    }

    /**
     * Recupera tutte le entità di un determinato tipo memorizzate nel database.
     * Esegue una query generica per prelevare tutti i record della tabella associata.
     *
     * @return Una lista contenente tutte le istanze trovate.
     */
    @Override
    public List<T> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from " + entityClass.getSimpleName(), entityClass).list();
        }
    }
}
