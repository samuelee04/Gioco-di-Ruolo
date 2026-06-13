package it.unicam.cs.mpgc.rpg126355.View;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Boss;
import it.unicam.cs.mpgc.rpg126355.Model.Enemy;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;
import it.unicam.cs.mpgc.rpg126355.Model.Weapon;

/**
 * Controller della schermata principale dell'applicazione.
 * Gestisce la navigazione tra le varie View del gioco, caricando dinamicamente
 * il FXML corrispondente e iniettandolo nel {@code mainPane} centrale.
 *
 */
public class MainView {

    @FXML
    private BorderPane mainPane;

    private Controller<Enemy> enemyController;
    private Controller<Boss> bossController;
    private Controller<Hero> heroController;
    private Controller<Weapon> weaponController;

    /**
     * Inietta tutti i controller necessari alle View figlie.
     * Invocato da {@code App} subito dopo il caricamento del FXML principale.
     */
    public void setControllers(Controller<Enemy> enemyController,
                               Controller<Boss> bossController,
                               Controller<Hero> heroController,
                               Controller<Weapon> weaponController) {
        this.enemyController = enemyController;
        this.bossController = bossController;
        this.heroController = heroController;
        this.weaponController = weaponController;
    }

    // ============================================================
    // --- AZIONI DEI BOTTONI GIOCO ---
    // ============================================================

    // Mostra la schermata "La Taverna degli Eroi": creazione/eliminazione eroi e generazione del mondo.
    @FXML
    public void handleHeroesClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/HeroView.fxml"));
            javafx.scene.Parent root = loader.load();

            HeroView view = loader.getController();
            view.setControllers(heroController, enemyController, weaponController, bossController);

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Armeria": equipaggiamento delle armi sugli eroi.
    @FXML
    public void handleEquipClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/EquipView.fxml"));
            javafx.scene.Parent root = loader.load();

            EquipView view = loader.getController();
            view.setControllers(heroController, weaponController);

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Arena di Combattimento": scontro tra eroe e nemico.
    @FXML
    public void handleArenaClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/ArenaView.fxml"));
            javafx.scene.Parent root = loader.load();

            ArenaView view = loader.getController();
            view.setControllers(heroController, enemyController);

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Stanza del Trono": scontro tra eroe e boss.
    @FXML
    public void handleBossArenaClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/BossArenaView.fxml"));
            javafx.scene.Parent root = loader.load();

            BossArenaView view = loader.getController();
            view.setControllers(heroController, bossController); // Usa il controller dei Boss

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Regole del Gioco": contenuto statico, nessun controller necessario.
    @FXML
    public void handleRulesClick() {
        try {
            // Carica la grafica pura, non c'è bisogno di passare nessun controller!
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/RulesView.fxml"));
            javafx.scene.Parent root = loader.load();

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ============================================================
    // --- AZIONI DEI BOTTONI ADMIN ---
    // ============================================================

    // Mostra la schermata "Bestiario": elenco e gestione dei nemici presenti nel mondo.
    @FXML
    public void handleEnemiesClick() {
        try {
            // 1. Carica il file FXML dei nemici
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/EnemyView.fxml"));
            javafx.scene.Parent root = loader.load();

            // 2. Prendo il controller e passo il database
            EnemyView view = loader.getController();
            view.setController(enemyController);

            // 3. Lo metto al centro del menù principale
            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Arsenale": elenco e gestione delle armi presenti nel mondo.
    @FXML
    public void handleWeaponsClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/WeaponView.fxml"));
            javafx.scene.Parent root = loader.load();

            WeaponView view = loader.getController();
            view.setController(weaponController);

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mostra la schermata "Sala del Trono - Boss": elenco e gestione dei boss presenti nel mondo.
    @FXML
    public void handleBossesClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/BossView.fxml"));
            javafx.scene.Parent root = loader.load();

            BossView view = loader.getController();
            view.setController(bossController);

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}