package it.unicam.cs.mpgc.rpg126355.Controller;

import java.util.List;

public interface Controller<T> {

    void add(T element);

    void remove(Long id);

    void update(T entity);

    List<T> getAll();
}
