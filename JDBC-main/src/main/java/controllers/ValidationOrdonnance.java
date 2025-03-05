package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;
import tn.esprit.services.ServiceMedicament;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ValidationOrdonnance {
    @FXML
    private ListView<Ordonnance> ordonnancesListView;
    @FXML
    private ComboBox<String> searchTypeComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button validerButton; // Bouton pour valider l'ordonnance
    @FXML
    private DatePicker datePicker; // DatePicker for date search

    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();
    private ServiceMedicament serviceMedicament = new ServiceMedicament();
    private Ordonnance ordonnanceSelectionnee; // Ordonnance sélectionnée

    @FXML
    public void initialize() {
        // Initialisation du ComboBox avec les options de recherche
        searchTypeComboBox.getItems().addAll("Date", "Nom patient");
        searchTypeComboBox.setValue("Date");

        // Charger toutes les ordonnances au début
        loadOrdonnances(serviceOrdonnance.getAll());

        // Désactiver le bouton Valider par défaut
        validerButton.setDisable(true);

        // Gestion de la visibilité dynamique des champs
        searchTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Date".equals(newValue)) {
                datePicker.setVisible(true);
                searchField.setVisible(false);
            } else if ("Nom patient".equals(newValue)) {
                searchField.setVisible(true);
                datePicker.setVisible(false);
            }
        });
    }

    @FXML
    public void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        List<Ordonnance> filteredOrdonnances;

        if ("Date".equals(searchType)) {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                String dateString = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                filteredOrdonnances = serviceOrdonnance.getOrdonnancesByDate(dateString);
            } else {
                filteredOrdonnances = serviceOrdonnance.getAll();
            }
        } else if ("Nom patient".equals(searchType)) {
            String patientName = searchField.getText().trim();
            if (!patientName.isEmpty()) {
                filteredOrdonnances = serviceOrdonnance.getOrdonnancesByPatientFullName(patientName);
            } else {
                filteredOrdonnances = serviceOrdonnance.getAll();
            }
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

    private String getUserNameById(int userId) {
        String userName = "";
        String query = "SELECT nom, prenom FROM utilisateur WHERE id = ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/integration", "root", "");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("prenom");
                    String lastName = rs.getString("nom");
                    userName = firstName + " " + lastName;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            userName = "Unknown";  // Fallback in case of error
        }
        return userName;
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
                    String doctorName = getUserNameById(ordonnance.getMedecinId());
                    String patientName = getUserNameById(ordonnance.getPatientId());
                    Map<String, Integer> medicaments = ordonnance.getMedicaments();
                    String medications = "No medications listed";  // Default message

                    if (medicaments != null && !medicaments.isEmpty()) {
                        StringBuilder medicationBuilder = new StringBuilder();
                        for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
                            medicationBuilder.append(entry.getKey())  // Medication name
                                    .append(" (x")
                                    .append(entry.getValue())  // Medication quantity
                                    .append("), ");
                        }
                        medications = medicationBuilder.toString();
                        // Remove the last comma and space
                        medications = medications.substring(0, medications.length() - 2);
                    }
                    setText("Doctor: " +doctorName +
                            " | Patient: " + patientName +
                            " | " + ordonnance.getDatePrescription() +
                            " | Statut: " + ordonnance.getStatut() +
                            " | Medications: " + medications);
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



