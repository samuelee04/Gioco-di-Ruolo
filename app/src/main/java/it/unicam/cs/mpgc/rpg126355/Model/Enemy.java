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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int atk;

    @Column(nullable = false)
    private int def;

    protected Enemy() {}

    public Enemy(String name, int hp, int atk, int def){
        if(name == null || name.isBlank() || hp < 0 || atk < 0 || def < 0){
            throw new IllegalArgumentException("Ci sono errori nel costruttore");
        }
        this.name = name;
        this.hp = hp;
        this.atk = atk;
        this.def = def;
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

    public static Enemy generateRandomEnemy() {
        java.util.Random rand = new java.util.Random();

        String[] tipi = {"Orco", "Goblin", "Scheletro", "Bandito", "Ragno Gigante", "Lupo Mannaro", "Demone Minore", "Golem", "Vampiro", "Cultista", "Spettro"};
        String[] aggettivi = {"Feroce", "Corrotto", "Sanguinario", "Putrido", "Oscuro", "Maledetto", "Velenoso", "Corazzato", "Zoppicante", "Famelico"};

        String randomName = tipi[rand.nextInt(tipi.length)] + " " + aggettivi[rand.nextInt(aggettivi.length)];

        int hp = rand.nextInt(30) + 15;
        int atk = rand.nextInt(10) + 5;
        int def = rand.nextInt(6) + 2;

        return new Enemy(randomName, hp, atk, def);
    }
}
