package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Enemy;

import java.util.List;

public class EnemyView {

    @FXML private TableView<Enemy> table;
    @FXML private TableColumn<Enemy, Long> idCol;
    @FXML private TableColumn<Enemy, String> nameCol;
    @FXML private TableColumn<Enemy, Integer> hpCol;
    @FXML private TableColumn<Enemy, Integer> atkCol;
    @FXML private TableColumn<Enemy, Integer> defCol;

    private Controller<Enemy> enemyController;

    // Lista osservabile collegata alla TableView: ogni modifica si riflette automaticamente nell'UI.
    private final ObservableList<Enemy> items = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega ogni colonna della TableView alla proprietà corrispondente del Model Enemy
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
     * Inietta il controller necessario alla View e carica immediatamente la lista dei nemici.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param enemyController controller per la persistenza dei nemici.
     */
    public void setController(Controller<Enemy> enemyController) {
        this.enemyController = enemyController;
        loadData();
    }

    // Ricarica la lista dei nemici dal database e aggiorna la TableView.
    private void loadData() {
        items.clear();
        if(enemyController != null) {
            List<Enemy> all = enemyController.getAll();
            items.addAll(all);
        }
    }

    // Elimina il nemico selezionato nella TableView dal database.
    @FXML
    public void handleDeleteSelected() {
        Enemy selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un nemico da eliminare.");
            return;
        }
        try {
            enemyController.remove(selected.getId());
            showAlert(Alert.AlertType.INFORMATION, "Eliminato", "Il nemico è stato rimosso.");
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