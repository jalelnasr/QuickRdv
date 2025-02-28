package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Analyse;
import tn.esprit.services.ServiceAnalyse;

import java.io.IOException;
import java.util.Date;

public class AjouterAnalyse {

    @FXML
    private TextField id_medecin;

    @FXML
    private TextField id_patient;

    @FXML
    private TextField instructions;

    @FXML
    private TextField type;

    @FXML
    private TextField id_rendezvous; // Nouveau champ

    private ServiceAnalyse serviceAnalyse = new ServiceAnalyse();

    @FXML
    void submit(ActionEvent event) {
        try {
            // Vérification des champs vides
            if (id_medecin.getText().isEmpty() || id_patient.getText().isEmpty() ||
                    instructions.getText().isEmpty() || type.getText().isEmpty() ||
                    id_rendezvous.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            // Récupération et conversion des valeurs
            int medecinId = Integer.parseInt(id_medecin.getText());
            int patientId = Integer.parseInt(id_patient.getText());
            int rendezvousId = Integer.parseInt(id_rendezvous.getText());
            String instructionText = instructions.getText();
            String typeText = type.getText();

            // Validation des IDs
            if (medecinId <= 0 || patientId <= 0 || rendezvousId <= 0) {
                showAlert("Erreur", "Les IDs doivent être des nombres positifs.");
                return;
            }

            // Création de l'objet Analyse avec la date actuelle et idRendezVous
            Analyse analyse = new Analyse(new Date(), typeText, instructionText, patientId, medecinId, rendezvousId);

            // Ajout dans la base de données via le service
            serviceAnalyse.add(analyse);

            // Affichage d'un message de succès
            showAlert("Succès", "Analyse ajoutée avec succès !");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Les champs ID doivent être des nombres entiers.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue : " + e.getMessage());
        }
    }

    @FXML
    private void goToAfficherAnalyse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAnalyse.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) id_medecin.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        id_medecin.clear();
        id_patient.clear();
        instructions.clear();
        type.clear();
        id_rendezvous.clear();
    }
}