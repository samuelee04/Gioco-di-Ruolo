package it.unicam.cs.mpgc.rpg126355.View;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;
import it.unicam.cs.mpgc.rpg126355.Model.Weapon;

import java.util.List;

public class EquipView {

    // Tabella Eroi
    @FXML private TableView<Hero> heroTable;
    @FXML private TableColumn<Hero, Long> hIdCol;
    @FXML private TableColumn<Hero, String> hNameCol;
    @FXML private TableColumn<Hero, Integer> hAtkCol;
    @FXML private TableColumn<Hero, Integer> hExpCol;
    @FXML private TableColumn<Hero, String> hWeaponCol;

    // Tabella Armi
    @FXML private TableView<Weapon> weaponTable;
    @FXML private TableColumn<Weapon, String> wNameCol;
    @FXML private TableColumn<Weapon, Integer> wPowerCol;
    @FXML private TableColumn<Weapon, Integer> wLevelCol;

    private Controller<Hero> heroController;
    private Controller<Weapon> weaponController;

    // Liste osservabili collegate alle rispettive TableView.
    private final ObservableList<Hero> heroItems = FXCollections.observableArrayList();
    private final ObservableList<Weapon> weaponItems = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega le colonne di entrambe le TableView alle proprietà dei rispettivi Model.
     *
     * <p>La colonna {@code hWeaponCol} usa una lambda con {@link ReadOnlyStringWrapper} invece di
     * {@code PropertyValueFactory} perché il nome dell'arma è un campo annidato
     * ({@code hero.getEquippedWeapon().getName()}), non una proprietà diretta di {@link Hero}.</p>
     */
    @FXML
    public void initialize() {
        // Setup colonne tabella Eroi
        hIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        hNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        hExpCol.setCellValueFactory(new PropertyValueFactory<>("exp"));

        // Campo annidato: recupera il nome dell'arma equipaggiata, o "nessuna" se non c'è
        hWeaponCol.setCellValueFactory(cd -> {
            Weapon w = cd.getValue().getEquippedWeapon();
            String name = (w != null) ? w.getName() : "Nessuna";
            return new ReadOnlyStringWrapper(name);
        });

        heroTable.setItems(heroItems);
        heroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Setup colonne tabella Armi
        wNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        wPowerCol.setCellValueFactory(new PropertyValueFactory<>("power"));
        wLevelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

        weaponTable.setItems(weaponItems);
        weaponTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Inietta i controller necessari alla View e carica immediatamente le liste di eroi e armi.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param heroController   controller per la persistenza degli eroi.
     * @param weaponController controller per la persistenza delle armi.
     */
    public void setControllers(Controller<Hero> heroController, Controller<Weapon> weaponController) {
        this.heroController = heroController;
        this.weaponController = weaponController;
        loadData();
    }

    // Ricarica dal database le liste di eroi e armi e aggiorna entrambe le TableView.
    private void loadData() {
        heroItems.clear();
        weaponItems.clear();
        if (heroController != null && weaponController != null) {
            heroItems.addAll(heroController.getAll());
            weaponItems.addAll(weaponController.getAll());
        }
    }

    /**
     * Gestisce l'equipaggiamento dell'arma selezionata sull'eroe selezionato.
     * Delega la logica di validazione del livello a {@link Hero#equipWeapon(Weapon)},
     * poi persiste la modifica nel database tramite il controller.
     * In caso di livello insufficiente mostra un avviso senza modificare il DB.
     */
    @FXML
    public void handleEquip() {
        Hero selectedHero = heroTable.getSelectionModel().getSelectedItem();
        Weapon selectedWeapon = weaponTable.getSelectionModel().getSelectedItem();

        if (selectedHero == null || selectedWeapon == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Devi selezionare sia un Eroe che un'Arma!");
            return;
        }

        try {
            // Delega la validazione del livello al Model; lancia IllegalArgumentException se non è idoneo
            selectedHero.equipWeapon(selectedWeapon);
            heroController.update(selectedHero); // Salva nel database

            showAlert(Alert.AlertType.INFORMATION, "Successo", "Hai equipaggiato l'arma " + selectedWeapon.getName() + " a " + selectedHero.getName() + "!");

            // Ricarica le tabelle per riflettere la nuova arma nella colonna ARMA
            loadData();
            heroTable.getSelectionModel().clearSelection();
            weaponTable.getSelectionModel().clearSelection();
        } catch (IllegalArgumentException ex) {
            // Livello EXP dell'eroe insufficiente per questa arma
            showAlert(Alert.AlertType.ERROR, "Livello insufficiente", ex.getMessage());
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore: " + ex.getMessage());
        }
    }

    // Mostra un dialog di avviso all'utente con il tipo, il titolo e il messaggio specificati.
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}