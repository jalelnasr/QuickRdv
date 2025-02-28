package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;

import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ModifierRendezVous implements Initializable {
    @FXML
    private TextField id_patient;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private ComboBox<String> medecinComboBox;

    @FXML
    private ComboBox<String> typeConsultationComboBox;

    private ServiceRendezVous ps = new ServiceRendezVous();
    private RendezVous currentRendezVous;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedecins();
        loadTypeConsultationOptions();
        LocalDate today = LocalDate.now();

        // Set the DatePicker to allow only dates after today
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        // Disable dates before today
                        if (item.isBefore(today)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Optional: Style for disabled dates
                        }
                    }
                };
            }
        });
    }

    private void loadMedecins() {
        String query = "SELECT u.nom,u.prenom, m.specialite " +
                "FROM utilisateur u " +
                "JOIN medecin m ON u.id = m.id " +
                "WHERE u.role = 'medecin'"; // Filter only doctors

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chrayta", "root", "");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            medecinComboBox.getItems().clear(); // Clear previous items to avoid duplication

            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom"); // Get name from utilisateur
                String specialite = rs.getString("specialite"); // Get specialty from medecin

                // Format: "Doctor Name - Specialty"
                String displayText = "Dr. " + nom + " " + prenom + " - " + specialite;

                medecinComboBox.getItems().add(displayText);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des médecins.");
        }
    }

    private int getMedecinIdByNomPrenom(String nom, String prenom) {
        String query = "SELECT m.id FROM medecin m " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE u.nom = ? AND u.prenom = ? AND u.role = 'medecin'";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chrayta", "root", "");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Retourne -1 si non trouvé
    }

    private void loadAvailableTimes(String selectedDate, int doctorId) {
        // Define the working hours for the doctor
        // Assuming a fixed working time: 9 AM to 5 PM (1-hour intervals)
        LocalTime startWorkingHour = LocalTime.of(9, 0);
        LocalTime endWorkingHour = LocalTime.of(17, 0);

        // Create a list of all possible time slots
        List<String> allTimeSlots = new ArrayList<>();
        for (LocalTime time = startWorkingHour; time.isBefore(endWorkingHour); time = time.plusHours(1)) {
            LocalTime nextHour = time.plusHours(1);
            // Create time slot as a range (e.g., "9:00-10:00")
            allTimeSlots.add(time.toString() + "-" + nextHour.toString());
        }

        // Get the list of already reserved time slots for this doctor on the selected date
        String query = "SELECT start_time, end_time FROM rendezvous WHERE medecinId = ? AND DATE(start_time) = ?";

        List<String> reservedSlots = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chrayta", "root", "");
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, doctorId); // Set the doctor’s ID
            stmt.setString(2, selectedDate); // Set the selected date

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp startTime = rs.getTimestamp("start_time");
                Timestamp endTime = rs.getTimestamp("end_time");

                // Convert the times to LocalTime
                LocalTime reservedStart = startTime.toLocalDateTime().toLocalTime();
                LocalTime reservedEnd = endTime.toLocalDateTime().toLocalTime();

                // For simplicity, assume the time slots are hourly, check if the start or end time is inside our list
                // of available time slots (1-hour intervals).
                for (LocalTime time = reservedStart; time.isBefore(reservedEnd); time = time.plusHours(1)) {
                    LocalTime nextHour = time.plusHours(1);
                    reservedSlots.add(time.toString() + "-" + nextHour.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la récupération des créneaux réservés.");
        }

        // Remove the reserved time slots from the all available time slots
        allTimeSlots.removeAll(reservedSlots);

        // Update the ComboBox with available time slots
        timeComboBox.setItems(FXCollections.observableArrayList(allTimeSlots));
    }

    @FXML
    private void onDoctorAndDateSelected(ActionEvent event) {
        // Get the selected date from the DatePicker and convert it to string
        if (medecinComboBox.getValue() == null || datePicker.getValue() == null) {
            return; // Don't do anything if one is still null
        }
        String selectedDate = datePicker.getValue().toString();

        // Get the selected doctor from the ComboBox
        String selectedDoctor = medecinComboBox.getValue();

        // Split the doctor's name and specialty from the displayed text
        String[] doctorNameParts = selectedDoctor.split(" - ")[0].split(" ", 3);

        // Make sure we have at least 3 parts: "Dr.", first name, and last name
        if (doctorNameParts.length < 3) {
            showAlert("Erreur", "Nom du médecin invalide.");
            return;
        }

        // Extract the doctor’s first and last name (ignoring the "Dr." part)
        String doctorPrenom = doctorNameParts[2];  // First name
        String doctorNom = doctorNameParts[1];     // Last name

        // Use your existing method to get the doctor's ID by their name
        int doctorId = getMedecinIdByNomPrenom(doctorNom, doctorPrenom);

        // Check if the doctor's ID was found
        if (doctorId == -1) {
            showAlert("Erreur", "Médecin non trouvé.");
            return;
        }

        // Load available times for the selected doctor and date
        loadAvailableTimes(selectedDate, doctorId);
    }



    private void loadTypeConsultationOptions() {
        typeConsultationComboBox.setItems(FXCollections.observableArrayList("Consultation", "Téléconsultation"));
    }

    public void setRendezVous(RendezVous rendezVous) {
        this.currentRendezVous = rendezVous;
        datePicker.setValue(rendezVous.getDate());
        id_patient.setText(String.valueOf(rendezVous.getPatientId()));
        String doctorFullName = rendezVous.getMedecinNom() + " " + rendezVous.getMedecinPrenom();
        medecinComboBox.setValue(doctorFullName);
        String timeRange = rendezVous.getStartTime().toLocalTime() + " - " + rendezVous.getEndTime().toLocalTime();
        timeComboBox.setValue(timeRange);
        // Pré-sélectionner le type de consultation
        typeConsultationComboBox.setValue(rendezVous.getTypeConsultationId() == 1 ? "Consultation" : "Téléconsultation");
    }

    @FXML
    void modifier() {
        try {
            if ( id_patient.getText().isEmpty() || timeComboBox.getValue() == null || datePicker.getValue() == null ||
                    medecinComboBox.getValue() == null || typeConsultationComboBox.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = timeComboBox.getValue();  // Get the selected time range from the ComboBox

            // Parse the start time from the selected time range (e.g., "9:00-10:00")
            String[] timeParts = selectedTime.split("-"); // Split the time range into start and end times
            if (timeParts.length < 2) {
                showAlert("Erreur", "Plage horaire invalide.");
                return;
            }

            LocalTime startTime = LocalTime.parse(timeParts[0].trim());  // Start time (e.g., "9:00")
            LocalDateTime startDateTime = LocalDateTime.of(selectedDate, startTime);
            LocalDateTime endDateTime = startDateTime.plusHours(1);// Modify as needed if consultation duration differs

            int patientId = Integer.parseInt(id_patient.getText());
            if (!ps.isPatientIdValid(patientId)) {
                showAlert("Erreur", "ID patient invalide.");
                return;
            }
            String selectedMedecin = medecinComboBox.getValue(); // Ex: "Dr. Ahmed Ben Salah - Cardiologue"
            String[] parts = selectedMedecin.split(" - "); // Séparer spécialité
            if (parts.length < 2) {
                showAlert("Erreur", "Médecin sélectionné invalide.");
                return;
            }

            String fullName = parts[0]; // Extrait "Dr. Ahmed Ben Salah"
            String[] nameParts = fullName.split(" ", 3); // Sépare "Dr.", "Ahmed", "Ben Salah"

            if (nameParts.length < 3) {
                showAlert("Erreur", "Le format du nom du médecin est invalide.");
                return;
            }

            String medecinNom = nameParts[1]; // "Ahmed"
            String medecinPrenom = nameParts[2]; // "Ben Salah"

            // Récupérer l'ID du médecin
            int medecinId = getMedecinIdByNomPrenom(medecinNom, medecinPrenom);
            if (medecinId == -1) {
                showAlert("Erreur", "Médecin non trouvé.");
                return;
            }

            // Convertir le texte du ComboBox en ID numérique
            int consultationType = typeConsultationComboBox.getValue().equals("Consultation") ? 1 : 2;

            currentRendezVous.setDate(selectedDate);
            currentRendezVous.setPatientId(patientId);
            currentRendezVous.setMedecinId(medecinId);
            currentRendezVous.setTypeConsultationId(consultationType);
            currentRendezVous.setStartTime(startDateTime);
            currentRendezVous.setEndTime(endDateTime);



            ps.update(currentRendezVous);

            showAlert("Succès", "Rendez-vous modifié avec succès !");
        } catch (DateTimeParseException e) {
            showAlert("Erreur de format", "Le format de la date est invalide. Utilisez YYYY-MM-DD.");
        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "L’ID patient doit être un nombre entier.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur s'est produite : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}