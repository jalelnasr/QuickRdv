package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Assurance;
import tn.esprit.services.ServiceAssurance;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AjoutAssurance {

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextField montantField;

    @FXML
    private TextField nomField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ListView<Assurance> assuranceListView;

    private ServiceAssurance serviceAssurance = new ServiceAssurance();

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshAssurancesList();

        assuranceListView.setCellFactory(param -> new ListCell<Assurance>() {
            @Override
            protected void updateItem(Assurance assurance, boolean empty) {
                super.updateItem(assurance, empty);
                if (empty || assurance == null) {
                    setText(null);
                } else {
                    setText("{ ID = " + assurance.getIdAssurance() + " | Nom = " + assurance.getNom() +
                            " | Type = " + assurance.getType() + " | Date Début = " + assurance.getDateDebut() +
                            " | Date Fin = " + assurance.getDateFin() + " | Montant Couvert = " + assurance.getMontantCouvert() + " } ");
                }
            }
        });
    }

    @FXML
    void SaveAssurance(ActionEvent event) {
        try {
            String nom = nomField.getText().trim();
            String type = typeComboBox.getValue();
            float montantCouvert = Float.parseFloat(montantField.getText().trim());

            if (dateDebutPicker.getValue() == null || dateFinPicker.getValue() == null) {
                throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires.");
            }
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (nom.isEmpty() || type == null) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            Assurance newAssurance = new Assurance(nom, type, dateDebut, dateFin, montantCouvert);
            serviceAssurance.add(newAssurance);

            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Assurance ajoutée avec succès !");
            refreshAssurancesList();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format invalide", "Veuillez entrer un montant valide.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout de l'assurance.");
        }
    }

    @FXML
    void UpdateAssurance(ActionEvent event) {
        Assurance selectedAssurance = assuranceListView.getSelectionModel().getSelectedItem();
        if (selectedAssurance == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une assurance à modifier.");
            return;
        }

        try {
            String nom = nomField.getText().trim();
            String type = typeComboBox.getValue();
            float montantCouvert = Float.parseFloat(montantField.getText().trim());
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (nom.isEmpty() || type == null || dateDebut == null || dateFin == null) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            selectedAssurance.setNom(nom);
            selectedAssurance.setType(type);
            selectedAssurance.setDateDebut(dateDebut);
            selectedAssurance.setDateFin(dateFin);
            selectedAssurance.setMontantCouvert(montantCouvert);

            serviceAssurance.update(selectedAssurance);
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Assurance mise à jour avec succès !");
            refreshAssurancesList();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format invalide", "Veuillez entrer un montant valide.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour de l'assurance.");
        }
    }

    @FXML
    void DeleteAssurance(ActionEvent event) {
        Assurance selectedAssurance = assuranceListView.getSelectionModel().getSelectedItem();

        if (selectedAssurance == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner une assurance à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette assurance ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                serviceAssurance.delete(selectedAssurance); // Pass the object instead of ID
                showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "L'assurance a été supprimée avec succès.");
                refreshAssurancesList();
            }
        });
    }


    private void refreshAssurancesList() {
        List<Assurance> updatedAssurances = serviceAssurance.getAll();
        ObservableList<Assurance> observableList = FXCollections.observableArrayList(updatedAssurances);
        assuranceListView.setItems(observableList);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}