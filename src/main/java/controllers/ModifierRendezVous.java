package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ModifierRendezVous implements Initializable {
    @FXML
    private TextField date;

    @FXML
    private TextField id_patient;

    @FXML
    private ComboBox<String> medecinComboBox;

    @FXML
    private TextField type_consultation;

    private ServiceRendezVous ps = new ServiceRendezVous();
    private Map<Integer, String> medecinMap = new HashMap<>();
    private RendezVous currentRendezVous;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedecins();
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

            // Populate the ComboBox with doctor specialties
            medecinComboBox.getItems().addAll(medecinMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des médecins.");
        }
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.currentRendezVous = rendezVous;

        // Set the date field
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(rendezVous.getDate()));

        // Set the patient ID field
        id_patient.setText(String.valueOf(rendezVous.getPatientId()));

        // Set the consultation type field
        type_consultation.setText(String.valueOf(rendezVous.getTypeConsultationId()));

        // Set the selected doctor in the ComboBox
        String doctorSpecialite = medecinMap.get(rendezVous.getMedecinId());
        if (doctorSpecialite != null) {
            medecinComboBox.setValue(doctorSpecialite);
        }
    }

    @FXML
    void modifier() {
        try {
            if (date.getText().isEmpty() || id_patient.getText().isEmpty() ||
                    medecinComboBox.getValue() == null || type_consultation.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Parse the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.util.Date parsedDate = dateFormat.parse(date.getText());

            // Parse the patient ID
            int patientId = Integer.parseInt(id_patient.getText());

            // Validate the patient ID
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

            // Parse the consultation type
            int consultationType = Integer.parseInt(type_consultation.getText());

            // Update the current RendezVous object
            currentRendezVous.setDate(parsedDate);
            currentRendezVous.setPatientId(patientId);
            currentRendezVous.setMedecinId(medecinId);
            currentRendezVous.setTypeConsultationId(consultationType);

            // Save the changes to the database
            ps.update(currentRendezVous);

            // Show success message
            showAlert("Succès", "Rendez-vous modifié avec succès !");
        } catch (ParseException e) {
            showAlert("Erreur de format", "Le format de la date est invalide. Utilisez le format YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Les champs ID patient et type de consultation doivent être des nombres entiers.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite lors de la modification du rendez-vous: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}