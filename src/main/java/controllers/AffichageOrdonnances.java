package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.util.List;

public class AffichageOrdonnances {
    @FXML
    private ListView<String> ordonnancesListView;

    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();

    public void initialize() {
        // Fetch ordonnances for a specific patient (e.g., patientId = 1)
        int patientId = 1; // Replace with the actual patient ID
        List<Ordonnance> ordonnances = serviceOrdonnance.getOrdonnancesByPatientId(patientId);

        // Display ordonnances in the ListView
        for (Ordonnance ordonnance : ordonnances) {
            ordonnancesListView.getItems().add(
                    ordonnance.getDatePrescription() + " - " + // Updated field name
                            ordonnance.getMedicaments() + " - " +
                            "Statut: " + ordonnance.getStatut()
            );
        }
    }
}