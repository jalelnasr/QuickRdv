package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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

    @FXML
    private Button saveButton;

    private ServiceAvis sa = new ServiceAvis();
    private final int currentUserId = 1;
    Avis selectedAvis = null;  // Holds the Avis being edited
// Replace this with the actual logged-in user ID


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
        if (selectedAvis != null && saveButton.getText().equals("Update") ) {
            // If an Avis is selected for update
            selectedAvis.setCommentaire(Commentaire.getText());  // Get the updated commentaire from the TextField
            selectedAvis.setNote(Integer.parseInt(Note.getText()));
            sa.update(selectedAvis);
            avisListView.refresh();  // Refresh the ListView to show updated items
            selectedAvis = null;  // Reset the selectedAvis
            Commentaire.clear();  // Clear the commentaire TextField
            Note.clear();  // Clear the note TextField
            saveButton.setText("Save");
            return;
        }
            if (saveButton.getText().equals("Save")) {  // If button says "Save", add new Avis
                sa.add(new Avis(1, 2, Commentaire.getText(), Integer.parseInt(Note.getText()), new Date()));
            }




    }
    @FXML
    void Show(ActionEvent event) {

            // Fetch all avis from the database
            List<Avis> avisList = sa.getAll();

            // Clear the ListView and add the fetched avis
            avisListView.getItems().clear();
            avisListView.getItems().addAll(avisList);
        avisListView.setCellFactory(param -> new ListCell<Avis>() {
            @Override
            protected void updateItem(Avis avis, boolean empty) {
                super.updateItem(avis, empty);

                if (empty || avis == null) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label idLabel = new Label("ID: " + avis.getId());
                    Label patientIdLabel = new Label("Patient ID: " + avis.getPatient_id());
                    Label medecinIdLabel = new Label("Medecin ID: " + avis.getMedecin_id());
                    Label commentaireLabel = new Label("Commentaire: " + avis.getCommentaire());
                    Label noteLabel = new Label("Note: " + avis.getNote());
                    Label dateAvisLabel = new Label("Date: " + avis.getDate_avis().toString());
                    int currentUserId = 1; // Replace with actual logged-in user ID

                    if (avis.getPatient_id() == currentUserId) {
                        Button deleteButton = new Button("Delete");
                        Button updateButton = new Button("Update");


                        deleteButton.setOnAction(event -> {
                            sa.delete(avis); // Delete from database
                            avisListView.getItems().remove(avis); // Remove from UI
                        });

                        updateButton.setOnAction(event -> {
                            selectedAvis = avis;  // Set the selected Avis to the one the user clicked
                            Commentaire.setText(avis.getCommentaire());  // Set the commentaire TextField
                            Note.setText(String.valueOf(avis.getNote()));  // Set the note TextField
                            saveButton.setText("Update");  // Change Save button text to "Update"
                        });

                        hbox.getChildren().addAll(idLabel, patientIdLabel, medecinIdLabel, commentaireLabel, noteLabel, dateAvisLabel, deleteButton , updateButton);
                    } else {
                        hbox.getChildren().addAll(idLabel, patientIdLabel, medecinIdLabel, commentaireLabel, noteLabel, dateAvisLabel);
                    }

                    setGraphic(hbox);
                }
            }
        });

    }

    }


