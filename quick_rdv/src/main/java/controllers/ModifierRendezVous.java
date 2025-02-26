package controllers;

import javafx.collections.FXCollections;
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
    private ComboBox<String> typeConsultationComboBox; // Remplacé TextField par ComboBox

    private ServiceRendezVous ps = new ServiceRendezVous();
    private Map<Integer, String> medecinMap = new HashMap<>();
    private RendezVous currentRendezVous;

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

    public void setRendezVous(RendezVous rendezVous) {
        this.currentRendezVous = rendezVous;
        date.setText(new SimpleDateFormat("yyyy-MM-dd").format(rendezVous.getDate()));
        id_patient.setText(String.valueOf(rendezVous.getPatientId()));
        String doctorSpecialite = medecinMap.get(rendezVous.getMedecinId());
        if (doctorSpecialite != null) {
            medecinComboBox.setValue(doctorSpecialite);
        }
        // Pré-sélectionner le type de consultation
        typeConsultationComboBox.setValue(rendezVous.getTypeConsultationId() == 1 ? "Consultation" : "Téléconsultation");
    }

    @FXML
    void modifier() {
        try {
            if (date.getText().isEmpty() || id_patient.getText().isEmpty() ||
                    medecinComboBox.getValue() == null || typeConsultationComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.util.Date parsedDate = dateFormat.parse(date.getText());

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

            // Convertir le texte du ComboBox en ID numérique
            int consultationType = typeConsultationComboBox.getValue().equals("Consultation") ? 1 : 2;

            currentRendezVous.setDate(parsedDate);
            currentRendezVous.setPatientId(patientId);
            currentRendezVous.setMedecinId(medecinId);
            currentRendezVous.setTypeConsultationId(consultationType);

            ps.update(currentRendezVous);

            showAlert("Succès", "Rendez-vous modifié avec succès !");
        } catch (ParseException e) {
            showAlert("Erreur de format", "Le format de la date est invalide. Utilisez YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "L’ID patient doit être un nombre entier.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}