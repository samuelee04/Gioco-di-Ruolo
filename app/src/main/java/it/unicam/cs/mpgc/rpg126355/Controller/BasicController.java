package it.unicam.cs.mpgc.rpg126355.Controller;

/**
 * Implementazione concreta e istanziabile del controller generico.
 * Questa classe estende {@link AbstractController} e ha la responsabilità di fornire
 * un punto di accesso pratico per gestire la persistenza di una specifica entità
 * del Model (es. Hero, Enemy, Weapon), ereditando tutti i metodi CRUD
 * senza la necessità di duplicare il codice.
 *
 * @param <T> Il tipo dell'entità del Model che questa istanza del controller andrà a gestire.
 */
public class BasicController<T> extends AbstractController<T>{

    /**
     * Costruisce un'istanza del controller per l'entità specificata.
     * Invia il riferimento della classe al costruttore della superclasse
     * {@link AbstractController} per configurare la sessione di Hibernate e
     * identificare la tabella di destinazione.
     *
     * @param entityClass Il token di classe (es. Hero.class, Weapon.class) dell'entità da gestire.
     */
    public BasicController(Class<T> entityClass) {
        super(entityClass);
    }
}
