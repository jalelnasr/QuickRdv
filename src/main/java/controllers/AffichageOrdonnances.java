package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AffichageOrdonnances {
    @FXML
    private ListView<String> ordonnancesListView; // ListView to display ordonnances
    @FXML
    private ComboBox<String> searchTypeComboBox; // ComboBox for search type
    @FXML
    private TextField searchField; // TextField for doctor's name
    @FXML
    private DatePicker datePicker; // DatePicker for date search

    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();

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

        // Load all ordonnances initially
        loadOrdonnances(serviceOrdonnance.getAllOrdonnances());
    }

    @FXML
    public void handleSearch() {
        String searchType = searchTypeComboBox.getValue();
        List<Ordonnance> filteredOrdonnances;

        if (searchType.equals("Date")) {
            // Get the selected date from the DatePicker
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate != null) {
                // Format the date as a string (e.g., "2023-10-15")
                String dateString = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                // Search by date
                filteredOrdonnances = serviceOrdonnance.getOrdonnancesByDate(dateString);
            } else {
                // If no date is selected, show all ordonnances
                filteredOrdonnances = serviceOrdonnance.getAllOrdonnances();
            }
        } else if (searchType.equals("Nom du médecin")) {
            // Search by doctor's name
            String doctorName = searchField.getText().trim();
            filteredOrdonnances = serviceOrdonnance.getOrdonnancesByDoctorName(doctorName);
        } else {
            // If no valid search type, show all ordonnances
            filteredOrdonnances = serviceOrdonnance.getAllOrdonnances();
        }

        // Display the filtered ordonnances
        loadOrdonnances(filteredOrdonnances);
    }

    private void loadOrdonnances(List<Ordonnance> ordonnances) {
        // Clear the existing items in the ListView
        ordonnancesListView.getItems().clear();

        // Add ordonnances to the ListView
        for (Ordonnance ordonnance : ordonnances) {
            ordonnancesListView.getItems().add(
                    "Doctor: " + ordonnance.getMedecinNom() + " " + ordonnance.getMedecinPrenom() + " (" + ordonnance.getMedecinSpecialite() + ")\n" +
                            "Medicaments: " + ordonnance.getMedicaments() + "\n" +
                            "Date: " + ordonnance.getDatePrescription() + "\n" +
                            "Instructions: " + ordonnance.getInstructions() + "\n" +
                            "Statut: " + ordonnance.getStatut()
            );
        }
    }
}