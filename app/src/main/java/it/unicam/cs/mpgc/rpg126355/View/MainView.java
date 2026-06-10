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

}