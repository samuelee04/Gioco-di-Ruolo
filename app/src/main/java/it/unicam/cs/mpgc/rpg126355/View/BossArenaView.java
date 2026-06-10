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

    /**
     * Liste osservabili utilizzate da JavaFX per riflettere dinamicamente
     * i cambiamenti dei dati del database all'interno delle TableView.
     */
    private final ObservableList<Hero> heroItems = FXCollections.observableArrayList();
    private final ObservableList<Boss> bossItems = FXCollections.observableArrayList();

    /**
     * Metodo di ciclo di vita di JavaFX. Viene invocato automaticamente al caricamento del file FXML.
     * Configura il data binding delle colonne delle tabelle (Eroi e Boss) mappando i campi
     * delle relative entità del Model tramite {@link PropertyValueFactory}.
     */
    @FXML
    public void initialize() {
        // Setup Eroi
        hIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        hNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        hHpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        hAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        hDefCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        hExpCol.setCellValueFactory(new PropertyValueFactory<>("exp"));
        heroTable.setItems(heroItems);

        // Setup Boss
        bIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        bHpCol.setCellValueFactory(new PropertyValueFactory<>("hp"));
        bAtkCol.setCellValueFactory(new PropertyValueFactory<>("atk"));
        bDefCol.setCellValueFactory(new PropertyValueFactory<>("def"));
        bLevelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        bossTable.setItems(bossItems);
    }

    /**
     * Inietta i controller di persistenza necessari al corretto funzionamento della vista
     * e avvia il caricamento iniziale dei record delle tabelle.
     *
     * @param heroController   Il controller delegato alla gestione dell'entità {@link Hero}.
     * @param bossController   Il controller delegato alla gestione dell'entità {@link Boss}.
     */
    public void setControllers(Controller<Hero> heroController, Controller<Boss> bossController) {
        this.heroController = heroController;
        this.bossController = bossController;
        loadData();
    }

    /**
     * Sincronizza le liste osservabili grafiche recuperando i dati aggiornati
     * dal database tramite i rispettivi controller di persistenza.
     */
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
            // Esegue il combattimento epico
            String log = eroe.fight(boss);
            combatLogArea.setText(log);

            // Vittoria
            heroController.update(eroe);
            bossController.remove(boss.getId());

            showAlert(Alert.AlertType.INFORMATION, "IMPRESA LEGGENDARIA!", "Hai sconfitto il Signore Oscuro!");

        } catch (IllegalArgumentException e) {
            // Livello troppo basso
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.WARNING, "Non sei pronto", e.getMessage());

        } catch (RuntimeException e) {
            // Sconfitta (Permadeath)
            combatLogArea.setText(e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Fatality", "Il tuo eroe è stato annientato...");
            heroController.remove(eroe.getId());
        } finally {
            loadData();
            heroTable.getSelectionModel().clearSelection();
            bossTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * Visualizza un dialogo modale di avviso (Alert) standard per notificare un messaggio all'utente.
     *
     * @param type    Il tipo di Alert (INFORMATION, WARNING, ERROR).
     * @param title   Il titolo della finestra del dialogo.
     * @param message Il corpo del messaggio testuale da mostrare.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}