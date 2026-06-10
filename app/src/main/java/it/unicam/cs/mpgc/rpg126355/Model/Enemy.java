package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rappresenta l'entità dei nemici standard all'interno del gioco.
 * Questa classe è configurata come entità JPA per essere mappata direttamente sul database locale.
 * Gestisce lo stato delle statistiche dei mostri durante i combattimenti nell'Arena e
 * incapsula gli algoritmi per la generazione procedurale dei vari tipi di avversari.
 */
@Entity
public class Enemy implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Costanti per i range di bilanciamento delle statistiche del nemico
    private static final int MIN_HP   = 15, HP_RANGE  = 30;
    private static final int MIN_ATK  = 5,  ATK_RANGE = 10;
    private static final int MIN_DEF  = 2,  DEF_RANGE = 6;

    // Pool di componenti stringa per la generazione procedurale del nome
    private static final String[] TIPI = {"Orco", "Goblin", "Scheletro", "Bandito", "Ragno Gigante", "Lupo Mannaro", "Demone Minore", "Golem", "Vampiro", "Cultista", "Spettro", "Ghoul", "Arpia", "Melma", "Imps", "Basilisco", "Necromante", "Gargoyle"};
    private static final String[] AGGETTIVI = {"Feroce", "Corrotto", "Sanguinario", "Putrido", "Oscuro", "Maledetto", "Velenoso", "Corazzato", "Zoppicante", "Famelico", "Spettrale", "Infetto", "Furtivo", "Immortale", "Colossale", "Errante"};

    /**
     * Identificatore univoco del nemico nel database (Primary Key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int atk;

    @Column(nullable = false)
    private int def;

    /**
     * Costruttore protetto privo di parametri richiesto dalle specifiche JPA/Hibernate
     * per istanziare l'oggetto durante il recupero dei dati dal database.
     */
    protected Enemy() {}


    /**
     * Costruisce un nuovo nemico eseguendo i controlli di validità sui parametri inseriti.
     *
     * @param name Il nome del nemico.
     * @param hp   I Punti Vita iniziali (non devono essere negativi).
     * @param atk  Il valore di attacco base (non deve essere negativo).
     * @param def  Il valore di difesa base (non deve essere negativo).
     * @throws IllegalArgumentException se il nome risulta vuoto o se una statistica è negativa.
     */
    public Enemy(String name, int hp, int atk, int def){
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere nullo o vuoto");
        }
        if (hp < 0) {
            throw new IllegalArgumentException("Gli HP non possono essere negativi");
        }
        if (atk < 0) {
            throw new IllegalArgumentException("L'attacco non può essere negativo");
        }
        if (def < 0) {
            throw new IllegalArgumentException("La difesa non può essere negativa");
        }
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
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

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    /**
     * Genera in modo procedurale un'istanza di un nemico combinando casualmente una tipologia
     * di mostro e un aggettivo dal pool di stringhe di classe, calcolandone i parametri di
     * combattimento in base alle costanti numeriche prefissate.
     *
     * @return Un'istanza di {@link Enemy} inizializzata e pronta all'uso.
     */
    public static Enemy generateRandomEnemy() {
        java.util.Random rand = new java.util.Random();

        String randomName = TIPI[rand.nextInt(TIPI.length)] + " " + AGGETTIVI[rand.nextInt(AGGETTIVI.length)];

        int hp  = rand.nextInt(HP_RANGE)  + MIN_HP;
        int atk = rand.nextInt(ATK_RANGE) + MIN_ATK;
        int def = rand.nextInt(DEF_RANGE) + MIN_DEF;

        return new Enemy(randomName, hp, atk, def);
    }

    /**
     * Valuta l'uguaglianza tra questo nemico e un altro oggetto confrontando le rispettive
     * chiavi primarie (ID) generate dal database.
     *
     * @param o L'oggetto con cui effettuare il confronto.
     * @return true se gli ID sono uguali, false in caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enemy e)) return false;
        return id != null && id.equals(e.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Enemy{id=" + id + ", name='" + name + "', hp=" + hp + ", atk=" + atk + ", def=" + def + "}";
    }
}
