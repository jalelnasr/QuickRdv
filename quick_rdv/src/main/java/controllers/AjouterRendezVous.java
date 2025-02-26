package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;
import tn.esprit.test.MainFX;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AjouterRendezVous implements Initializable {

    @FXML
    private TextField date;

    @FXML
    private TextField time; // Added time TextField

    @FXML
    private TextField id_patient;

    @FXML
    private ComboBox<String> medecinComboBox;

    @FXML
    private ComboBox<String> typeConsultationComboBox;

    private ServiceRendezVous ps = new ServiceRendezVous();
    private Map<Integer, String> medecinMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedecins();
        loadTypeConsultationOptions();
    }

    private void loadMedecins() {
        String query = "SELECT u.nom,u.prenom, m.specialite " +
                "FROM utilisateur u " +
                "JOIN medecin m ON u.id = m.id " +
                "WHERE u.role = 'medecin'"; // Filter only doctors

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quick_rdv", "root", "");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            medecinComboBox.getItems().clear(); // Clear previous items to avoid duplication

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom"); // Get name from utilisateur
                String specialite = rs.getString("specialite"); // Get specialty from medecin

                // Format: "Doctor Name - Specialty"
                String displayText = "Dr. " + nom + " " + prenom + " - " + specialite;

                medecinComboBox.getItems().add(displayText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des médecins.");
        }
    }

    private void loadTypeConsultationOptions() {
        typeConsultationComboBox.setItems(FXCollections.observableArrayList("Consultation", "Téléconsultation"));
    }

    private int getMedecinIdByNomPrenom(String nom, String prenom) {
        String query = "SELECT m.id FROM medecin m " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE u.nom = ? AND u.prenom = ? AND u.role = 'medecin'";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quick_rdv", "root", "");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Retourne -1 si non trouvé
    }

    @FXML
    void reserve(ActionEvent event) {
        try {
            if (date.getText().isEmpty() || time.getText().isEmpty() || id_patient.getText().isEmpty() ||
                    medecinComboBox.getValue() == null || typeConsultationComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Parse the date and time
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dateTimeFormat.setLenient(false);
            java.util.Date parsedDate = dateTimeFormat.parse(date.getText() + " " + time.getText());

            int patientId = Integer.parseInt(id_patient.getText());
            if (!ps.isPatientIdValid(patientId)) {
                showAlert("Erreur", "ID patient invalide.");
                return;
            }

            String selectedMedecin = medecinComboBox.getValue(); // Ex: "Dr. Ahmed Ben Salah - Cardiologue"
            String[] parts = selectedMedecin.split(" - "); // Séparer spécialité
            if (parts.length < 2) {
                showAlert("Erreur", "Médecin sélectionné invalide.");
                return;
            }

            String fullName = parts[0]; // Extrait "Dr. Ahmed Ben Salah"
            String[] nameParts = fullName.split(" ", 3); // Sépare "Dr.", "Ahmed", "Ben Salah"

            if (nameParts.length < 3) {
                showAlert("Erreur", "Le format du nom du médecin est invalide.");
                return;
            }

            String medecinNom = nameParts[1]; // "Ahmed"
            String medecinPrenom = nameParts[2]; // "Ben Salah"

            // Récupérer l'ID du médecin
            int medecinId = getMedecinIdByNomPrenom(medecinNom, medecinPrenom);
            if (medecinId == -1) {
                showAlert("Erreur", "Médecin non trouvé.");
                return;
            }

            // Vérifier la disponibilité du médecin pour la date et l'heure
            if (!ps.isMedecinAvailable(medecinId, parsedDate)) {
                showAlert("Erreur", "Le médecin n'est pas disponible à cette date et heure.");
                return;
            }

            // Convertir le texte du ComboBox en ID numérique
            int consultationType = typeConsultationComboBox.getValue().equals("Consultation") ? 1 : 2;

            RendezVous rendezVous = new RendezVous(parsedDate, patientId, medecinId, consultationType);
            ps.add(rendezVous);

            showAlert("Succès", "Rendez-vous ajouté avec succès !");
            clearFields();
        } catch (ParseException e) {
            showAlert("Erreur de format", "Le format de la date et de l'heure est invalide. Utilisez YYYY-MM-DD HH:mm.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "L’ID patient doit être un nombre entier.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    @FXML
    private void handleAfficherRendezVous() {
        MainFX.showAfficherRendezVous();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        date.clear();
        time.clear();
        id_patient.clear();
        medecinComboBox.setValue(null);
        typeConsultationComboBox.setValue(null);
    }
}
