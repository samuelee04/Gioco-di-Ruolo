package it.unicam.cs.mpgc.rpg126355.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import it.unicam.cs.mpgc.rpg126355.Controller.Controller;
import it.unicam.cs.mpgc.rpg126355.Model.Boss;
import it.unicam.cs.mpgc.rpg126355.Model.Hero;

import java.util.List;

public class BossArenaView {

    @FXML private TableView<Hero> heroTable;
    @FXML private TableColumn<Hero, Long> hIdCol;
    @FXML private TableColumn<Hero, String> hNameCol;
    @FXML private TableColumn<Hero, Integer> hHpCol;
    @FXML private TableColumn<Hero, Integer> hAtkCol;
    @FXML private TableColumn<Hero, Integer> hDefCol;
    @FXML private TableColumn<Hero, Integer> hExpCol;

    @FXML private TableView<Boss> bossTable;
    @FXML private TableColumn<Boss, Long> bIdCol;
    @FXML private TableColumn<Boss, String> bNameCol;
    @FXML private TableColumn<Boss, Integer> bHpCol;
    @FXML private TableColumn<Boss, Integer> bAtkCol;
    @FXML private TableColumn<Boss, Integer> bDefCol;
    @FXML private TableColumn<Boss, Integer> bLevelCol;

    // Area di testo in sola lettura che mostra il log turno per turno del combattimento.
    @FXML private TextArea combatLogArea;

    private Controller<Hero> heroController;
    private Controller<Boss> bossController;

    // Liste osservabili collegate alle rispettive TableView.
    private final ObservableList<Hero> heroItems = FXCollections.observableArrayList();
    private final ObservableList<Boss> bossItems = FXCollections.observableArrayList();

    /**
     * Metodo di inizializzazione invocato automaticamente da JavaFX dopo il caricamento del FXML.
     * Collega le colonne di entrambe le TableView alle proprietà dei rispettivi Model.
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

        // Setup colonne tabella Boss
        bIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        bHpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        bAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        bDefCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        bLevelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        bossTable.setItems(bossItems);
    }

    /**
     * Inietta i controller necessari alla View e carica immediatamente le liste di eroi e boss.
     * Deve essere invocato dalla schermata chiamante dopo il caricamento dell'FXML.
     *
     * @param heroController controller per la persistenza degli eroi.
     * @param bossController controller per la persistenza dei boss.
     */
    public void setControllers(Controller<Hero> heroController, Controller<Boss> bossController) {
        this.heroController = heroController;
        this.bossController = bossController;
        loadData();
    }

    // Ricarica dal database le liste di eroi e boss e aggiorna entrambe le TableView.
    private void loadData() {
        heroItems.clear();
        bossItems.clear();
        if (heroController != null && bossController != null) {
            heroItems.addAll(heroController.getAll());
            bossItems.addAll(bossController.getAll());
        }
    }

    /**
     * Gestisce l'evento di attivazione dello scontro tra l'Eroe e il Boss selezionati nelle tabelle.
     * <p>
     * Il metodo cattura le risposte del motore di gioco integrato nel Model ed esegue i relativi
     * flussi alternativi sulla persistenza:
     * <ul>
     * <li><b>Vittoria:</b> Salva i progressi (EXP accumulata) dell'Eroe nel DB e distrugge il Boss sconfitto.</li>
     * <li><b>Requisito Insufficiente:</b> Blocca il combattimento stampando l'avviso di livello non idoneo.</li>
     * <li><b>Sconfitta (Permadeath):</b> Mostra il log fatale e rimuove definitivamente l'Eroe dal DB.</li>
     * </ul>
     * Al termine dello scontro, l'interfaccia viene riallineata resettando le selezioni correnti.
     */
    @FXML
    public void handleFight() {
        Hero eroe = heroTable.getSelectionModel().getSelectedItem();
        Boss boss = bossTable.getSelectionModel().getSelectedItem();

        if (eroe == null || boss == null) {
            showAlert(Alert.AlertType.WARNING, "Attenzione", "Seleziona un Eroe e un Boss!");
            return;
        }

        combatLogArea.clear();

        try {
            // Esegue il combattimento
            String log = eroe.fight(boss);
            combatLogArea.setText(log);

            // Vittoria: aggiorna l'EXP dell'eroe e rimuove il boss dal mondo
            heroController.update(eroe);
            bossController.remove(boss.getId());

            showAlert(Alert.AlertType.INFORMATION, "IMPRESA LEGGENDARIA!", "Hai sconfitto il Signore Oscuro!");

        } catch (IllegalArgumentException e) {

            // Livello EXP dell'eroe insufficiente: nessuna modifica al DB
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.WARNING, "Non sei pronto", e.getMessage());

        } catch (RuntimeException e) {

            // Sconfitta: l'eroe muore — Permadeath, rimosso definitivamente dal DB
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Fatality", "Il tuo eroe è stato annientato...");
            heroController.remove(eroe.getId());
        } finally {
            // Ricarica sempre i dati e pulisce le selezioni, indipendentemente dall'esito
            loadData();
            heroTable.getSelectionModel().clearSelection();
            bossTable.getSelectionModel().clearSelection();
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