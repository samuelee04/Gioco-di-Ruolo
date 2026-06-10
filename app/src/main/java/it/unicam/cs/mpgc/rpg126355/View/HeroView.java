package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Boss;
import it.unicam.cs.mpgc.rpg126355.Model.Enemy;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;
import it.unicam.cs.mpgc.rpg126355.Model.Weapon;

import java.util.List;

public class HeroView {

    @FXML private TextField heroNameField;
    @FXML private TableView<Hero> heroTable;
    @FXML private TableColumn<Hero, Long> idCol;
    @FXML private TableColumn<Hero, String> nameCol;
    @FXML private TableColumn<Hero, Integer> hpCol;
    @FXML private TableColumn<Hero, Integer> atkCol;
    @FXML private TableColumn<Hero, Integer> defCol;
    @FXML private TableColumn<Hero, Integer> expCol;

    private Controller<Hero> heroController;
    private Controller<Enemy> enemyController;
    private Controller<Weapon> weaponController;
    private Controller<Boss> bossController;

    private final ObservableList<Hero> heroItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        defCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        expCol.setCellValueFactory(new PropertyValueFactory<>("exp"));

        heroTable.setItems(heroItems);
        heroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void setControllers(Controller<Hero> heroCtrl, Controller<Enemy> enemyCtrl,
                               Controller<Weapon> weaponCtrl, Controller<Boss> bossCtrl) {
        this.heroController = heroCtrl;
        this.enemyController = enemyCtrl;
        this.weaponController = weaponCtrl;
        this.bossController = bossCtrl;
        loadHeroes();
    }

    private void loadHeroes() {
        heroItems.clear();
        if (heroController != null) {
            List<Hero> all = heroController.getAll();
            heroItems.addAll(all);
        }
    }

    @FXML
    public void handleForgeHero() {
        String name = heroNameField.getText() == null ? "" : heroNameField.getText().trim();
        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Inserisci il nome dell'eroe.");
            return;
        }

        try {
            // 1) Crea e salva l'eroe
            Hero nuovoEroe = new Hero(name);
            heroController.add(nuovoEroe);

            // 2) Evoca 5 nemici
            for (int i = 0; i < 5; i++) {
                Enemy enemy = Enemy.generateRandomEnemy();
                enemyController.add(enemy);
            }

            // 3) Evoca 3 armi
            for (int i = 0; i < 3; i++) {
                Weapon weapon = Weapon.generateRandomWeapon();
                weaponController.add(weapon);
            }

            // 4) Evoca 1 boss
            Boss boss = Boss.generateRandomBoss();
            bossController.add(boss);

            showAlert(Alert.AlertType.INFORMATION, "Successo",
                    "Eroe " + name + " creato! Il mondo ha generato 5 nemici, 3 armi e 1 Boss epico.");

            loadHeroes();
            heroNameField.clear();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile forgiare l'eroe: " + ex.getMessage());
        }
    }

    @FXML
    public void handleDeleteHero() {
        Hero selected = heroTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un eroe dalla tabella");
            return;
        }
        try {
            heroController.remove(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Informazione", "L'Eroe è stato rimosso, ma i pericoli creati rimangono!");
            loadHeroes();
            heroTable.getSelectionModel().clearSelection();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile eliminare: " + ex.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}