package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Boss;

import java.util.List;

public class BossView {

    @FXML private TableView<Boss> table;
    @FXML private TableColumn<Boss, Long> idCol;
    @FXML private TableColumn<Boss, String> nameCol;
    @FXML private TableColumn<Boss, Integer> hpCol;
    @FXML private TableColumn<Boss, Integer> atkCol;
    @FXML private TableColumn<Boss, Integer> defCol;

    private Controller<Boss> bossController;

    // Lista osservabile collegata alla TableView: ogni modifica si riflette automaticamente nell'UI.
    private final ObservableList<Boss> items = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega ogni colonna della TableView alla proprietà corrispondente del Model Boss
     * e imposta la policy di ridimensionamento automatico delle colonne.
     */
    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        atkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        defCol.setCellValueFactory(new PropertyValueFactory<>("def"));

        table.setItems(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Inietta il controller necessario alla View e carica immediatamente la lista dei boss.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param bossController controller per la persistenza dei boss.
     */
    public void setController(Controller<Boss> bossController) {
        this.bossController = bossController;
        loadData();
    }

    // Ricarica la lista dei boss dal database e aggiorna la TableView.
    private void loadData() {
        items.clear();
        if (bossController != null) {
            List<Boss> all = bossController.getAll();
            items.addAll(all);
        }
    }

    // Elimina il boss selezionato nella TableView dal database.
    @FXML
    public void handleDeleteSelected() {
        Boss selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un Boss da eliminare.");
            return;
        }
        try {
            bossController.remove(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Eliminato", "Il Boss è stato rimosso.");
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