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
        String query = "SELECT id, specialite FROM medecin";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quick_rdv", "root", "");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String specialite = rs.getString("specialite");
                medecinMap.put(id, specialite);
            }
            medecinComboBox.getItems().addAll(medecinMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des médecins.");
        }
    }

    private void loadTypeConsultationOptions() {
        typeConsultationComboBox.setItems(FXCollections.observableArrayList("Consultation", "Téléconsultation"));
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
