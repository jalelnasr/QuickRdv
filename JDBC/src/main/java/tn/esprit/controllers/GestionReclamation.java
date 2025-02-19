package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.models.Avis;
import tn.esprit.services.ServiceReclamation;
import tn.esprit.models.Reclamation;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class GestionReclamation {


    @FXML
    private TextArea Description;

    @FXML
    private ListView<Reclamation> ReclamationListView ;

    @FXML
    private TextField Sujet;
    private ServiceReclamation sr = new ServiceReclamation();



    @FXML
    void LoadAvisPage(ActionEvent event) {
        try {
            System.out.println("Loading Avis Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAvis.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) Description.getScene().getWindow();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if something goes wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load the Reclamation page.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void Send(ActionEvent event) {
        sr.add(new Reclamation(1, 1, Sujet.getText(), Description.getText(), new Date()));

    }

    @FXML
    void Show(ActionEvent event) {
        List<Reclamation> ReclamationList = sr.getAll();

        // Clear the ListView and add the fetched avis
        ReclamationListView.getItems().clear();
        ReclamationListView.getItems().addAll(ReclamationList);

    }

}
