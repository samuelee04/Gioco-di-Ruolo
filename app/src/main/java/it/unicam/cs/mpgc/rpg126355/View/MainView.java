package it.unicam.cs.mpgc.rpg126355.View;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Boss;
import it.unicam.cs.mpgc.rpg126355.Model.Enemy;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;
import it.unicam.cs.mpgc.rpg126355.Model.Weapon;

public class MainView {

    @FXML
    private BorderPane mainPane;

    private Controller<Enemy> enemyController;
    private Controller<Boss> bossController;
    private Controller<Hero> heroController;
    private Controller<Weapon> weaponController;

    public void setControllers(Controller<Enemy> enemyController,
                               Controller<Boss> bossController,
                               Controller<Hero> heroController,
                               Controller<Weapon> weaponController) {
        this.enemyController = enemyController;
        this.bossController = bossController;
        this.heroController = heroController;
        this.weaponController = weaponController;
    }

    // --- AZIONI DEI BOTTONI GIOCO ---

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

    @FXML
    public void handleBossArenaClick() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/BossArenaView.fxml"));
            javafx.scene.Parent root = loader.load();

            BossArenaView view = loader.getController();
            view.setControllers(heroController, bossController); // Usa il controller dei Boss!

            mainPane.setCenter(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    // --- AZIONI DEI BOTTONI ADMIN ---

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