package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;
import tn.esprit.services.ServiceMedicament;

import java.util.List;

public class ValidationOrdonnance {
    @FXML
    private ListView<Ordonnance> ordonnancesListView;


    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button validerButton; // Bouton pour valider l'ordonnance

    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();
    private ServiceMedicament serviceMedicament = new ServiceMedicament();
    private Ordonnance ordonnanceSelectionnee; // Ordonnance sélectionnée

    @FXML
    public void initialize() {
        searchTypeComboBox.getItems().addAll("Date", "Doctor Name");
        searchTypeComboBox.setValue("Date");

        loadOrdonnances(serviceOrdonnance.getAll());

        validerButton.setDisable(true); // Désactiver le bouton par défaut
    }

    @FXML
    public void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        String searchTerm = searchField.getText().trim();

        List<Ordonnance> filteredOrdonnances;

        if (searchType.equals("Date")) {
            filteredOrdonnances = serviceOrdonnance.getOrdonnancesByDate(searchTerm);
        } else {
            filteredOrdonnances = serviceOrdonnance.getAll();
        }

        loadOrdonnances(filteredOrdonnances);
    }

    @FXML
    public void handleSelection() {
        ordonnanceSelectionnee = ordonnancesListView.getSelectionModel().getSelectedItem();
        validerButton.setDisable(ordonnanceSelectionnee == null);
    }



    @FXML
    public void validerOrdonnance() {
        if (ordonnanceSelectionnee != null) {
            System.out.println("Validation de l'ordonnance ID: " + ordonnanceSelectionnee.getId());
            System.out.println("Médicaments: " + ordonnanceSelectionnee.getMedicaments());

            // Vérifier si l'ordonnance est déjà validée
            if ("Validée".equals(ordonnanceSelectionnee.getStatut())) {
                showAlert("⚠️ Cette ordonnance est déjà validée.");
                return;
            }

            // Mise à jour du stock des médicaments
            boolean stockMisAJour = serviceMedicament.updateStock(ordonnanceSelectionnee.getMedicaments());

            if (stockMisAJour) {
                ordonnanceSelectionnee.setStatut("Validée");
                serviceOrdonnance.updateStatut(ordonnanceSelectionnee.getId(), "Validée");

                showAlert("✅ Ordonnance validée avec succès !");
                loadOrdonnances(serviceOrdonnance.getAll()); // Rafraîchir la liste
            } else {
                showAlert("❌ Impossible de valider l'ordonnance : Stock insuffisant !");
            }
        }
    }


    private void loadOrdonnances(List<Ordonnance> ordonnances) {
        ordonnancesListView.getItems().clear();
        ordonnancesListView.getItems().addAll(ordonnances);

        // Afficher un texte personnalisé au lieu de l'objet brut
        ordonnancesListView.setCellFactory(listView -> new ListCell<Ordonnance>() {
            @Override
            protected void updateItem(Ordonnance ordonnance, boolean empty) {
                super.updateItem(ordonnance, empty);
                if (empty || ordonnance == null) {
                    setText(null);
                } else {
                    setText("Doctor: " + ordonnance.getMedecinId() +
                            " | Patient: " + ordonnance.getPatientId() +
                            " | Date: " + ordonnance.getDatePrescription() +
                            " | Statut: " + ordonnance.getStatut());
                }
            }
        });
    }



    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 



 