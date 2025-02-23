package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.models.Assurance;
import tn.esprit.services.ServiceAssurance;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AffichageAssurances {

    @FXML
    private VBox cardContainer; // VBox to hold the cards

    private ServiceAssurance serviceAssurance = new ServiceAssurance();

    @FXML
    public void initialize() {
        // Load and display all assurances as cards
        List<Assurance> assurances = serviceAssurance.getAll();
        for (Assurance assurance : assurances) {
            // Create a card for each assurance
            AnchorPane card = createCard(assurance);
            cardContainer.getChildren().add(card); // Add the card to the VBox
        }
    }

    /**
     * Creates a card (AnchorPane) to display the details of an Assurance.
     */
    private AnchorPane createCard(Assurance assurance) {
        // Create a card container (AnchorPane)
        AnchorPane card = new AnchorPane();
        card.setPrefSize(550, 150); // Set a fixed size for the card
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Add non-editable fields to the card (initially displayed as Text)
        Text nomText = new Text("Nom: " + assurance.getNom());
        nomText.setLayoutX(10);
        nomText.setLayoutY(20);
        nomText.setStyle("-fx-font-size: 14px;");

        Text typeText = new Text("Type: " + assurance.getType());
        typeText.setLayoutX(10);
        typeText.setLayoutY(40);
        typeText.setStyle("-fx-font-size: 14px;");

        Text montantText = new Text("Montant: " + assurance.getMontantCouvert());
        montantText.setLayoutX(10);
        montantText.setLayoutY(60);
        montantText.setStyle("-fx-font-size: 14px;");

        Text dateDebutText = new Text("Date Début: " + assurance.getDateDebut());
        dateDebutText.setLayoutX(10);
        dateDebutText.setLayoutY(80);
        dateDebutText.setStyle("-fx-font-size: 14px;");

        Text dateFinText = new Text("Date Fin: " + assurance.getDateFin());
        dateFinText.setLayoutX(10);
        dateFinText.setLayoutY(100);
        dateFinText.setStyle("-fx-font-size: 14px;");

        // Add editable fields to the card (initially hidden)
        TextField nomField = new TextField(assurance.getNom());
        nomField.setLayoutX(10);
        nomField.setLayoutY(20);
        nomField.setPrefWidth(200);
        nomField.setVisible(false); // Initially hidden

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Étatique", "Privée");
        typeComboBox.setValue(assurance.getType()); // Set the current type
        typeComboBox.setLayoutX(10);
        typeComboBox.setLayoutY(40);
        typeComboBox.setPrefWidth(200);
        typeComboBox.setVisible(false); // Initially hidden

        TextField montantField = new TextField(String.valueOf(assurance.getMontantCouvert()));
        montantField.setLayoutX(10);
        montantField.setLayoutY(60);
        montantField.setPrefWidth(200);
        montantField.setVisible(false); // Initially hidden

        // Use DatePicker for date fields
        DatePicker dateDebutPicker = new DatePicker(assurance.getDateDebut());
        dateDebutPicker.setLayoutX(10);
        dateDebutPicker.setLayoutY(80);
        dateDebutPicker.setPrefWidth(200);
        dateDebutPicker.setVisible(false); // Initially hidden

        DatePicker dateFinPicker = new DatePicker(assurance.getDateFin());
        dateFinPicker.setLayoutX(10);
        dateFinPicker.setLayoutY(100);
        dateFinPicker.setPrefWidth(200);
        dateFinPicker.setVisible(false); // Initially hidden

        // Add a "Modifier" button to the card
        Button modifierButton = new Button("Modifier");
        modifierButton.setLayoutX(400);
        modifierButton.setLayoutY(20);
        modifierButton.setPrefWidth(100);
        modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"); // Darker green

        // Add an "Annuler" button to the card
        Button annulerButton = new Button("Annuler");
        annulerButton.setLayoutX(400);
        annulerButton.setLayoutY(60);
        annulerButton.setPrefWidth(100);
        annulerButton.setStyle("-fx-background-color: #1974d9; -fx-text-fill: white;"); // Softer red

        // Add a "Supprimer" (Delete) button to the card
        Button supprimerButton = new Button("Supprimer");
        supprimerButton.setLayoutX(400);
        supprimerButton.setLayoutY(100);
        supprimerButton.setPrefWidth(100);
        supprimerButton.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white;"); // Darker red

        // Handle "Modifier" button click
        modifierButton.setOnAction(event -> {
            if (modifierButton.getText().equals("Modifier")) {
                // Switch to "Enregistrer" mode
                modifierButton.setText("Enregistrer");
                modifierButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                // Hide non-editable fields
                nomText.setVisible(false);
                typeText.setVisible(false);
                montantText.setVisible(false);
                dateDebutText.setVisible(false);
                dateFinText.setVisible(false);

                // Show editable fields
                nomField.setVisible(true);
                typeComboBox.setVisible(true);
                montantField.setVisible(true);
                dateDebutPicker.setVisible(true);
                dateFinPicker.setVisible(true);
            } else {
                // Switch back to "Modifier" mode
                modifierButton.setText("Modifier");
                modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;");

                // Hide editable fields
                nomField.setVisible(false);
                typeComboBox.setVisible(false);
                montantField.setVisible(false);
                dateDebutPicker.setVisible(false);
                dateFinPicker.setVisible(false);

                // Show non-editable fields with updated values
                nomText.setText("Nom: " + nomField.getText());
                typeText.setText("Type: " + typeComboBox.getValue());
                montantText.setText("Montant: " + montantField.getText());
                dateDebutText.setText("Date Début: " + dateDebutPicker.getValue());
                dateFinText.setText("Date Fin: " + dateFinPicker.getValue());

                nomText.setVisible(true);
                typeText.setVisible(true);
                montantText.setVisible(true);
                dateDebutText.setVisible(true);
                dateFinText.setVisible(true);

                // Save the edited assurance details
                assurance.setNom(nomField.getText());
                assurance.setType(typeComboBox.getValue());
                assurance.setMontantCouvert(Float.parseFloat(montantField.getText()));
                assurance.setDateDebut(dateDebutPicker.getValue());
                assurance.setDateFin(dateFinPicker.getValue());

                // Update the assurance in the database
                serviceAssurance.update(assurance);

                System.out.println("Enregistrer button clicked for assurance: " + assurance.getIdAssurance());
            }
        });

        // Handle "Annuler" button click
        annulerButton.setOnAction(event -> {
            // Reset the "Modifier" button to its original state
            modifierButton.setText("Modifier");
            modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;");

            // Hide editable fields
            nomField.setVisible(false);
            typeComboBox.setVisible(false);
            montantField.setVisible(false);
            dateDebutPicker.setVisible(false);
            dateFinPicker.setVisible(false);

            // Show non-editable fields with original values
            nomText.setVisible(true);
            typeText.setVisible(true);
            montantText.setVisible(true);
            dateDebutText.setVisible(true);
            dateFinText.setVisible(true);

            System.out.println("Annuler button clicked for assurance: " + assurance.getIdAssurance());
        });

        // Handle "Supprimer" (Delete) button click
        supprimerButton.setOnAction(event -> {
            // Show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'assurance");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette assurance ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remove the assurance from the database
                serviceAssurance.delete(assurance);

                // Remove the card from the UI
                cardContainer.getChildren().remove(card);

                System.out.println("Supprimer button clicked for assurance: " + assurance.getIdAssurance());
            }
        });

        // Add all elements to the card
        card.getChildren().addAll(
                nomText, typeText, montantText, dateDebutText, dateFinText,
                nomField, typeComboBox, montantField, dateDebutPicker, dateFinPicker,
                modifierButton, annulerButton, supprimerButton
        );

        return card;
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}