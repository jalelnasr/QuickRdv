package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import javafx.scene.layout.HBox;

import java.sql.Date;
import java.util.HashMap;

public class AfficherOrdonnance {

    @FXML
    private VBox cardContainer; // Conteneur pour les cartes des ordonnances

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button searchButton;

    @FXML
    private Button pdfButton;


    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();

    @FXML
    public void initialize() {
        // Charger et afficher toutes les ordonnances sous forme de cartes
        List<Ordonnance> ordonnances = serviceOrdonnance.getAll();
        for (Ordonnance ordonnance : ordonnances) {
            AnchorPane card = createCard(ordonnance);
            cardContainer.getChildren().add(card); // Ajouter la carte au conteneur
        }

        pdfButton.setDisable(true); // Désactiver le bouton PDF au démarrage


        // Ajouter un événement au bouton PDF
        pdfButton.setOnAction(event -> {
            if (ordonnanceSelectionnee != null) {
                generatePdf(ordonnanceSelectionnee);
            } else {
                showAlert("Erreur", "Veuillez sélectionner une ordonnance.");
            }
        });
    }

    private void refreshMedicamentDisplay(VBox box, Map<String, Integer> medicaments) {
        box.getChildren().clear();
        for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
            box.getChildren().add(new Text(entry.getKey() + " : " + entry.getValue()));
        }
    }

    private void refreshMedicamentEdit(VBox box, Map<String, Integer> medicaments) {
        box.getChildren().clear();
        for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
            HBox row = new HBox(10);
            TextField medField = new TextField(entry.getKey());
            TextField qteField = new TextField(String.valueOf(entry.getValue()));
            row.getChildren().addAll(medField, qteField);
            box.getChildren().add(row);
        }
    }

    

    private AnchorPane createCard(Ordonnance ordonnance) {
        // Créer un conteneur de carte (AnchorPane)
        AnchorPane card = new AnchorPane();
        card.setPrefSize(600, 300);
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Ajouter des champs non modifiables (affichés sous forme de texte)
        Text patientText = new Text("ID Patient: " + ordonnance.getPatientId());
        patientText.setLayoutX(10);
        patientText.setLayoutY(20);

        Text dateText = new Text("Date: " + ordonnance.getDatePrescription());
        dateText.setLayoutX(10);
        dateText.setLayoutY(40);

        Text instructionsText = new Text("Instructions: " + ordonnance.getInstructions());
        instructionsText.setLayoutX(10);
        instructionsText.setLayoutY(60);

        VBox medicamentDisplayBox = new VBox(5);
        medicamentDisplayBox.setLayoutX(10);
        medicamentDisplayBox.setLayoutY(80);
        refreshMedicamentDisplay(medicamentDisplayBox, ordonnance.getMedicaments());

        // Ajouter des champs modifiables (initialement cachés)
        TextField patientField = new TextField(String.valueOf(ordonnance.getPatientId()));
        patientField.setLayoutX(10);
        patientField.setLayoutY(20);
        patientField.setVisible(false);

        DatePicker datePicker = new DatePicker();
        datePicker.setLayoutX(10);
        datePicker.setLayoutY(60);
        datePicker.setVisible(false);

        TextField instructionsField = new TextField(ordonnance.getInstructions());
        instructionsField.setLayoutX(10);
        instructionsField.setLayoutY(100);
        instructionsField.setVisible(false);

        // Gestion des médicaments
        VBox medicamentEditBox = new VBox(5);
        medicamentEditBox.setLayoutX(10);
        medicamentEditBox.setLayoutY(140);
        medicamentEditBox.setVisible(false);

        refreshMedicamentEdit(medicamentEditBox, ordonnance.getMedicaments());


        TextField newMedicamentField = new TextField();
        newMedicamentField.setPromptText("Nom du médicament");
        newMedicamentField.setLayoutX(10);
        newMedicamentField.setLayoutY(250);
        newMedicamentField.setPrefWidth(150);
        newMedicamentField.setVisible(false);

        TextField newQuantiteField = new TextField();
        newQuantiteField.setPromptText("Quantité");
        newQuantiteField.setLayoutX(170);
        newQuantiteField.setLayoutY(250);
        newQuantiteField.setPrefWidth(100);
        newQuantiteField.setVisible(false);

        Button ajouterMedicamentButton = new Button("Ajouter");
        ajouterMedicamentButton.setLayoutX(280);
        ajouterMedicamentButton.setLayoutY(250);
        ajouterMedicamentButton.setVisible(false);
        ajouterMedicamentButton.setOnAction(e -> {
            String med = newMedicamentField.getText();
            try {
                int qte = Integer.parseInt(newQuantiteField.getText());
                ordonnance.getMedicaments().put(med, qte);
                refreshMedicamentEdit(medicamentEditBox, ordonnance.getMedicaments());

                newMedicamentField.clear();
                newQuantiteField.clear();
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer une quantité valide.");
            }
        });

        // Buttons
        Button modifierButton = new Button("Modifier");
        modifierButton.setLayoutX(400);
        modifierButton.setLayoutY(20);

        Button enregistrerButton = new Button("Enregistrer");
        enregistrerButton.setLayoutX(400);
        enregistrerButton.setLayoutY(60);
        enregistrerButton.setVisible(false);

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setLayoutX(400);
        supprimerButton.setLayoutY(100);
        supprimerButton.setVisible(false);

        modifierButton.setOnAction(event -> {
            modifierButton.setVisible(false);
            enregistrerButton.setVisible(true);
            supprimerButton.setVisible(true);

            patientText.setVisible(false);
            dateText.setVisible(false);
            instructionsText.setVisible(false);
            medicamentDisplayBox.setVisible(false);

            patientField.setVisible(true);
            datePicker.setVisible(true);
            instructionsField.setVisible(true);
            medicamentEditBox.setVisible(true);
            newMedicamentField.setVisible(true);
            newQuantiteField.setVisible(true);
            ajouterMedicamentButton.setVisible(true);
            if (ordonnance.getDatePrescription() != null) {
                // If it's a java.sql.Date, convert it to LocalDate
                if (ordonnance.getDatePrescription() instanceof java.sql.Date) {
                    LocalDate localDate = ((Date) ordonnance.getDatePrescription()).toLocalDate();
                    datePicker.setValue(localDate); // Set the value of the DatePicker
                } else if (ordonnance.getDatePrescription() instanceof java.util.Date) {
                    // If it's a java.util.Date, convert it to LocalDate
                    java.util.Date utilDate = ordonnance.getDatePrescription();
                    LocalDate localDate = utilDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    datePicker.setValue(localDate); // Set the value of the DatePicker
                }
            }
        });

        supprimerButton.setOnAction(event -> {
            // Afficher une boîte de dialogue de confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'ordonnance");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette ordonnance ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer l'ordonnance de la base de données
                serviceOrdonnance.delete(ordonnance);

                // Supprimer la carte de l'interface utilisateur
                cardContainer.getChildren().remove(card);
            }
        });

        enregistrerButton.setOnAction(event -> {
            double currentScroll = scrollPane.getVvalue();

            modifierButton.setVisible(true);
            enregistrerButton.setVisible(false);
            supprimerButton.setVisible(false);


            patientText.setVisible(true);
            dateText.setVisible(true);
            instructionsText.setVisible(true);
            medicamentDisplayBox.setVisible(true);

            patientField.setVisible(false);
            datePicker.setVisible(false);
            instructionsField.setVisible(false);
            medicamentEditBox.setVisible(false);
            newMedicamentField.setVisible(false);
            newQuantiteField.setVisible(false);
            ajouterMedicamentButton.setVisible(false);


            ordonnance.setPatientId(Integer.parseInt(patientField.getText()));
            ordonnance.setDatePrescription(Date.valueOf(datePicker.getValue()));
            ordonnance.setInstructions(instructionsField.getText());

            // Fix: Correctly update medicament quantities
            Map<String, Integer> updatedMedicaments = new HashMap<>();

            for (Node node : medicamentEditBox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    if (hbox.getChildren().size() >= 2) {
                        TextField medNameField = (TextField) hbox.getChildren().get(0); // Fix: This is a TextField, not a Label
                        TextField qtyField = (TextField) hbox.getChildren().get(1); // Fix: Correctly reference the TextField for quantity

                        String medName = medNameField.getText().trim();
                        String qtyText = qtyField.getText().trim();

                        if (qtyText.isEmpty()) {
                            showAlert("Erreur", "Veuillez entrer une quantité pour " + medName);
                            return;
                        }

                        try {
                            int newQty = Integer.parseInt(qtyText);
                            updatedMedicaments.put(medName, newQty); // Update local map instead of modifying while iterating
                        } catch (NumberFormatException e) {
                            showAlert("Erreur", "Veuillez entrer une quantité valide pour " + medName);
                            return;
                        }
                    }
                }
            }

            ordonnance.setMedicaments(updatedMedicaments); // Update all at once
            patientText.setText("Patient ID: " + ordonnance.getPatientId());
            dateText.setText("Date: " + ordonnance.getDatePrescription()); // Ensure correct format
            instructionsText.setText("Instructions: " + ordonnance.getInstructions());
            refreshMedicamentDisplay(medicamentDisplayBox, ordonnance.getMedicaments());

            // Save the updated ordonnance
            serviceOrdonnance.update(ordonnance);
            Platform.runLater(() -> {
                scrollPane.setVvalue(currentScroll);
            });

        });

        // Sélectionner l'ordonnance lorsqu'on clique sur la carte
        card.setOnMouseClicked(event -> {
            ordonnanceSelectionnee = ordonnance; // Stocke l'ordonnance sélectionnée
            pdfButton.setDisable(false); // Active le bouton PDF
        });

        card.getChildren().addAll(patientText, dateText, instructionsText, medicamentDisplayBox,
                patientField, datePicker, instructionsField, medicamentEditBox,
                newMedicamentField, newQuantiteField, ajouterMedicamentButton,
                modifierButton, enregistrerButton , supprimerButton);

        return card;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void rechercherOrdonnances() {
        LocalDate selectedDate = datePicker.getValue();

        // Si la date est vide, on recharge toutes les ordonnances
        if (selectedDate == null) {
            cardContainer.getChildren().clear(); // Nettoyer le conteneur
            List<Ordonnance> allOrdonnances = serviceOrdonnance.getAll(); // Charger toutes les ordonnances
            for (Ordonnance ordonnance : allOrdonnances) {
                cardContainer.getChildren().add(createCard(ordonnance)); // Afficher chaque ordonnance sous forme de carte
            }
            return;
        }

        System.out.println("Recherche des ordonnances pour la date : " + selectedDate);

        // Récupération des ordonnances correspondant à la date sélectionnée
        List<Ordonnance> ordonnances = serviceOrdonnance.getOrdonnancesByDate(String.valueOf(selectedDate));

        // Nettoyage de l'affichage
        cardContainer.getChildren().clear();

        if (ordonnances.isEmpty()) {
            Label noResults = new Label("Aucune ordonnance trouvée pour cette date.");
            noResults.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");
            cardContainer.getChildren().add(noResults);
            return;
        }

        // Affichage des résultats sous forme de cartes
        for (Ordonnance ordonnance : ordonnances) {
            cardContainer.getChildren().add(createCard(ordonnance));
        }
    }
    private Ordonnance ordonnanceSelectionnee = null;




    private void generatePdf(Ordonnance ordonnance) {
        try {
            String filePath = "/C:/Users/LENOVO/OneDrive/Documents/ordonnance.pdf"; // Remplacez par un chemin valide

            serviceOrdonnance.generatePdf(ordonnance, filePath); // Assurez-vous que le service est correctement injecté
            showAlert("Succès,PDF généré avec succès à l'emplacement : " + filePath);
        } catch (Exception e) {
            showAlert("Erreur,Erreur lors de la génération du PDF : " + e.getMessage());
            e.printStackTrace(); // Affiche la stack trace pour plus de détails
        }
    }

    private void showAlert(String s) {
    }
}
