package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Random;

@Entity
public class Hero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int INITIAL_EXP = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int hp;

    @Column(nullable = false)
    private int atk;

    @Column(nullable = false)
    private int def;

    @Column(nullable = false)
    private int exp;

    @ManyToOne // Un eroe può avere un'arma. Molti eroi possono avere la stessa arma
    @JoinColumn(name = "weapon_id")
    private Weapon equippedWeapon;

    protected Hero() {}

    public Hero(String name) {
        if(name == null || name.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere nullo o vuoto");
        }
        this.name = name;
        this.exp  = INITIAL_EXP;

        Random random = new Random();

        this.hp = random.nextInt(21) + 40;
        this.atk = random.nextInt(5) + 4;
        this.def = random.nextInt(4) + 3;
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

    public int getExp() {
        return exp;
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hero t)) {
            return false;
        }
        return id != null && id.equals(t.id);
    }

    @Override
    public String toString() {
        return "Hero{id=" + id + ", name='" + name + "', hp=" + hp + ", atk=" + atk + ", def=" + def + ", exp=" + exp + "}";
    }


    public boolean canEquip(Weapon weapon) {
        return this.exp >= weapon.getLevel();
    }

    public boolean canFight(Enemy enemy) {
        return this.exp >= enemy.getAtk() && this.exp >= enemy.getDef();
    }

    public boolean canFight(Boss boss) {
        return this.exp >= boss.getLevel();
    }

    public void equipWeapon(Weapon newWeapon) {
        if (canEquip(newWeapon)) {
            this.equippedWeapon = newWeapon;
        } else {
            throw new IllegalArgumentException("Livello troppo alto per questa arma!");
        }
    }

    public String fight(Enemy enemy) {
        if (!canFight(enemy)) {
            throw new IllegalArgumentException("Non sei ancora pronto per affrontare " + enemy.getName() + "! Livello EXP troppo basso.");
        }

        StringBuilder log = new StringBuilder();
        log.append("⚔️ INIZIA LO SCONTRO: ").append(this.name).append(" VS ").append(enemy.getName()).append(" ⚔️\n\n");

        int currentHeroHp = this.hp;
        int currentEnemyHp = enemy.getHp();

        int totalAtk = this.atk;
        if (this.equippedWeapon != null) {
            totalAtk += this.equippedWeapon.getPower();
        }

        int damageToEnemy = Math.max(1, totalAtk - enemy.getDef());
        int damageToHero = Math.max(1, enemy.getAtk() - this.def);

        int turn = 1;

        while (currentHeroHp > 0 && currentEnemyHp > 0) {
            log.append("--- Turno ").append(turn).append(" ---\n");

            // 1. L'Eroe attacca
            currentEnemyHp -= damageToEnemy;
            log.append("🗡️ ").append(this.name).append(" infligge ").append(damageToEnemy).append(" danni. (HP Nemico: ").append(Math.max(0, currentEnemyHp)).append(")\n");

            if (currentEnemyHp <= 0) break; // Il nemico è morto, interrompe il ciclo!

            // 2. Il Nemico contrattacca
            currentHeroHp -= damageToHero;
            log.append("🩸 ").append(enemy.getName()).append(" contrattacca e infligge ").append(damageToHero).append(" danni. (Tuoi HP: ").append(Math.max(0, currentHeroHp)).append(")\n\n");

            turn++;
        }

        // Verifica il risultato dello scontro
        if (currentHeroHp > 0) {
            // VITTORIA
            int expGained = enemy.getAtk() + enemy.getDef(); // L'EXP guadagnata dipende dalla forza del mostro
            this.exp += expGained;
            log.append("\n🏆 VITTORIA! Hai sconfitto ").append(enemy.getName()).append(".\n");
            log.append("✨ Hai guadagnato ").append(expGained).append(" punti EXP! (Nuova EXP totale: ").append(this.exp).append(")");
            return log.toString();
        } else {
            // SCONFITTA: L'eroe muore. Lanciamo un'eccezione speciale che cattureremo nel Controller.
            log.append("\n☠️ SCONFITTA... ").append(this.name).append(" è caduto valorosamente in battaglia.");
            throw new RuntimeException(log.toString());
        }
    }

    public String fight(Boss boss) {
        // Usa la regola specifica per i Boss (EXP >= Livello Boss)
        if (!canFight(boss)) {
            throw new IllegalArgumentException("Non sei degno! Il tuo livello EXP (" + this.exp + ") è troppo basso per affrontare " + boss.getName() + " (Richiede livello " + boss.getLevel() + ").");
        }

        StringBuilder log = new StringBuilder();
        log.append("💀 SCONTRO EPICO: ").append(this.name).append(" VS ").append(boss.getName()).append(" 💀\n\n");

        int currentHeroHp = this.hp;
        int currentBossHp = boss.getHp();

        // Calcoliamo l'attacco totale (Base + Arma se equipaggiata)
        int totalAtk = this.atk;
        if (this.equippedWeapon != null) {
            totalAtk += this.equippedWeapon.getPower();
        }

        int damageToBoss = Math.max(1, totalAtk - boss.getDef());
        int damageToHero = Math.max(1, boss.getAtk() - this.def);

        int turn = 1;

        while (currentHeroHp > 0 && currentBossHp > 0) {
            log.append("--- Turno ").append(turn).append(" ---\n");

            currentBossHp -= damageToBoss;
            log.append("🗡️ ").append(this.name).append(" infligge ").append(damageToBoss).append(" danni. (HP Boss: ").append(Math.max(0, currentBossHp)).append(")\n");

            if (currentBossHp <= 0) break;

            currentHeroHp -= damageToHero;
            log.append("🔥 ").append(boss.getName()).append(" scatena la sua furia e infligge ").append(damageToHero).append(" danni. (Tuoi HP: ").append(Math.max(0, currentHeroHp)).append(")\n\n");

            turn++;
        }

        if (currentHeroHp > 0) {
            // Ricompensa massiccia per aver battuto il Boss
            int expGained = (boss.getAtk() + boss.getDef()) * 2;
            this.exp += expGained;
            log.append("\n👑 IMPRESA LEGGENDARIA! Hai sconfitto il Boss ").append(boss.getName()).append("!\n");
            log.append("✨ Hai guadagnato ").append(expGained).append(" punti EXP! (Nuova EXP: ").append(this.exp).append(")");
            return log.toString();
        } else {
            log.append("\n☠️ SCONFITTA FATALE... ").append(this.name).append(" è stato annientato dal Boss.");
            throw new RuntimeException(log.toString());
        }
    }
}