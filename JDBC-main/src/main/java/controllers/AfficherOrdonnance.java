package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class AfficherOrdonnance {

    @FXML
    private ListView<Ordonnance> tableOrdonnances; // ✅ ListView au lieu de TableView

    private ServiceOrdonnance ordonnanceService = new ServiceOrdonnance();


    @FXML
    private ComboBox<String> medecinComboBox;
    @FXML
    private TextField idPatientField;
    @FXML
    private DatePicker datePrescription;
    @FXML
    private TextField instructionsField;
    @FXML
    private TextField medicamentField;
    @FXML
    private TextField quantiteField;
    @FXML
    private ListView<String> listMedicaments;
    @FXML
    private TextField txtMedicaments;
    private Map<String, Integer> medicamentsMap;

    @FXML
    public void initialize() {
        loadOrdonnances();
    }

    private void loadOrdonnances() {
        ObservableList<Ordonnance> ordonnances = FXCollections.observableArrayList(ordonnanceService.getAll());
        tableOrdonnances.setItems(ordonnances);

        // ✅ Définir l'affichage des éléments dans la liste
        tableOrdonnances.setCellFactory(param -> new ListCell<Ordonnance>() {
            @Override
            protected void updateItem(Ordonnance ordonnance, boolean empty) {
                super.updateItem(ordonnance, empty);
                if (empty || ordonnance == null) {
                    setText(null);
                } else {
                    setText("Patient: " + ordonnance.getPatientId() + " | Médicament: " + ordonnance.getMedicaments() + " | Date: " + ordonnance.getDatePrescription());
                }
            }
        });
    }

    // Méthode de modification d'ordonnance
    @FXML
    public void modifierOrdonnance() {
        try {
            // Vérifier si une ordonnance est sélectionnée
            Ordonnance selectedOrdonnance = tableOrdonnances.getSelectionModel().getSelectedItem();
            if (selectedOrdonnance == null) {
                showAlert("Sélectionnez une ordonnance à modifier !");
                return;
            }

            System.out.println("Ordonnance sélectionnée : " + selectedOrdonnance);

            // Charger la vue AjouterOrdonnance.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrdonnance.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur
            AjouterOrdonnance controller = loader.getController();
            if (controller == null) {
                System.out.println("ERREUR : Le contrôleur de AjouterOrdonnance.fxml est null !");
                return;
            }

            // Passer l'ordonnance sélectionnée au contrôleur
            controller.setOrdonnance(selectedOrdonnance);
            ordonnanceService.update(selectedOrdonnance);

            showAlert("Ordonnance mise à jour avec succès !");

            // Ouvrir une nouvelle fenêtre pour modifier l'ordonnance
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Ordonnance");
            stage.showAndWait(); // Attendre la fermeture de la fenêtre avant de continuer

            // Réactualiser la table après modification
            tableOrdonnances.refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String erreur, String s) {
    }


    // 🔹 Supprimer une ordonnance sélectionnée
    @FXML
    private void supprimerOrdonnance() {
        Ordonnance selectedOrdonnance = tableOrdonnances.getSelectionModel().getSelectedItem();
        if (selectedOrdonnance == null) {
            showAlert("Sélectionnez une ordonnance à supprimer !");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cette ordonnance ?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            ordonnanceService.delete(selectedOrdonnance);
            showAlert("Ordonnance supprimée avec succès !");
            loadOrdonnances();
        }
    }

    // 🔹 Fermer la fenêtre
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) tableOrdonnances.getScene().getWindow();
        stage.close();
    }

    // 🔹 Affichage d'une alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }






}

    // 🔹 Modifier une ordonnance sélectionnée
    // Assure-toi que c'est bien défini dans ton FXML





