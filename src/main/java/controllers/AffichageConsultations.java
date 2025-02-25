package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import tn.esprit.models.Consultation;
import tn.esprit.services.ServiceConsultation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AffichageConsultations {
    @FXML
    private VBox consultationsCardView; // VBox to hold the card views
    @FXML
    private ComboBox<String> searchTypeComboBox; // ComboBox for search type
    @FXML
    private TextField searchField; // TextField for doctor's name
    @FXML
    private DatePicker datePicker; // DatePicker for date search

    private ServiceConsultation serviceConsultation = new ServiceConsultation();

    @FXML
    public void initialize() {
        // Initialize the search type ComboBox
        searchTypeComboBox.getItems().addAll("Date", "Nom du médecin");
        searchTypeComboBox.setValue("Date"); // Default selection

        // Add listener to handle search type changes
        searchTypeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Date")) {
                // Show DatePicker and hide TextField
                datePicker.setVisible(true);
                searchField.setVisible(false);
            } else if (newValue.equals("Nom du médecin")) {
                // Show TextField and hide DatePicker
                searchField.setVisible(true);
                datePicker.setVisible(false);
            }
        });

        // Load all consultations initially
        loadConsultations(serviceConsultation.getConsultationsByPatientId(1)); // Replace with actual patient ID
    }

    @FXML
    public void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        List<Consultation> filteredConsultations;

        if (searchType.equals("Date")) {
            // Get the selected date from the DatePicker
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                // Format the date as a string (e.g., "2023-10-15")
                String dateString = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Search by date
                filteredConsultations = serviceConsultation.getConsultationsByDate(dateString);
            } else {
                // If no date is selected, show all consultations
                filteredConsultations = serviceConsultation.getConsultationsByPatientId(1); // Replace with actual patient ID
            }
        } else if (searchType.equals("Nom du médecin")) {
            // Search by doctor's name
            String doctorName = searchField.getText().trim();
            filteredConsultations = serviceConsultation.getConsultationsByDoctorName(doctorName);
        } else {
            // If no valid search type, show all consultations
            filteredConsultations = serviceConsultation.getConsultationsByPatientId(1); // Replace with actual patient ID
        }

        // Display the filtered consultations
        loadConsultations(filteredConsultations);
    }

    private void loadConsultations(List<Consultation> consultations) {
        // Clear the existing cards
        consultationsCardView.getChildren().clear();

        // Add a card for each consultation
        for (Consultation consultation : consultations) {
            HBox card = createConsultationCard(consultation);
            consultationsCardView.getChildren().add(card);
        }
    }

    private HBox createConsultationCard(Consultation consultation) {
        // Create a card layout using HBox
        HBox card = new HBox(10);
        card.setStyle("-fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Add consultation details to the card
        VBox details = new VBox(5);
        details.getChildren().add(new Label("Date: " + consultation.getDateHeure()));
        details.getChildren().add(new Label("Type: " + consultation.getTypeConsultation()));
        details.getChildren().add(new Label("Médecin: " + consultation.getMedecinNom() + " " + consultation.getMedecinPrenom()));
        details.getChildren().add(new Label("Spécialité: " + consultation.getMedecinSpecialite()));

        card.getChildren().add(details);
        return card;
    }
}