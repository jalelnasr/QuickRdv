package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import tn.esprit.models.Consultation;
import tn.esprit.services.ServiceConsultation;

import java.util.List;

public class AffichageConsultations {

    @FXML
    private ComboBox<String> searchTypeComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private StackPane searchContainer; // Use StackPane to overlay the search fields

    @FXML
    private VBox consultationsCardView;

    private ServiceConsultation serviceConsultation = new ServiceConsultation();

    @FXML
    public void initialize() {
        // Populate the ComboBox with search options
        searchTypeComboBox.getItems().addAll("Nom du médecin", "Date");

        // Set a default value for the ComboBox
        searchTypeComboBox.getSelectionModel().selectFirst();

        // Load all consultations for a specific patient initially
        int idPatient = 1; // Replace with the actual patient ID or fetch it dynamically
        loadConsultationsByPatientId(idPatient);

        // Add a listener to the ComboBox to toggle visibility of search fields
        searchTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("Nom du médecin")) {
                searchField.setVisible(true);
                datePicker.setVisible(false);
            } else if (newVal.equals("Date")) {
                searchField.setVisible(false);
                datePicker.setVisible(true);
            }
        });

        // Initially, set the visibility based on the default selected value
        searchField.setVisible(true);
        datePicker.setVisible(false);
    }

    // Load consultations by patient ID initially
    private void loadConsultationsByPatientId(int idPatient) {
        List<Consultation> consultations = serviceConsultation.getConsultationsByPatientId(idPatient);
        updateConsultationsCardView(consultations);
    }

    @FXML
    public void handleDynamicSearch() {
        String selectedSearchType = searchTypeComboBox.getValue();
        if (selectedSearchType != null) {
            List<Consultation> consultations = null;

            if (selectedSearchType.equals("Nom du médecin")) {
                String doctorName = searchField.getText();
                consultations = serviceConsultation.getConsultationsByDoctorName(doctorName);
            } else if (selectedSearchType.equals("Date")) {
                String selectedDate = datePicker.getValue() != null ? datePicker.getValue().toString() : "";
                consultations = serviceConsultation.getConsultationsByDate(selectedDate);
            }

            // Update the UI with the search results
            updateConsultationsCardView(consultations);
        }
    }

    @FXML
    public void handleSearch() {
        // This method can remain unchanged if you still want to allow manual search
        handleDynamicSearch();
    }

    private void updateConsultationsCardView(List<Consultation> consultations) {
        // Clear the current view
        consultationsCardView.getChildren().clear();

        // Add new consultation cards to the view
        for (Consultation consultation : consultations) {
            GridPane card = createConsultationCard(consultation);
            consultationsCardView.getChildren().add(card);
        }
    }

    private GridPane createConsultationCard(Consultation consultation) {
        GridPane card = new GridPane();
        card.setHgap(10);
        card.setVgap(5);
        card.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-color: #f9f9f9;");

        // Add consultation details to the card
        card.addRow(0, new Label("Médecin:"), new Label(consultation.getMedecinNom() + " " + consultation.getMedecinPrenom()));
        card.addRow(1, new Label("Spécialité:"), new Label(consultation.getMedecinSpecialite()));
        card.addRow(2, new Label("Date et Heure:"), new Label(consultation.getDateHeure().toString()));
        card.addRow(3, new Label("Type de Consultation:"), new Label(consultation.getTypeConsultation()));

        return card;
    }
}