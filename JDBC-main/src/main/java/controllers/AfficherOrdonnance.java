package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

public class AfficherOrdonnance {

    @FXML
    private VBox cardContainer; // Conteneur pour les cartes des ordonnances

    private ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();

    @FXML
    public void initialize() {
        // Charger et afficher toutes les ordonnances sous forme de cartes
        List<Ordonnance> ordonnances = serviceOrdonnance.getAll();
        for (Ordonnance ordonnance : ordonnances) {
            AnchorPane card = createCard(ordonnance);
            cardContainer.getChildren().add(card); // Ajouter la carte au conteneur
        }
    }

    /**
     * Crée une carte (AnchorPane) pour afficher les détails d'une ordonnance.
     */
    private AnchorPane createCard(Ordonnance ordonnance) {
        // Créer un conteneur de carte (AnchorPane)
        AnchorPane card = new AnchorPane();
        card.setPrefSize(600, 300); // Taille fixe pour la carte
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

        // Ajouter des champs modifiables (initialement cachés)
        TextField patientField = new TextField(String.valueOf(ordonnance.getPatientId()));
        patientField.setLayoutX(10);
        patientField.setLayoutY(20);
        patientField.setPrefWidth(200);
        patientField.setVisible(false);

        DatePicker datePicker = new DatePicker();
        datePicker.setLayoutX(10);
        datePicker.setLayoutY(40);
        datePicker.setPrefWidth(200);
        datePicker.setVisible(false);

        TextField instructionsField = new TextField(ordonnance.getInstructions());
        instructionsField.setLayoutX(10);
        instructionsField.setLayoutY(60);
        instructionsField.setPrefWidth(200);
        instructionsField.setVisible(false);

        // Gestion des médicaments
        VBox medicamentBox = new VBox(5);
        medicamentBox.setLayoutX(10);
        medicamentBox.setLayoutY(100);
        medicamentBox.setPrefWidth(400);

        Map<String, Integer> tempMedicaments = new HashMap<>(ordonnance.getMedicaments());
        for (Map.Entry<String, Integer> entry : tempMedicaments.entrySet()) {
            HBox medicamentRow = new HBox(10);
            TextField medField = new TextField(entry.getKey());
            TextField qteField = new TextField(String.valueOf(entry.getValue()));

            Button deleteButton = new Button("X");
            deleteButton.setOnAction(e -> medicamentBox.getChildren().remove(medicamentRow));

            medicamentRow.getChildren().addAll(medField, qteField, deleteButton);
            medicamentBox.getChildren().add(medicamentRow);
        }

        TextField newMedicamentField = new TextField();
        newMedicamentField.setPromptText("Nom du médicament");
        newMedicamentField.setLayoutX(10);
        newMedicamentField.setLayoutY(250);
        newMedicamentField.setPrefWidth(150);

        TextField newQuantiteField = new TextField();
        newQuantiteField.setPromptText("Quantité");
        newQuantiteField.setLayoutX(170);
        newQuantiteField.setLayoutY(250);
        newQuantiteField.setPrefWidth(100);

        Button ajouterMedicamentButton = new Button("Ajouter");
        ajouterMedicamentButton.setLayoutX(280);
        ajouterMedicamentButton.setLayoutY(250);
        ajouterMedicamentButton.setOnAction(e -> {
            String med = newMedicamentField.getText();
            int qte;
            try {
                qte = Integer.parseInt(newQuantiteField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer une quantité valide.");
                return;
            }

            if (!med.isEmpty()) {
                HBox medicamentRow = new HBox(10);
                TextField medField = new TextField(med);
                TextField qteField = new TextField(String.valueOf(qte));

                Button deleteButton = new Button("X");
                deleteButton.setOnAction(event -> medicamentBox.getChildren().remove(medicamentRow));

                medicamentRow.getChildren().addAll(medField, qteField, deleteButton);
                medicamentBox.getChildren().add(medicamentRow);

                newMedicamentField.clear();
                newQuantiteField.clear();
            }
        });

        // Ajouter un bouton "Modifier"
        Button modifierButton = new Button("Modifier");
        modifierButton.setLayoutX(400);
        modifierButton.setLayoutY(20);
        modifierButton.setPrefWidth(100);

        // Ajouter un bouton "Annuler"
        Button annulerButton = new Button("Annuler");
        annulerButton.setLayoutX(400);
        annulerButton.setLayoutY(60);
        annulerButton.setPrefWidth(100);

        // Ajouter un bouton "Supprimer"
        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setLayoutX(400);
        supprimerButton.setLayoutY(100);
        supprimerButton.setPrefWidth(100);

        // Gérer le clic sur le bouton "Modifier"
        modifierButton.setOnAction(event -> {
            if (modifierButton.getText().equals("Modifier")) {
                // Passer en mode "Enregistrer"
                modifierButton.setText("Enregistrer");

                // Masquer les champs non modifiables
                patientText.setVisible(false);
                dateText.setVisible(false);
                instructionsText.setVisible(false);

                // Afficher les champs modifiables
                patientField.setVisible(true);
                datePicker.setVisible(true);
                instructionsField.setVisible(true);
                medicamentBox.setVisible(true);
                newMedicamentField.setVisible(true);
                newQuantiteField.setVisible(true);
                ajouterMedicamentButton.setVisible(true);
            } else {
                // Revenir en mode "Modifier"
                modifierButton.setText("Modifier");

                // Masquer les champs modifiables
                patientField.setVisible(false);
                datePicker.setVisible(false);
                instructionsField.setVisible(false);
                medicamentBox.setVisible(false);
                newMedicamentField.setVisible(false);
                newQuantiteField.setVisible(false);
                ajouterMedicamentButton.setVisible(false);

                // Afficher les champs non modifiables avec les nouvelles valeurs
                patientText.setText("ID Patient: " + patientField.getText());
                dateText.setText("Date: " + datePicker.getValue());
                instructionsText.setText("Instructions: " + instructionsField.getText());

                patientText.setVisible(true);
                dateText.setVisible(true);
                instructionsText.setVisible(true);

                // Mettre à jour l'ordonnance dans la base de données
                ordonnance.setPatientId(Integer.parseInt(patientField.getText()));
                ordonnance.setDatePrescription(Date.valueOf(datePicker.getValue()));
                ordonnance.setInstructions(instructionsField.getText());

                // Mettre à jour les médicaments
                Map<String, Integer> newMedicaments = new HashMap<>();
                for (Node node : medicamentBox.getChildren()) {
                    if (node instanceof HBox) {
                        HBox row = (HBox) node;
                        TextField medField = (TextField) row.getChildren().get(0);
                        TextField qteField = (TextField) row.getChildren().get(1);
                        newMedicaments.put(medField.getText(), Integer.parseInt(qteField.getText()));
                    }
                }
                ordonnance.setMedicaments(newMedicaments);

                serviceOrdonnance.update(ordonnance);
            }
        });

        // Gérer le clic sur le bouton "Annuler"
        annulerButton.setOnAction(event -> {
            // Revenir en mode "Modifier"
            modifierButton.setText("Modifier");

            // Masquer les champs modifiables
            patientField.setVisible(false);
            datePicker.setVisible(false);
            instructionsField.setVisible(false);
            medicamentBox.setVisible(false);
            newMedicamentField.setVisible(false);
            newQuantiteField.setVisible(false);
            ajouterMedicamentButton.setVisible(false);

            // Afficher les champs non modifiables avec les valeurs d'origine
            patientText.setVisible(true);
            dateText.setVisible(true);
            instructionsText.setVisible(true);
        });

        // Gérer le clic sur le bouton "Supprimer"
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

        // Ajouter tous les éléments à la carte
        card.getChildren().addAll(
                patientText, dateText, instructionsText,
                patientField, datePicker, instructionsField,
                medicamentBox, newMedicamentField, newQuantiteField, ajouterMedicamentButton,
                modifierButton, annulerButton, supprimerButton
        );

        return card;
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}