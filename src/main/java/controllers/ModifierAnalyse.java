package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.models.Analyse;
import tn.esprit.services.ServiceAnalyse;

public class ModifierAnalyse {

    @FXML
    private TextField id_patient;

    @FXML
    private TextField id_medecin;

    @FXML
    private TextField type;

    @FXML
    private TextField instructions;

    @FXML
    private TextField id_rendezvous; // Nouveau champ

    private ServiceAnalyse serviceAnalyse = new ServiceAnalyse();
    private Analyse currentAnalyse;

    public void setAnalyse(Analyse analyse) {
        this.currentAnalyse = analyse;
        id_patient.setText(String.valueOf(analyse.getPatientId()));
        id_medecin.setText(String.valueOf(analyse.getMedecinId()));
        type.setText(analyse.getType());
        instructions.setText(analyse.getInstructions());
        id_rendezvous.setText(String.valueOf(analyse.getIdRendezVous()));
    }

    @FXML
    void modifier() {
        try {
            if (id_patient.getText().isEmpty() || id_medecin.getText().isEmpty() ||
                    type.getText().isEmpty() || instructions.getText().isEmpty() ||
                    id_rendezvous.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            int patientId = Integer.parseInt(id_patient.getText());
            int medecinId = Integer.parseInt(id_medecin.getText());
            int rendezvousId = Integer.parseInt(id_rendezvous.getText());

            if (patientId <= 0) {
                showAlert("Erreur", "ID patient invalide.");
                return;
            }
            if (medecinId <= 0) {
                showAlert("Erreur", "ID médecin invalide.");
                return;
            }
            if (rendezvousId <= 0) {
                showAlert("Erreur", "ID rendez-vous invalide.");
                return;
            }

            currentAnalyse.setPatientId(patientId);
            currentAnalyse.setMedecinId(medecinId);
            currentAnalyse.setType(type.getText());
            currentAnalyse.setInstructions(instructions.getText());
            currentAnalyse.setIdRendezVous(rendezvousId);

            serviceAnalyse.update(currentAnalyse);

            showAlert("Succès", "Analyse modifiée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Les champs ID doivent être des nombres entiers.");
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