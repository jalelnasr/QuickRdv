package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    Reclamation selectedReclamation = null;  // Holds the Avis being edited


    @FXML
    private Button sendButton;



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
        if (selectedReclamation != null && sendButton.getText().equals("Update") ) {
            // If an Avis is selected for update
            selectedReclamation.setSujet(Sujet.getText());  // Get the updated commentaire from the TextField
            selectedReclamation.setDescription(Description.getText());
            sr.update(selectedReclamation);
            ReclamationListView.refresh();  // Refresh the ListView to show updated items
            selectedReclamation = null;  // Reset the selectedAvis
            Sujet.clear();  // Clear the commentaire TextField
            Description.clear();  // Clear the note TextField
            sendButton.setText("Send");
            return;
        }
        if (sendButton.getText().equals("Send")) {  // If button says "Save", add new Avis
            sr.add(new Reclamation(1, 1, Sujet.getText(), Description.getText(), new Date()));
        }


    }

    @FXML
    void Show(ActionEvent event) {
        List<Reclamation> ReclamationList = sr.getAll();

        // Clear the ListView and add the fetched avis
        ReclamationListView.getItems().clear();
        ReclamationListView.getItems().addAll(ReclamationList);
        ReclamationListView.setCellFactory(param -> new ListCell<Reclamation>() {
            @Override
            protected void updateItem(Reclamation reclamation, boolean empty) {
                super.updateItem(reclamation, empty);

                if (empty || reclamation == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label idLabel = new Label("ID: " + reclamation.getId());
                    Label utilisateurIdLabel = new Label("Utilisateur ID: " + reclamation.getUtilisateur_id());
                    Label rendezVousIdLabel = new Label("Rendez-vous ID: " + reclamation.getRendez_vous_id());
                    Label sujetLabel = new Label("Sujet: " + reclamation.getSujet());
                    Label descriptionLabel = new Label("Description: " + reclamation.getDescription());
                    Label dateReclamationLabel = new Label("Date: " + reclamation.getDate_reclamation().toString());

                    int currentUserId = 1; // Replace with actual logged-in user ID

                    if (reclamation.getUtilisateur_id() == currentUserId) {
                        Button deleteButton = new Button("Delete");
                        Button updateButton = new Button("Update");


                        deleteButton.setOnAction(event -> {
                            sr.delete(reclamation); // Delete from database
                            ReclamationListView.getItems().remove(reclamation); // Remove from UI
                        });

                        updateButton.setOnAction(event -> {
                            selectedReclamation = reclamation;  // Set the selected Avis to the one the user clicked
                            Sujet.setText(reclamation.getSujet());  // Set the commentaire TextField
                            Description.setText(reclamation.getDescription());  // Set the note TextField
                            sendButton.setText("Update");  // Change Save button text to "Update"
                        });

                        hbox.getChildren().addAll(idLabel, utilisateurIdLabel, rendezVousIdLabel, sujetLabel, descriptionLabel, dateReclamationLabel, deleteButton , updateButton);
                    } else {
                        hbox.getChildren().addAll(idLabel, utilisateurIdLabel, rendezVousIdLabel, sujetLabel, descriptionLabel, dateReclamationLabel);
                    }

                    setGraphic(hbox);
                }
            }
        });

    }

}
