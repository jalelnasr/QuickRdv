package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class GestionStatistiques {

    @FXML
    private TableColumn<?, ?> averageRatingCol;

    @FXML
    private TableColumn<?, ?> doctorNameCol;

    @FXML
    private TableColumn<?, ?> reclamationsCol;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<?> statsTable;

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void LoadReclamationPage(ActionEvent event) {

        try {
            System.out.println("Loading Reclamation Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) refreshButton.getScene().getWindow();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if something goes wrong
            showAlert("Error", "Failed to load the Reclamation page.");
        }


    }

    @FXML
    void LoadAvisPage(ActionEvent event) {

        try {
            System.out.println("Loading Avis Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAvis.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) refreshButton.getScene().getWindow();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if something goes wrong
            showAlert("Error", "Failed to load the Avis page.");
        }


    }


}
