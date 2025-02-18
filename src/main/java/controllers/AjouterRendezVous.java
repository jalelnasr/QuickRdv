package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AjouterRendezVous implements Initializable {
    @FXML
    private TextField date;

    @FXML
    private TextField id_patient;

    @FXML
    private ComboBox<String> medecinComboBox; // ComboBox for selecting a doctor

    @FXML
    private TextField type_consultation;

    private ServiceRendezVous ps = new ServiceRendezVous(); // Service for RendezVous
    private Map<Integer, String> medecinMap = new HashMap<>(); // Map to store doctor IDs and names

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load available doctors from the database
        loadMedecins();
    }

    // Method to load available doctors into the ComboBox
    private void loadMedecins() {
        String query = "SELECT id, specialite FROM medecin"; // Fetch doctor IDs and specialties

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quick_rdv", "root", "");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String specialite = rs.getString("specialite");
                medecinMap.put(id, specialite); // Store doctor ID and specialty in the map
            }

            // Populate the ComboBox with doctor specialties
            medecinComboBox.getItems().addAll(medecinMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des médecins.");
        }
    }

    @FXML
    void reserve(ActionEvent event) {
        try {
            // Validate inputs
            if (date.getText().isEmpty() || id_patient.getText().isEmpty() ||
                    medecinComboBox.getValue() == null || type_consultation.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Parse inputs
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Match the date format in the prompt
            dateFormat.setLenient(false); // Ensure strict date parsing
            java.util.Date parsedDate = dateFormat.parse(date.getText());

            int patientId = Integer.parseInt(id_patient.getText());

            // Check if the patientId is valid
            if (!ps.isPatientIdValid(patientId)) {
                showAlert("Erreur", "ID patient invalide. Veuillez entrer un ID patient valide.");
                return;
            }

            // Get the selected doctor's ID from the map
            String selectedSpecialite = medecinComboBox.getValue();
            int medecinId = -1;
            for (Map.Entry<Integer, String> entry : medecinMap.entrySet()) {
                if (entry.getValue().equals(selectedSpecialite)) {
                    medecinId = entry.getKey();
                    break;
                }
            }

            if (medecinId == -1) {
                showAlert("Erreur", "Médecin non trouvé.");
                return;
            }

            int consultationType = Integer.parseInt(type_consultation.getText());

            // Create and add RendezVous
            RendezVous rendezVous = new RendezVous(parsedDate, patientId, medecinId, consultationType);
            ps.add(rendezVous);

            // Display success message
            showAlert("Succès", "Rendez-vous ajouté avec succès !");
            clearFields(); // Clear input fields after successful addition
        } catch (ParseException e) {
            showAlert("Erreur de format", "Le format de la date est invalide. Utilisez le format YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Les champs ID patient et type de consultation doivent être des nombres entiers.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de l'ajout du rendez-vous: " + e.getMessage());
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to clear input fields
    private void clearFields() {
        date.clear();
        id_patient.clear();
        medecinComboBox.setValue(null); // Clear the selected doctor
        type_consultation.clear();
    }
}