package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import tn.esprit.services.ServiceMedicament;
import tn.esprit.models.Medicament;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AjouterMedicament {

    @FXML
    private TextField nomMedicamentField;

    @FXML
    private TextField stockMedicamentField;

    @FXML
    private TextField pharmacienIdField;

    @FXML
    private ListView<Medicament> medicamentListView;

    private final ServiceMedicament serviceMedicament = new ServiceMedicament();

    @FXML
    public void initialize() {
        loadMedicaments();
    }

    // Charger les médicaments dans la ListView
    private void loadMedicaments() {
        List<Medicament> medicaments = serviceMedicament.getAll();
        ObservableList<Medicament> observableList = FXCollections.observableArrayList(medicaments);
        medicamentListView.setItems(observableList);

        medicamentListView.setCellFactory(param -> new ListCell<Medicament>() {
            @Override
            protected void updateItem(Medicament medicament, boolean empty) {
                super.updateItem(medicament, empty);
                if (empty || medicament == null) {
                    setText(null);
                } else {
                    setText(medicament.getNom() + " - Stock: " + medicament.getStock() + " - Pharmacien: " + medicament.getPharmacienId());
                }
            }
        });

        // Ajouter un listener pour afficher les détails lorsqu'on sélectionne un élément
        medicamentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showDetails(newValue));
    }

    // Ajouter un médicament
    @FXML
    void ajouterMedicament(ActionEvent event) {
        try {
            if (!allFieldsFilled()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            String nom = nomMedicamentField.getText();
            int stock = Integer.parseInt(stockMedicamentField.getText());
            int pharmacienId = Integer.parseInt(pharmacienIdField.getText());

            Medicament medicament = new Medicament(nom, stock, pharmacienId);
            serviceMedicament.add(medicament);

            showAlert("Succès", "Médicament ajouté avec succès !");
            clearFields();
            loadMedicaments();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Stock et ID pharmacien doivent être des nombres.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout du médicament.");
            e.printStackTrace();
        }
    }

    // Modifier un médicament
    @FXML
    void modifierMedicament(ActionEvent event) {
        Medicament selectedMedicament = medicamentListView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Erreur", "Veuillez sélectionner un médicament à modifier.");
            return;
        }

        try {
            if (!allFieldsFilled()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            selectedMedicament.setNom(nomMedicamentField.getText());
            selectedMedicament.setStock(Integer.parseInt(stockMedicamentField.getText()));
            selectedMedicament.setPharmacienId(Integer.parseInt(pharmacienIdField.getText()));

            serviceMedicament.update(selectedMedicament);

            showAlert("Succès", "Médicament modifié avec succès !");
            clearFields();
            loadMedicaments();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Stock et ID pharmacien doivent être des nombres.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la modification du médicament.");
            e.printStackTrace();
        }
    }

    // Supprimer un médicament
    @FXML
    void supprimerMedicament(ActionEvent event) {
        Medicament selectedMedicament = medicamentListView.getSelectionModel().getSelectedItem();
        if (selectedMedicament == null) {
            showAlert("Erreur", "Veuillez sélectionner un médicament à supprimer.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer le médicament ?");
        alert.setContentText("Voulez-vous vraiment supprimer ce médicament ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            serviceMedicament.delete(selectedMedicament);
            showAlert("Succès", "Médicament supprimé avec succès !");
            clearFields();
            loadMedicaments();
        }
    }

    // Afficher les détails du médicament sélectionné
    private void showDetails(Medicament medicament) {
        if (medicament != null) {
            nomMedicamentField.setText(medicament.getNom());
            stockMedicamentField.setText(String.valueOf(medicament.getStock()));
            pharmacienIdField.setText(String.valueOf(medicament.getPharmacienId()));
        }
    }

    // Vérifier si tous les champs sont remplis
    private boolean allFieldsFilled() {
        return !nomMedicamentField.getText().isEmpty() &&
                !stockMedicamentField.getText().isEmpty() &&
                !pharmacienIdField.getText().isEmpty();
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialiser les champs
    @FXML
    void resetFields(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        nomMedicamentField.clear();
        stockMedicamentField.clear();
        pharmacienIdField.clear();
    }



}
