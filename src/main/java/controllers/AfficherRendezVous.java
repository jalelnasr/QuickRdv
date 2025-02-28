package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;

import java.io.IOException;
import java.util.Optional;

public class AfficherRendezVous {
    @FXML
    private ListView<RendezVous> rendezVousListView;

    private ServiceRendezVous serviceRendezVous = new ServiceRendezVous();

    @FXML
    public void initialize() {
        rendezVousListView.setCellFactory(listView -> new ListCell<RendezVous>() {
            @Override
            protected void updateItem(RendezVous rv, boolean empty) {
                super.updateItem(rv, empty);
                if (empty || rv == null) {
                    setText(null);
                } else {
                    setText("Date: " + rv.getDate() +
                            " | Start: " + rv.getStartTime() +
                            " | End: " + rv.getEndTime() +
                            " | Médecin: " + rv.getMedecinNom() + " " + rv.getMedecinPrenom() +
                            " | Patient: " + rv.getPatientNom() + " " + rv.getPatientPrenom());
                }
            }
        });
        refreshRendezVousList();
    }

    private void refreshRendezVousList() {

        rendezVousListView.getItems().clear();
        rendezVousListView.getItems().addAll(serviceRendezVous.getAll());


    }

    @FXML
    private void modifierRendezVous() {
        RendezVous selectedRendezVous = rendezVousListView.getSelectionModel().getSelectedItem();
        if (selectedRendezVous != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierRendezVous.fxml"));
                Parent root = loader.load();

                ModifierRendezVous controller = loader.getController();
                controller.setRendezVous(selectedRendezVous);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Rendez-vous");
                stage.showAndWait(); // Attendre que la fenêtre de modification se ferme

                // Rafraîchir la liste après modification
                refreshRendezVousList();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de modifierRendezVous.fxml: " + e.getMessage());
            }
        } else {
            System.out.println("Aucun rendez-vous sélectionné.");
        }
    }

    @FXML
    private void supprimerRendezVous() {
        RendezVous selectedRendezVous = rendezVousListView.getSelectionModel().getSelectedItem();
        if (selectedRendezVous != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            System.out.println("Selected RendezVous ID: " + selectedRendezVous.getId());
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce rendez-vous ?");
            alert.setContentText("Cette action est irréversible.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceRendezVous.delete(selectedRendezVous);
                System.out.println("Rendez-vous supprimé avec succès !");
                refreshRendezVousList();
            }
        } else {
            System.out.println("Aucun rendez-vous sélectionné.");
        }
    }
}