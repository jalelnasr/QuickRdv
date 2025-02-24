package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class AjouterOrdonnance implements Initializable {

    @FXML
    private ComboBox<String> medecinComboBox;
    @FXML
    private TextField idPatientField;
    @FXML
    private DatePicker datePrescription;
    @FXML
    private TextField instructionsField;
    @FXML
    private TextField medicamentField;
    @FXML
    private TextField quantiteField;
    @FXML
    private ListView<String> listMedicaments;





    @FXML
    private TextField txtMedicaments;

    private final ObservableList<String> medicamentsList = FXCollections.observableArrayList();
    private final Map<String, Integer> medicamentsMap = new HashMap<>();
    private final ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();
    private final Map<Integer, String> medecinMap = new HashMap<>();
    private Ordonnance ordonnance;

    public AjouterOrdonnance() throws IOException {
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedecins();
        // Ajouter un gestionnaire d'événement pour la sélection dans la liste des médicaments
        medicamentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Lorsque l'utilisateur sélectionne un médicament dans la liste de recherche,
                // le médicament est ajouté au champ medicamentField
                medicamentField.setText(newValue);
                medicamentListView.setVisible(false); // Masquer la liste après sélection
            }
        });
    }


     // Charge les médecins disponibles dans le ComboBox.

    private void loadMedecins() {
        String query = "SELECT id, specialite FROM medecin";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quick_rdv", "root", "");
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String specialite = rs.getString("specialite");
                medecinMap.put(id, specialite);
            }

            medecinComboBox.getItems().addAll(medecinMap.values());
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des médecins.");
            e.printStackTrace();
        }
    }


     // Ajoute un médicament à la liste temporaire.

    @FXML
    private void ajouterMedicament() {
        String nomMed = medicamentField.getText().trim();
        String qteStr = quantiteField.getText().trim();

        if (!nomMed.isEmpty() && !qteStr.isEmpty()) {
            try {
                int qte = Integer.parseInt(qteStr);
                medicamentsMap.put(nomMed, qte);
                medicamentsList.add(nomMed + " : " + qte);
                listMedicaments.setItems(medicamentsList);

                medicamentField.clear();
                quantiteField.clear();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Veuillez entrer une quantité valide.");
            }
        } else {
            showAlert("Erreur", "Remplissez tous les champs du médicament.");
        }
    }


     // Enregistre une ordonnance dans la base de données directement avec ServiceOrdonnance.

    @FXML
    private void ajouterOrdonnance(ActionEvent event) {
        try {
            if (idPatientField.getText().isEmpty() || datePrescription.getValue() == null ||
                    medecinComboBox.getValue() == null || medicamentsMap.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            int patientId = Integer.parseInt(idPatientField.getText());
            LocalDate datePresc = datePrescription.getValue();

            int medecinId = medecinMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(medecinComboBox.getValue()))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(-1);

            if (medecinId == -1) {
                showAlert("Erreur", "Médecin non trouvé.");
                return;
            }

            String instructions = instructionsField.getText().trim();
            if (instructions.isEmpty()) {
                instructions = "Suivre les indications médicales.";
            }

            String statut = "Actif";

            // Ajout direct avec ServiceOrdonnance sans instancier Ordonnance
            ServiceOrdonnance so = new ServiceOrdonnance();

             so.add(new Ordonnance(medecinId, patientId, Date.valueOf(datePresc), instructions, statut, medicamentsMap));

            showAlert("Succès", "Ordonnance ajoutée avec succès !");
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID patient doit être un nombre.");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout de l'ordonnance.");
            e.printStackTrace();
        }
    }


     // Affiche une alerte.

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


     // Réinitialise les champs après l'ajout.

    @FXML
    private void clearFields() {
        idPatientField.clear();
        datePrescription.setValue(null);
        medecinComboBox.setValue(null);
        instructionsField.clear();
        medicamentsMap.clear();
        medicamentsList.clear();
        listMedicaments.setItems(medicamentsList);
    }

    @FXML
    private void ouvrirAfficherOrdonnance(ActionEvent event) throws IOException {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherOrdonnance.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Liste des Ordonnances");
            stage.setScene(new Scene(root));
            stage.show();

    }




    @FXML

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
        idPatientField.setText(String.valueOf(ordonnance.getPatientId()));


        instructionsField.setText(ordonnance.getInstructions());
        // Si getMedicaments() retourne un HashMap, extraire les valeurs sous forme de liste
        if (ordonnance.getMedicaments() instanceof Map) {
            // Cast to Map<String, Integer> if that's the actual type
            Map<String, Integer> medicamentsMap = (Map<String, Integer>) ordonnance.getMedicaments();

            // Convert Map<String, Integer> to List<String>
            List<String> medicamentsList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : medicamentsMap.entrySet()) {
                // Convert Integer to String and add to the list
                medicamentsList.add(entry.getKey() + ": " + entry.getValue()); // Or adjust how you want the string representation
            }

            // Set items in the ListView
            listMedicaments.getItems().setAll(medicamentsList);
        }





    }

    @FXML
    private TextField searchTextField;
    @FXML
    private ListView<String> medicamentListView;



    @FXML
    private void searchMedicaments() {
        String searchText = searchTextField.getText().trim();

        if (!searchText.isEmpty()) {
            List<String> medicaments = serviceOrdonnance.searchMedicaments(searchText);

            // Filtrer pour ne garder que les médicaments qui commencent par le texte fourni
            List<String> filteredMedicaments = medicaments.stream()
                    .filter(med -> med.toLowerCase().startsWith(searchText.toLowerCase()))
                    .toList();

            if (!filteredMedicaments.isEmpty()) {
                medicamentListView.getItems().setAll(filteredMedicaments);
                medicamentListView.setVisible(true);
            } else {
                medicamentListView.setVisible(false);
            }
        } else {
            medicamentListView.setVisible(false);
        }
    }









}
