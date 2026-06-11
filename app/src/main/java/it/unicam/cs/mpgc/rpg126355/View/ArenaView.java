package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Enemy;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;

import java.util.List;

public class ArenaView {

    @FXML private TableView<Hero> heroTable;
    @FXML private TableColumn<Hero, Long> hIdCol;
    @FXML private TableColumn<Hero, String> hNameCol;
    @FXML private TableColumn<Hero, Integer> hHpCol;
    @FXML private TableColumn<Hero, Integer> hAtkCol;
    @FXML private TableColumn<Hero, Integer> hDefCol;
    @FXML private TableColumn<Hero, Integer> hExpCol;

    @FXML private TableView<Enemy> enemyTable;
    @FXML private TableColumn<Enemy, Long> eIdCol;
    @FXML private TableColumn<Enemy, String> eNameCol;
    @FXML private TableColumn<Enemy, Integer> eHpCol;
    @FXML private TableColumn<Enemy, Integer> eAtkCol;
    @FXML private TableColumn<Enemy, Integer> eDefCol;

    // Area di testo in sola lettura che mostra il log turno per turno del combattimento.
    @FXML private TextArea combatLogArea;

    private Controller<Hero> heroController;
    private Controller<Enemy> enemyController;

    // Liste osservabili collegate alle rispettive TableView.
    private final ObservableList<Hero> heroItems = FXCollections.observableArrayList();
    private final ObservableList<Enemy> enemyItems = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega le colonne di entrambe le TableView alle proprietà dei rispettivi Model
     * e imposta la policy di ridimensionamento automatico delle colonne.
     */
    @FXML
    public void initialize() {
        // Setup colonne tabella Eroi
        hIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        hNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hHpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        hAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        hDefCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        hExpCol.setCellValueFactory(new PropertyValueFactory<>("exp"));
        heroTable.setItems(heroItems);
        heroTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Setup colonne tabella Nemici
        eIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        eNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        eHpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        eAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        eDefCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        enemyTable.setItems(enemyItems);
        enemyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Inietta i controller necessari alla View e carica immediatamente le liste di eroi e nemici.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param heroController  controller per la persistenza degli eroi.
     * @param enemyController controller per la persistenza dei nemici.
     */
    public void setControllers(Controller<Hero> heroController, Controller<Enemy> enemyController) {
        this.heroController = heroController;
        this.enemyController = enemyController;
        loadData();
    }

    // Ricarica dal database le liste di eroi e nemici e aggiorna entrambe le TableView.
    private void loadData() {
        heroItems.clear();
        enemyItems.clear();
        if (heroController != null && enemyController != null) {
            heroItems.addAll(heroController.getAll());
            enemyItems.addAll(enemyController.getAll());
        }
    }

    /**
     * Gestisce lo scontro tra l'eroe e il nemico selezionati.
     *
     * <p>Tre esiti possibili:</p>
     * <ul>
     *   <li><b>Vittoria</b>: aggiorna l'eroe nel DB (nuova EXP) e rimuove il nemico sconfitto.</li>
     *   <li><b>Livello troppo basso</b> ({@link IllegalArgumentException}): mostra un avviso,
     *       nessuna modifica al DB.</li>
     *   <li><b>Sconfitta / Permadeath</b> ({@link RuntimeException}): rimuove l'eroe dal DB.</li>
     * </ul>
     * In tutti i casi il log del combattimento viene mostrato nella {@code combatLogArea}.
     */
    @FXML
    public void handleFight() {
        Hero eroe = heroTable.getSelectionModel().getSelectedItem();
        Enemy nemico = enemyTable.getSelectionModel().getSelectedItem();

        if (eroe == null || nemico == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Devi selezionare un eroe e un bersaglio!");
            return;
        }

        combatLogArea.clear();

        try {
            // Esegue il combattimento
            String log = eroe.fight(nemico);
            combatLogArea.setText(log);

            // Vittoria: aggiorna l'EXP dell'eroe e rimuove il nemico dal mondo
            heroController.update(eroe);
            enemyController.remove(nemico.getId());

            showAlert(Alert.AlertType.INFORMATION, "Vittoria!", "Hai sconfitto il nemico!");

        } catch (IllegalArgumentException e) {
            // Livello EXP dell'eroe insufficiente: nessuna modifica al DB
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.WARNING, "Livello insufficiente", e.getMessage());

        } catch (RuntimeException e) {
            // Sconfitta: l'eroe muore — Permadeath, rimosso definitivamente dal DB
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Sconfitta", "Il tuo eroe è stato massacrato...");
            heroController.remove(eroe.getId());
        } finally {
            // Ricarica sempre i dati e pulisce le selezioni, indipendentemente dall'esito
            loadData();
            heroTable.getSelectionModel().clearSelection();
            enemyTable.getSelectionModel().clearSelection();
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