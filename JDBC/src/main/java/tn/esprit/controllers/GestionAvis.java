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
import tn.esprit.services.ServiceAvis;
import tn.esprit.models.Avis;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class GestionAvis {

    @FXML
    private TextArea Commentaire;

    @FXML
    private TextField Note;

    @FXML
    private ListView<Avis> avisListView;

    private ServiceAvis sa = new ServiceAvis();

    @FXML
    void LoadReclamationPage(ActionEvent event) {
        try {
            System.out.println("Loading Reclamation Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) Commentaire.getScene().getWindow();

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
    void Save(ActionEvent event) {
        sa.add(new Avis(1, 2, Commentaire.getText(), Integer.parseInt(Note.getText()), new Date()));

    }
    @FXML
    void Show(ActionEvent event) {

            // Fetch all avis from the database
            List<Avis> avisList = sa.getAll();

            // Clear the ListView and add the fetched avis
            avisListView.getItems().clear();
            avisListView.getItems().addAll(avisList);

    }

    }


