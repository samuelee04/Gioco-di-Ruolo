package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

@Entity
public class Weapon implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int MIN_POWER   = 5,  POWER_RANGE = 20;
    private static final int LEVEL_RANGE = 21;

    private static final String[] TIPI     = {"Spada", "Ascia", "Martello", "Daga", "Lancia", "Alabarda", "Mazza Chiodata", "Bastone Magico", "Spadone", "Arco Lungo"};
    private static final String[] SUFFISSI = {"delle Ceneri", "dei Re Caduti", "dell'Abisso", "delle Stelle", "dell'Eclissi", "del Drago", "dell'Ira", "dell'Anima", "del Vuoto", "dimenticata", "sanguinaria", "infranta", "del Giudizio"};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int power;

    //Alcune armi potranno essere usate solo al raggiungimento di un certo livello
    @Column(nullable = false)
    private int level;

    protected Weapon() {}

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

    public static Weapon generateRandomWeapon() {
        java.util.Random rand = new java.util.Random();

        String randomName  = TIPI[rand.nextInt(TIPI.length)] + " " + SUFFISSI[rand.nextInt(SUFFISSI.length)];

        int randomPower = rand.nextInt(POWER_RANGE) + MIN_POWER;
        int randomLevel = rand.nextInt(LEVEL_RANGE);

        return new Weapon(randomName, randomPower, randomLevel);
    }

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