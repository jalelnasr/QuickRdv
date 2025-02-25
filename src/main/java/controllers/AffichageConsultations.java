package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import tn.esprit.models.Consultation;
import tn.esprit.services.ServiceConsultation;

import java.util.List;

public class AffichageConsultations {
    @FXML
    private ListView<String> consultationsListView;

    private ServiceConsultation serviceConsultation = new ServiceConsultation();

    public void initialize() {
        // Fetch consultations for a specific patient (e.g., patientId = 1)
        int patientId = 1; // Replace with the actual patient ID
        List<Consultation> consultations = serviceConsultation.getConsultationsByPatientId(patientId);

        // Display consultations in the ListView
        for (Consultation consultation : consultations) {
            consultationsListView.getItems().add(
                    consultation.getDateHeure() + " - " +
                            consultation.getTypeConsultation() + " - " +
                            "Medecin ID: " + consultation.getIdMedecin()
            );
        }
    }
}