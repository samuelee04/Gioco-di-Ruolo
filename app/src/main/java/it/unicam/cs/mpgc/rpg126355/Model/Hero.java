package it.unicam.cs.mpgc.rpg126355.Model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

import java.util.Random;

/**
 * Rappresenta l'entità dell'Eroe, il protagonista controllato dal giocatore.
 * Questa classe è mappata come entità JPA per gestire la persistenza dei dati sul database.
 * Funge da contenitore dello stato del personaggio (statistiche, equipaggiamento) e
 * incapsula temporaneamente il motore dei combattimenti a turni contro nemici standard e boss.
 */
@Entity
public class Hero implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Esperienza iniziale assegnata a ogni eroe alla nascita per bilanciare
     */
    private static final int INITIAL_EXP = 12;

    /**
     * Identificatore univoco del personaggio nel database (Primary Key).
     */
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

    /**
     * Punti esperienza (EXP) accumulati dall'eroe. Determina quando l'eroe
     * può accedere alle sfide ed agli equipaggiamenti.
     */
    @Column(nullable = false)
    private int exp;

    /**
     * L'arma attualmente impugnata dall'eroe.
     * Relazione ManyToOne: più eroi nel tempo possono condividere lo stesso tipo di arma di riferimento.
     */
    @ManyToOne
    @JoinColumn(name = "weapon_id")
    private Weapon equippedWeapon;

    /**
     * Costruttore protetto senza parametri richiesto da JPA/Hibernate
     * per la mappatura e la ricostruzione dell'oggetto dal DB.
     */
    protected Hero() {}


    /**
     * Costruisce un nuovo Eroe inizializzandone il nome, impostando l'esperienza iniziale di sicurezza
     * e generando proceduralmente le statistiche di combattimento entro range bilanciati.
     *
     * @param name Il nome da assegnare all'Eroe.
     * @throws IllegalArgumentException se il nome risulta nullo o vuoto.
     */
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

    /**
     * Verifica se l'esperienza dell'eroe è sufficiente per soddisfare il livello richiesto dall'arma.
     *
     * @param weapon L'arma da controllare.
     * @return true se l'eroe ha l'esperienza necessaria, false altrimenti.
     */
    public boolean canEquip(Weapon weapon) {
        return this.exp >= weapon.getLevel();
    }

    /**
     * Valuta se l'eroe possiede abbastanza esperienza per sfidare un nemico standard.
     * Vincolo stringente: l'EXP deve superare sia l'attacco che la difesa dell'avversario.
     *
     * @param enemy Il nemico da esaminare.
     * @return true se l'eroe è idoneo allo scontro, false se il nemico è "wild" (troppo forte).
     */
    public boolean canFight(Enemy enemy) {
        return this.exp >= enemy.getAtk() && this.exp >= enemy.getDef();
    }

    /**
     * Valuta se l'eroe possiede abbastanza esperienza per accedere allo scontro epico con il Boss.
     *
     * @param boss Il boss da esaminare.
     * @return true se l'esperienza è maggiore o uguale al livello del boss, false altrimenti.
     */
    public boolean canFight(Boss boss) {
        return this.exp >= boss.getLevel();
    }

    /**
     * Equipaggia una nuova arma sul personaggio previa verifica dei requisiti minimi di livello.
     *
     * @param newWeapon La nuova arma da assegnare.
     * @throws IllegalArgumentException se il livello dell'eroe è insufficiente.
     */
    public void equipWeapon(Weapon newWeapon) {
        if (canEquip(newWeapon)) {
            this.equippedWeapon = newWeapon;
        } else {
            throw new IllegalArgumentException("Livello troppo alto per questa arma!");
        }
    }

    /**
     * Elabora il motore di scontro a turni contro un nemico standard.
     * Calcola i danni mitiagandoli con le difese e applicando i bonus dell'arma.
     * Gestisce la progressione dell'EXP in caso di vittoria e la morte permanente dell'eroe.
     *
     * @param enemy Il nemico da affrontare.
     * @return Un log testuale dettagliato dello scontro in caso di vittoria.
     * @throws IllegalArgumentException se l'eroe non soddisfa i requisiti di idoneità al combattimento.
     * @throws RuntimeException se l'eroe perde lo scontro (meccanica Permadeath).
     */
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
            // SCONFITTA: L'eroe muore.
            log.append("\n☠️ SCONFITTA... ").append(this.name).append(" è caduto valorosamente in battaglia.");
            throw new RuntimeException(log.toString());
        }
    }

    /**
     * Elabora il motore di scontro a turni contro il Boss finale del mondo.
     * Presenta un calcolo dei danni potenziato e un moltiplicatore massiccio di EXP in caso di successo.
     *
     * @param boss Il boss finale da affrontare.
     * @return Un log testuale dettagliato dello scontro epico in caso di vittoria.
     * @throws IllegalArgumentException se l'eroe non soddisfa il livello minimo del boss.
     * @throws RuntimeException se l'eroe soccombe sotto i colpi del Boss (meccanica Permadeath).
     */
    public String fight(Boss boss) {
        if (!canFight(boss)) {
            throw new IllegalArgumentException("Non sei degno! Il tuo livello EXP (" + this.exp + ") è troppo basso per affrontare " + boss.getName() + " (Richiede livello " + boss.getLevel() + ").");
        }

        StringBuilder log = new StringBuilder();
        log.append("💀 SCONTRO EPICO: ").append(this.name).append(" VS ").append(boss.getName()).append(" 💀\n\n");

        int currentHeroHp = this.hp;
        int currentBossHp = boss.getHp();

        // Calcolo l'attacco totale (Base + Arma se equipaggiata)
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
            // Ricompensa di exp per aver battuto il Boss
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