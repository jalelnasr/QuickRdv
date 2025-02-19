package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;

import java.util.List;

public class AfficherRendezVous {

    @FXML
    private ListView<String> rendezVousListView;

    private ServiceRendezVous serviceRendezVous = new ServiceRendezVous();

    @FXML
    public void initialize() {
        loadRendezVous();
    }

    private void loadRendezVous() {
        List<RendezVous> rendezVousList = serviceRendezVous.getAll();
        ObservableList<String> items = FXCollections.observableArrayList();

        for (RendezVous rv : rendezVousList) {
            String rendezVousInfo = String.format("ID: %d, Date: %s, Patient ID: %d, Médecin: %s %s, Type Consultation: %d",
                    rv.getId(), rv.getDate(), rv.getPatientId(), rv.getMedecinNom(), rv.getMedecinPrenom(), rv.getTypeConsultationId());
            items.add(rendezVousInfo);
        }

        rendezVousListView.setItems(items);
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRendezVous.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) rendezVousListView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}