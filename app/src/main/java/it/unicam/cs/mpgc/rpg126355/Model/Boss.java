package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rappresenta l'entità Boss (antagonista finale) all'interno del gioco.
 * Questa classe è mappata come entità JPA per consentire la persistenza dei dati sul database.
 * Include le statistiche di combattimento specifiche per le Boss Fight e la logica per la
 * generazione procedurale dell'antagonista.
 */
@Entity
public class Boss implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Costanti per il bilanciamento delle statistiche
    private static final int MIN_HP    = 150, HP_RANGE    = 100;
    private static final int MIN_ATK   = 15,  ATK_RANGE   = 20;
    private static final int MIN_DEF   = 10,  DEF_RANGE   = 15;
    private static final int MIN_LEVEL = 20,  LEVEL_RANGE = 10;

    // Pool di nomi e titoli per la generazione procedurale
    private static final String[] NOMI = {"Azazel", "Gorgoroth", "Malekith", "Voldor", "Kael'thas", "Nyarlathotep", "Balthazar", "Sauron", "Morok", "Xalthor", "Morgath", "Zul'Gurub", "Tenebris", "Arkanis", "Vaelastrasz", "Ragnarok", "Kalthazar", "Belial"};
    private static final String[] TITOLI = {"Il Distruttore", "Il Senzanome", "Signore dell'Abisso", "Re dei Lich", "Flagello degli Dei", "L'Ombra Strisciante", "Il Divoratore di Anime", "Il Titano Caduto", "L'Araldo della Rovina", "Il Mietitore di Sangue", "Il Sussurro del Vuoto", "Flagello di Fuoco", "L'Eterno Condannato", "Signore delle Ceneri", "L'Ombra del Destino"};

    /**
     * Identificatore univoco del Boss nel database (Primary Key).
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
     * Livello minimo richiesto o livello di forza del Boss.
     * Determina se un Eroe è abbastanza qualificato per accedere alla Boss Fight.
     */
    @Column(nullable = false)
    private int level;

    /**
     * Costruttore protetto richiesto da JPA/Hibernate per l'inizializzazione dell'entità
     * durante il recupero dal database. Non deve essere usato per la creazione manuale.
     */
    protected Boss() {}


    /**
     * Costruisce un nuovo Boss validando i parametri forniti in input.
     *
     * @param name  Il nome completo del Boss.
     * @param hp    I Punti Vita (non devono essere negativi).
     * @param atk   Il valore di attacco (non deve essere negativo).
     * @param def   Il valore di difesa (non deve essere negativo).
     * @param level Il livello del Boss (non deve essere negativo).
     * @throws IllegalArgumentException se uno dei parametri viola i vincoli di validazione.
     */
    public Boss(String name, int hp, int atk, int def,int level){
        if (name == null || name.isBlank()){
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
        if (level < 0) {
            throw new IllegalArgumentException("Il livello non può essere negativo");
        }

        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Genera proceduralmente un'istanza di Boss combinando casualmente un nome e un titolo
     * tratti dai pool predefiniti, calcolando le statistiche entro i range stabiliti.
     *
     * @return Un'istanza di {@link Boss} pronta per essere salvata e sfidata.
     */
    public static Boss generateRandomBoss() {
        java.util.Random rand = new java.util.Random();

        String randomName = NOMI[rand.nextInt(NOMI.length)] + ", " + TITOLI[rand.nextInt(TITOLI.length)];

        int hp    = rand.nextInt(HP_RANGE)    + MIN_HP;
        int atk   = rand.nextInt(ATK_RANGE)   + MIN_ATK;
        int def   = rand.nextInt(DEF_RANGE)   + MIN_DEF;
        int level = rand.nextInt(LEVEL_RANGE) + MIN_LEVEL;

        return new Boss(randomName, hp, atk, def, level);
    }

    /**
     * Confronta questo Boss con un altro oggetto basandosi sull'identificatore univoco (ID).
     *
     * @param o Oggetto da confrontare.
     * @return true se gli ID corrispondono, false altrimenti.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Boss b)) return false;
        return id != null && id.equals(b.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Boss{id=" + id + ", name='" + name + "', hp=" + hp + ", atk=" + atk + ", def=" + def + ", level=" + level + "}";
    }
}
