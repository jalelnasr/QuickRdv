
package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import tn.esprit.models.Assurance;
import tn.esprit.services.ServiceAssurance;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

public class AjoutAssurance {

    @FXML
    private TextField nomField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private DatePicker dateDebutPicker, dateFinPicker;

    private ServiceAssurance serviceAssurance = new ServiceAssurance();

    @FXML
    void initialize() {
        // Initialize ComboBox with types or any data you have for "type"
        typeComboBox.getItems().addAll("Étatique", "Privée"); // Add "Étatique" and "Privée" as options for type
    }

    @FXML
    void SaveAssurance(ActionEvent event) {
        try {
            String nom = nomField.getText().trim();
            String type = typeComboBox.getValue();

            // Validate dates
            if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
                throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires.");
            }

            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            // Check if dateDebut is before dateFin
            if (!dateDebut.isBefore(dateFin)) {
                throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
            }

            if (nom.isEmpty() || type == null) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            // Create Assurance object with id_PatientAs set to 1
            Assurance newAssurance = new Assurance(nom, type, dateDebut, dateFin, 1); // Use the correct constructor
            serviceAssurance.add(newAssurance);

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Assurance ajoutée avec succès !");
            clearForm();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur de validation", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de l'assurance.");
        }
    }

    @FXML
    void goToAssurancesList(ActionEvent event) {
        try {
            // Load the AffichageAssurances.fxml for viewing the list of assurances
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AffichageAssurances.fxml"));
            Parent root = loader.load();

            // Get current stage and set the new scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la navigation vers la liste des assurances.");
        }
    }

    private void clearForm() {
        nomField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
