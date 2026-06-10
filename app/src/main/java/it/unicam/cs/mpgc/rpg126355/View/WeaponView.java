package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Weapon;

import java.util.List;

public class WeaponView {

    @FXML private TableView<Weapon> table;
    @FXML private TableColumn<Weapon, Long> idCol;
    @FXML private TableColumn<Weapon, String> nameCol;
    @FXML private TableColumn<Weapon, Integer> powerCol;
    @FXML private TableColumn<Weapon, Integer> levelCol;

    private Controller<Weapon> weaponController;

    // Lista osservabile collegata alla TableView: ogni modifica si riflette automaticamente nell'UI.
    private final ObservableList<Weapon> items = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega ogni colonna della TableView alla proprietà corrispondente del Model Weapon
     * e imposta la policy di ridimensionamento automatico delle colonne.
     */
    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        powerCol.setCellValueFactory(new PropertyValueFactory<>("power"));
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));

        table.setItems(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Inietta il controller necessario alla View e carica immediatamente la lista delle armi.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param weaponController controller per la persistenza delle armi.
     */
    public void setController(Controller<Weapon> weaponController) {
        this.weaponController = weaponController;
        loadData();
    }

    // Ricarica la lista delle armi dal database e aggiorna la TableView.
    private void loadData() {
        items.clear();
        if (weaponController != null) {
            List<Weapon> all = weaponController.getAll();
            items.addAll(all);
        }
    }

    // Elimina l'arma selezionata nella TableView dal database.
    @FXML
    public void handleDeleteSelected() {
        Weapon selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un'arma da eliminare.");
            return;
        }
        try {
            weaponController.remove(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Eliminata", "L'arma è stata rimossa.");
            loadData();
        } catch (Exception ex) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile eliminare: " + ex.getMessage());
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