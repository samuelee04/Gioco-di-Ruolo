package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Boss implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

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

    protected Boss() {};

    public Boss(String name, int hp, int atk, int def,int level){
        if(name == null || name.isBlank() || hp < 0 || atk < 0 || def < 0 || level < 0){
            throw new IllegalArgumentException("Ci sono errori nel costruttore");
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

    public void setId(Long id) {
        this.id = id;
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

        String[] nomi = {"Azazel", "Gorgoroth", "Malekith", "Voldor", "Kael'thas", "Nyarlathotep", "Balthazar", "Sauron", "Morok"};
        String[] titoli = {
                "Il Distruttore", "Il Senzanome", "Signore dell'Abisso", "Re dei Lich",
                "Flagello degli Dei", "L'Ombra Strisciante", "Il Divoratore di Anime",
                "Il Titano Caduto", "L'Araldo della Rovina"
        };

        String randomName = nomi[rand.nextInt(nomi.length)] + ", " + titoli[rand.nextInt(titoli.length)];

        int hp = rand.nextInt(100) + 150; // HP tra 150 e 249
        int atk = rand.nextInt(20) + 15;  // ATK tra 15 e 34
        int def = rand.nextInt(15) + 10;  // DEF tra 10 e 24
        int level = rand.nextInt(10) + 20; // Richiede livello/EXP alto per essere affrontato!

        return new Boss(randomName, hp, atk, def, level);
    }
}
