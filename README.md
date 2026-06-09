# 📌 RPG Arena - Il Signore Oscuro

RPG Arena è un gioco gestionale di ruolo (RPG) a turni con meccaniche di Permadeath. Il giocatore crea e gestisce un party di Eroi, equipaggia armi generate proceduralmente e affronta battaglie nell'Arena contro nemici per accumulare esperienza, con l'obiettivo finale di sconfiggere il potente Boss nella Stanza del Trono.

---

## 🚀 Come eseguire il progetto
### Prerequisiti
- Java 25 (LTS)
- Gradle

### Istruzioni

```bash
git clone https://github.com/samuelee04/Gioco-di-Ruolo.git
cd Gioco-di-Ruolo
```

### Build del progetto
```bash
gradle build
```

### Esecuzione
```bash
gradle run
```

---

## 🤖 Uso di strumenti di AI

In questo progetto ho utilizzato l'Intelligenza Artificiale (Gemini) come tutor personale e assistente alla programmazione. L'AI non ha scritto il codice al mio posto, ma mi ha guidato nello sviluppo architetturale e nella risoluzione di problemi tecnici complessi.

In particolare, ho utilizzato Gemini per:

* **Gestione del Database (Hibernate):** L'AI mi ha guidato nella comprensione dell'ORM, spiegandomi come configurare la persistenza su database H2 locale.
* **Troubleshooting:** Risolvere conflitti tra dipendenze di Gradle, diagnosticare errori di caricamento dei file FXML (es. LoadException).
* **Game Design:** Discutere e bilanciare matematicamente le statistiche base degli Eroi, dei Nemici e le probabilità di successo nell'Arena, per rendere il Permadeath e lo scontro con il Boss una sfida equa.
* **Interfaccia Grafica (JavaFX e Scene Builder):** L'AI mi ha supportato nella stesura e strutturazione del codice necessario per collegare correttamente i file FXML disegnati su Scene Builder ai Controller Java (es. gestione degli @FXML, impostazione delle TableView e dei parametri di layout).

Tutta la logica di business, le meccaniche di base e la struttura del progetto sono state pensate, scritte e comprese da me. L'AI è intervenuta per velocizzare il setup della GUI e come guida per aiutarmi a superare gli ostacoli tecnici più difficili.

📌 Per una descrizione tecnica più dettagliata dell’architettura e delle scelte implementative, consultare la Wiki del repository.
---
