package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Boss implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_HP    = 150, HP_RANGE    = 100;
    private static final int MIN_ATK   = 15,  ATK_RANGE   = 20;
    private static final int MIN_DEF   = 10,  DEF_RANGE   = 15;
    private static final int MIN_LEVEL = 20,  LEVEL_RANGE = 10;

    private static final String[] NOMI   = {"Azazel", "Gorgoroth", "Malekith", "Voldor", "Kael'thas", "Nyarlathotep", "Balthazar", "Sauron", "Morok"};
    private static final String[] TITOLI = {"Il Distruttore", "Il Senzanome", "Signore dell'Abisso", "Re dei Lich", "Flagello degli Dei", "L'Ombra Strisciante", "Il Divoratore di Anime", "Il Titano Caduto", "L'Araldo della Rovina"};

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

    @Column(nullable = false)
    private int level;

    protected Boss() {}

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

    public static Boss generateRandomBoss() {
        java.util.Random rand = new java.util.Random();

        String randomName = NOMI[rand.nextInt(NOMI.length)] + ", " + TITOLI[rand.nextInt(TITOLI.length)];

        int hp    = rand.nextInt(HP_RANGE)    + MIN_HP;
        int atk   = rand.nextInt(ATK_RANGE)   + MIN_ATK;
        int def   = rand.nextInt(DEF_RANGE)   + MIN_DEF;
        int level = rand.nextInt(LEVEL_RANGE) + MIN_LEVEL;

        return new Boss(randomName, hp, atk, def, level);
    }

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
