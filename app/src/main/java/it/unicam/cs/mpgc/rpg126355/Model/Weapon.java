package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rappresenta l'entità Arma all'interno del gioco.
 * Questa classe è mappata come entità JPA per consentire la persistenza dei dati sul database locale.
 * Le armi forniscono bonus statistici di attacco (power) agli Eroi che le equipaggiano,
 * a patto che l'Eroe soddisfi il requisito di esperienza/livello richiesto dall'arma stessa.
 */
@Entity
public class Weapon implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // Costanti per il bilanciamento delle statistiche dell'arma
    private static final int MIN_POWER   = 5,  POWER_RANGE = 20;
    private static final int LEVEL_RANGE = 21;

    // Pool di componenti stringa per la generazione procedurale del nome dell'equipaggiamento
    private static final String[] TIPI = {"Spada", "Ascia", "Martello", "Daga", "Lancia", "Alabarda", "Mazza Chiodata", "Bastone Magico", "Spadone", "Arco Lungo", "Katana", "Scimitarra", "Falcione", "Stocco", "Lama Celata", "Mazza da Guerra"};
    private static final String[] SUFFISSI = {"delle Ceneri", "dei Re Caduti", "dell'Abisso", "delle Stelle", "dell'Eclissi", "del Drago", "dell'Ira", "dell'Anima", "del Vuoto", "dimenticata", "sanguinaria", "infranta", "del Giudizio", "della Luna Oscura", "dell'Agonia", "d'Ebano", "Suprema", "d'Argento", "della Tempesta", "del Sangue Corrotto"};

    /**
     * Identificatore univoco dell'arma nel database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Il potere offensivo dell'arma. Rappresenta il bonus di attacco
     * sommato alle statistiche base dell'Eroe durante un combattimento.
     */
    @Column(nullable = false)
    private int power;

    /**
     * Il livello/EXP minimo richiesto affinché un Eroe possa equipaggiare l'arma.
     */
    @Column(nullable = false)
    private int level;

    /**
     * Costruttore protetto senza parametri richiesto dalle specifiche JPA/Hibernate
     * per la ricostruzione dell'oggetto durante le letture dal database.
     */
    protected Weapon() {}


    /**
     * Costruisce una nuova arma effettuando i relativi controlli di validazione sui parametri.
     *
     * @param name  Il nome dell'arma.
     * @param power Il potere offensivo (non deve essere negativo).
     * @param level Il livello minimo richiesto per l'equipaggiamento (non deve essere negativo).
     * @throws IllegalArgumentException se il nome è vuoto o se i valori numerici sono negativi.
     */
    public Weapon(String name, int power, int level){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere nullo o vuoto");
        }
        if (power < 0){
            throw new IllegalArgumentException("Il potere non può essere negativo");
        }
        if (level < 0) {
            throw new IllegalArgumentException("Il livello non può essere negativo");
        }

        this.name = name;
        this.power = power;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Genera in modo procedurale un'arma combinando casualmente una tipologia e un suffisso
     * tratti dai relativi pool di stringhe, assegnando un potere e un livello richiesto
     * calcolati entro i range definiti dalle costanti di classe.
     *
     * @return Un'istanza di {@link Weapon} configurata proceduralmente.
     */
    public static Weapon generateRandomWeapon() {
        java.util.Random rand = new java.util.Random();

        String randomName  = TIPI[rand.nextInt(TIPI.length)] + " " + SUFFISSI[rand.nextInt(SUFFISSI.length)];

        int randomPower = rand.nextInt(POWER_RANGE) + MIN_POWER;
        int randomLevel = rand.nextInt(LEVEL_RANGE);

        return new Weapon(randomName, randomPower, randomLevel);
    }

    /**
     * Verifica l'uguaglianza tra questa arma e un altro oggetto confrontando le rispettive
     * chiavi primarie (ID) generate dal database.
     *
     * @param o L'oggetto con cui effettuare il confronto.
     * @return true se gli ID corrispondono, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weapon w)) return false;
        return id != null && id.equals(w.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Weapon{id=" + id + ", name='" + name + "', power=" + power + ", level=" + level + "}";
    }
}