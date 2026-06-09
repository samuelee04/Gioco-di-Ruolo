package it.unicam.cs.mpgc.rpg126355.Controller;

import java.util.List;

/**
 * Interfaccia generica che definisce il contratto formale per le operazioni CRUD
 * (Create, Read, Update, Delete) sui dati del gioco.
 * Qualsiasi componente responsabile della persistenza delle entità del Model deve
 * implementare questa interfaccia per garantire l'uniformità e l'intercambiabilità
 * dei meccanismi di salvataggio.
 *
 * @param <T> Il tipo di entità gestita dal controller.
 */
public interface Controller<T> {

    /**
     * Inserisce e memorizza un nuovo elemento all'interno del sistema di persistenza.
     *
     * @param element L'oggetto di tipo {@code T} da aggiungere.
     */
    void add(T element);

    /**
     * Rimuove un elemento dal sistema di persistenza cercandolo tramite il suo
     * identificativo univoco.
     *
     * @param id L'ID dell'elemento da eliminare.
     */
    void remove(Long id);

    /**
     * Aggiorna lo stato di un'entità già esistente e registrata nel sistema di persistenza.
     *
     * @param entity L'oggetto contenente i dati modificati da sovrascrivere.
     */
    void update(T entity);

    /**
     * Recupera l'elenco completo di tutti gli elementi di tipo {@code T} attualmente
     * memorizzati nel sistema.
     *
     * @return Una {@link List} contenente tutte le entità registrate.
     */
    List<T> getAll();
}
