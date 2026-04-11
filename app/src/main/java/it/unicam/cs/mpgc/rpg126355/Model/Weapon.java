package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Weapon implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int power;

    @Column(nullable = false)
    private int level;

    protected Weapon() {}

    public Weapon(String name, int power, int level){
        if(name == null || name.isBlank() || power < 0 || level < 0){
            throw new IllegalArgumentException("Ci sono errori nel costruttore");
        }
        this.name = name;
        this.power = power;
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

    public static Weapon generateRandomWeapon() {
        java.util.Random rand = new java.util.Random();

        String[] tipi = {"Spada", "Ascia", "Martello", "Daga", "Lancia", "Alabarda", "Mazza Chiodata", "Bastone Magico", "Spadone", "Arco Lungo"};
        String[] suffissi = {
                "delle Ceneri", "dei Re Caduti", "dell'Abisso", "delle Stelle",
                "dell'Eclissi", "del Drago", "dell'Ira", "dell'Anima", "del Vuoto",
                "dimenticata", "sanguinaria", "infranta", "del Giudizio"
        };

        String randomName = tipi[rand.nextInt(tipi.length)] + " " + suffissi[rand.nextInt(suffissi.length)];

        int randomPower = rand.nextInt(20) + 5;
        int randomLevel = rand.nextInt(21);

        return new Weapon(randomName, randomPower, randomLevel);
    }
}