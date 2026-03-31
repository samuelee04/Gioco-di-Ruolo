package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Enemy implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
