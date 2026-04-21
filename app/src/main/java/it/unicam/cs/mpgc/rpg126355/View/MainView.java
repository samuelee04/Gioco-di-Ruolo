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
}
