package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import tn.esprit.models.InformationsGenerales;
import tn.esprit.services.ServiceInformationsGenerales;

import java.util.List;

public class AffichageInformations {

    @FXML
    private VBox cardContainer; // VBox to hold the cards

    private ServiceInformationsGenerales serviceInformations = new ServiceInformationsGenerales();

    @FXML
    public void initialize() {
        // Load and display all medical records as cards
        List<InformationsGenerales> dossiers = serviceInformations.getAll();
        for (InformationsGenerales dossier : dossiers) {
            AnchorPane card = createCard(dossier);
            cardContainer.getChildren().add(card);
        }
    }

    /**
     * Creates a card (AnchorPane) to display the details of a medical record.
     */
    private AnchorPane createCard(InformationsGenerales dossier) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(550, 300); // Adjusted size for better spacing
        card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-padding: 10;");

        // Column 1: Labels and non-editable text fields
        double labelX = 10; // X position for labels
        double fieldX = 200; // X position for fields
        double startY = 20; // Starting Y position
        double spacing = 25; // Vertical spacing between fields




        // Taille
        Text tailleLabel = new Text("Taille: ");
        tailleLabel.setLayoutX(labelX);
        tailleLabel.setLayoutY(startY + spacing);
        tailleLabel.setStyle("-fx-font-size: 14px;");

        Text tailleText = new Text(String.valueOf(dossier.getTaille()));
        tailleText.setLayoutX(fieldX);
        tailleText.setLayoutY(startY + spacing);
        tailleText.setStyle("-fx-font-size: 14px;");

        TextField tailleField = new TextField(String.valueOf(dossier.getTaille()));
        tailleField.setLayoutX(fieldX);
        tailleField.setLayoutY(startY + spacing);
        tailleField.setPrefWidth(200);
        tailleField.setVisible(false); // Initially hidden

        // Poids
        Text poidsLabel = new Text("Poids: ");
        poidsLabel.setLayoutX(labelX);
        poidsLabel.setLayoutY(startY + 2 * spacing);
        poidsLabel.setStyle("-fx-font-size: 14px;");

        Text poidsText = new Text(String.valueOf(dossier.getPoids()));
        poidsText.setLayoutX(fieldX);
        poidsText.setLayoutY(startY + 2 * spacing);
        poidsText.setStyle("-fx-font-size: 14px;");

        TextField poidsField = new TextField(String.valueOf(dossier.getPoids()));
        poidsField.setLayoutX(fieldX);
        poidsField.setLayoutY(startY + 2 * spacing);
        poidsField.setPrefWidth(200);
        poidsField.setVisible(false); // Initially hidden

        // Maladies
        Text maladiesLabel = new Text("Maladies: ");
        maladiesLabel.setLayoutX(labelX);
        maladiesLabel.setLayoutY(startY + 3 * spacing);
        maladiesLabel.setStyle("-fx-font-size: 14px;");

        Text maladiesText = new Text(dossier.hasMaladies() ? "Oui" : "Non");
        maladiesText.setLayoutX(fieldX);
        maladiesText.setLayoutY(startY + 3 * spacing);
        maladiesText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> maladiesCombo = new ComboBox<>();
        maladiesCombo.getItems().addAll("Oui", "Non");
        maladiesCombo.setValue(dossier.hasMaladies() ? "Oui" : "Non");
        maladiesCombo.setLayoutX(fieldX);
        maladiesCombo.setLayoutY(startY + 3 * spacing);
        maladiesCombo.setPrefWidth(200);
        maladiesCombo.setVisible(false); // Initially hidden

        // Antécédents Cardiovasculaires
        Text antecedentsLabel = new Text("Antécédents Cardiovasculaires: ");
        antecedentsLabel.setLayoutX(labelX);
        antecedentsLabel.setLayoutY(startY + 4 * spacing);
        antecedentsLabel.setStyle("-fx-font-size: 14px;");

        Text antecedentsText = new Text(dossier.getAntecedentsCardiovasculairesFamiliaux());
        antecedentsText.setLayoutX(fieldX);
        antecedentsText.setLayoutY(startY + 4 * spacing);
        antecedentsText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> antecedentsCardioCombo = new ComboBox<>();
        antecedentsCardioCombo.getItems().addAll("Oui", "Non", "Je ne sais pas");
        antecedentsCardioCombo.setValue(dossier.getAntecedentsCardiovasculairesFamiliaux());
        antecedentsCardioCombo.setLayoutX(fieldX);
        antecedentsCardioCombo.setLayoutY(startY + 4 * spacing);
        antecedentsCardioCombo.setPrefWidth(200);
        antecedentsCardioCombo.setVisible(false); // Initially hidden

        // Asthmatique
        Text asthmatiqueLabel = new Text("Asthmatique: ");
        asthmatiqueLabel.setLayoutX(labelX);
        asthmatiqueLabel.setLayoutY(startY + 5 * spacing);
        asthmatiqueLabel.setStyle("-fx-font-size: 14px;");

        Text asthmatiqueText = new Text(dossier.getAsthmatique());
        asthmatiqueText.setLayoutX(fieldX);
        asthmatiqueText.setLayoutY(startY + 5 * spacing);
        asthmatiqueText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> asthmatiqueCombo = new ComboBox<>();
        asthmatiqueCombo.getItems().addAll("Oui", "Non");
        asthmatiqueCombo.setValue(dossier.getAsthmatique());
        asthmatiqueCombo.setLayoutX(fieldX);
        asthmatiqueCombo.setLayoutY(startY + 5 * spacing);
        asthmatiqueCombo.setPrefWidth(200);
        asthmatiqueCombo.setVisible(false); // Initially hidden

        // Suivi Dentaire
        Text suiviDentaireLabel = new Text("Suivi Dentaire: ");
        suiviDentaireLabel.setLayoutX(labelX);
        suiviDentaireLabel.setLayoutY(startY + 6 * spacing);
        suiviDentaireLabel.setStyle("-fx-font-size: 14px;");

        Text suiviDentaireText = new Text(dossier.getSuiviDentaireRegulier());
        suiviDentaireText.setLayoutX(fieldX);
        suiviDentaireText.setLayoutY(startY + 6 * spacing);
        suiviDentaireText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> suiviDentaireCombo = new ComboBox<>();
        suiviDentaireCombo.getItems().addAll("Oui", "Non", "Je ne sais pas");
        suiviDentaireCombo.setValue(dossier.getSuiviDentaireRegulier());
        suiviDentaireCombo.setLayoutX(fieldX);
        suiviDentaireCombo.setLayoutY(startY + 6 * spacing);
        suiviDentaireCombo.setPrefWidth(200);
        suiviDentaireCombo.setVisible(false); // Initially hidden

        // Antécédents Chirurgicaux
        Text antecedentsChirurgicauxLabel = new Text("Antécédents Chirurgicaux: ");
        antecedentsChirurgicauxLabel.setLayoutX(labelX);
        antecedentsChirurgicauxLabel.setLayoutY(startY + 7 * spacing);
        antecedentsChirurgicauxLabel.setStyle("-fx-font-size: 14px;");

        Text antecedentsChirurgicauxText = new Text(dossier.getAntecedentsChirurgicaux());
        antecedentsChirurgicauxText.setLayoutX(fieldX);
        antecedentsChirurgicauxText.setLayoutY(startY + 7 * spacing);
        antecedentsChirurgicauxText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> antecedentsChirurgicauxCombo = new ComboBox<>();
        antecedentsChirurgicauxCombo.getItems().addAll("Oui", "Non", "Je ne sais pas");
        antecedentsChirurgicauxCombo.setValue(dossier.getAntecedentsChirurgicaux());
        antecedentsChirurgicauxCombo.setLayoutX(fieldX);
        antecedentsChirurgicauxCombo.setLayoutY(startY + 7 * spacing);
        antecedentsChirurgicauxCombo.setPrefWidth(200);
        antecedentsChirurgicauxCombo.setVisible(false); // Initially hidden

        // Allergies
        Text allergiesLabel = new Text("Allergies: ");
        allergiesLabel.setLayoutX(labelX);
        allergiesLabel.setLayoutY(startY + 8 * spacing);
        allergiesLabel.setStyle("-fx-font-size: 14px;");

        Text allergiesText = new Text(dossier.getAllergies());
        allergiesText.setLayoutX(fieldX);
        allergiesText.setLayoutY(startY + 8 * spacing);
        allergiesText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> allergiesCombo = new ComboBox<>();
        allergiesCombo.getItems().addAll("Oui", "Non");
        allergiesCombo.setValue(dossier.getAllergies());
        allergiesCombo.setLayoutX(fieldX);
        allergiesCombo.setLayoutY(startY + 8 * spacing);
        allergiesCombo.setPrefWidth(200);
        allergiesCombo.setVisible(false); // Initially hidden

        // Profession
        Text professionLabel = new Text("Profession: ");
        professionLabel.setLayoutX(labelX);
        professionLabel.setLayoutY(startY + 9 * spacing);
        professionLabel.setStyle("-fx-font-size: 14px;");

        Text professionText = new Text(dossier.getProfession());
        professionText.setLayoutX(fieldX);
        professionText.setLayoutY(startY + 9 * spacing);
        professionText.setStyle("-fx-font-size: 14px;");

        TextField professionField = new TextField(dossier.getProfession());
        professionField.setLayoutX(fieldX);
        professionField.setLayoutY(startY + 9 * spacing);
        professionField.setPrefWidth(200);
        professionField.setVisible(false); // Initially hidden

        // Niveau de Stress
        Text niveauDeStressLabel = new Text("Niveau de Stress: ");
        niveauDeStressLabel.setLayoutX(labelX);
        niveauDeStressLabel.setLayoutY(startY + 10 * spacing);
        niveauDeStressLabel.setStyle("-fx-font-size: 14px;");

        Text niveauDeStressText = new Text(dossier.getNiveauDeStress());
        niveauDeStressText.setLayoutX(fieldX);
        niveauDeStressText.setLayoutY(startY + 10 * spacing);
        niveauDeStressText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> niveauDeStressCombo = new ComboBox<>();
        niveauDeStressCombo.getItems().addAll("Faible", "Moyen", "Élevé");
        niveauDeStressCombo.setValue(dossier.getNiveauDeStress());
        niveauDeStressCombo.setLayoutX(fieldX);
        niveauDeStressCombo.setLayoutY(startY + 10 * spacing);
        niveauDeStressCombo.setPrefWidth(200);
        niveauDeStressCombo.setVisible(false); // Initially hidden

        // Qualité de Sommeil
        Text qualiteDeSommeilLabel = new Text("Qualité de Sommeil: ");
        qualiteDeSommeilLabel.setLayoutX(labelX);
        qualiteDeSommeilLabel.setLayoutY(startY + 11 * spacing);
        qualiteDeSommeilLabel.setStyle("-fx-font-size: 14px;");

        Text qualiteDeSommeilText = new Text(dossier.getQualiteDeSommeil());
        qualiteDeSommeilText.setLayoutX(fieldX);
        qualiteDeSommeilText.setLayoutY(startY + 11 * spacing);
        qualiteDeSommeilText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> qualiteDeSommeilCombo = new ComboBox<>();
        qualiteDeSommeilCombo.getItems().addAll("Mauvaise", "Moyenne", "Bonne");
        qualiteDeSommeilCombo.setValue(dossier.getQualiteDeSommeil());
        qualiteDeSommeilCombo.setLayoutX(fieldX);
        qualiteDeSommeilCombo.setLayoutY(startY + 11 * spacing);
        qualiteDeSommeilCombo.setPrefWidth(200);
        qualiteDeSommeilCombo.setVisible(false); // Initially hidden

        // Activité Physique
        Text activitePhysiqueLabel = new Text("Activité Physique: ");
        activitePhysiqueLabel.setLayoutX(labelX);
        activitePhysiqueLabel.setLayoutY(startY + 12 * spacing);
        activitePhysiqueLabel.setStyle("-fx-font-size: 14px;");

        Text activitePhysiqueText = new Text(dossier.getActivitePhysique());
        activitePhysiqueText.setLayoutX(fieldX);
        activitePhysiqueText.setLayoutY(startY + 12 * spacing);
        activitePhysiqueText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> activitePhysiqueCombo = new ComboBox<>();
        activitePhysiqueCombo.getItems().addAll("Marche", "Sport", "Autre", "Aucune");
        activitePhysiqueCombo.setValue(dossier.getActivitePhysique());
        activitePhysiqueCombo.setLayoutX(fieldX);
        activitePhysiqueCombo.setLayoutY(startY + 12 * spacing);
        activitePhysiqueCombo.setPrefWidth(200);
        activitePhysiqueCombo.setVisible(false); // Initially hidden

        // Situation Familiale
        Text situationFamilialeLabel = new Text("Situation Familiale: ");
        situationFamilialeLabel.setLayoutX(labelX);
        situationFamilialeLabel.setLayoutY(startY + 13 * spacing);
        situationFamilialeLabel.setStyle("-fx-font-size: 14px;");

        Text situationFamilialeText = new Text(dossier.getSituationFamiliale());
        situationFamilialeText.setLayoutX(fieldX);
        situationFamilialeText.setLayoutY(startY + 13 * spacing);
        situationFamilialeText.setStyle("-fx-font-size: 14px;");

        ComboBox<String> situationFamilialeCombo = new ComboBox<>();
        situationFamilialeCombo.getItems().addAll("Célibataire", "Marié", "Divorcé", "Veuf");
        situationFamilialeCombo.setValue(dossier.getSituationFamiliale());
        situationFamilialeCombo.setLayoutX(fieldX);
        situationFamilialeCombo.setLayoutY(startY + 13 * spacing);
        situationFamilialeCombo.setPrefWidth(200);
        situationFamilialeCombo.setVisible(false); // Initially hidden

        // Buttons
        Button modifierButton = new Button("Modifier");
        modifierButton.setLayoutX(400);
        modifierButton.setLayoutY(20);
        modifierButton.setPrefWidth(100);
        modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;");

        Button annulerButton = new Button("Annuler");
        annulerButton.setLayoutX(400);
        annulerButton.setLayoutY(60);
        annulerButton.setPrefWidth(100);
        annulerButton.setStyle("-fx-background-color: #1974d9; -fx-text-fill: white;");
        annulerButton.setVisible(false);

        // Handle "Modifier" button click
        modifierButton.setOnAction(event -> {
            if (modifierButton.getText().equals("Modifier")) {
                // Switch to "Enregistrer" mode
                modifierButton.setText("Enregistrer");
                modifierButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");

                // Hide non-editable text fields

                tailleText.setVisible(false);
                poidsText.setVisible(false);
                maladiesText.setVisible(false);
                antecedentsText.setVisible(false);
                asthmatiqueText.setVisible(false);
                suiviDentaireText.setVisible(false);
                antecedentsChirurgicauxText.setVisible(false);
                allergiesText.setVisible(false);
                professionText.setVisible(false);
                niveauDeStressText.setVisible(false);
                qualiteDeSommeilText.setVisible(false);
                activitePhysiqueText.setVisible(false);
                situationFamilialeText.setVisible(false);

                // Show editable fields
                tailleField.setVisible(true);
                poidsField.setVisible(true);
                maladiesCombo.setVisible(true);
                antecedentsCardioCombo.setVisible(true);
                asthmatiqueCombo.setVisible(true);
                suiviDentaireCombo.setVisible(true);
                antecedentsChirurgicauxCombo.setVisible(true);
                allergiesCombo.setVisible(true);
                professionField.setVisible(true);
                niveauDeStressCombo.setVisible(true);
                qualiteDeSommeilCombo.setVisible(true);
                activitePhysiqueCombo.setVisible(true);
                situationFamilialeCombo.setVisible(true);

                // Show "Annuler" button
                annulerButton.setVisible(true);
            } else {
                // Switch back to "Modifier" mode
                modifierButton.setText("Modifier");
                modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;");

                // Hide editable fields
                tailleField.setVisible(false);
                poidsField.setVisible(false);
                maladiesCombo.setVisible(false);
                antecedentsCardioCombo.setVisible(false);
                asthmatiqueCombo.setVisible(false);
                suiviDentaireCombo.setVisible(false);
                antecedentsChirurgicauxCombo.setVisible(false);
                allergiesCombo.setVisible(false);
                professionField.setVisible(false);
                niveauDeStressCombo.setVisible(false);
                qualiteDeSommeilCombo.setVisible(false);
                activitePhysiqueCombo.setVisible(false);
                situationFamilialeCombo.setVisible(false);

                // Show non-editable text fields with updated values
                tailleText.setText( tailleField.getText());
                poidsText.setText( poidsField.getText());
                maladiesText.setText( maladiesCombo.getValue());
                antecedentsText.setText( antecedentsCardioCombo.getValue());
                asthmatiqueText.setText(asthmatiqueCombo.getValue());
                suiviDentaireText.setText( suiviDentaireCombo.getValue());
                antecedentsChirurgicauxText.setText( antecedentsChirurgicauxCombo.getValue());
                allergiesText.setText(allergiesCombo.getValue());
                professionText.setText(professionField.getText());
                niveauDeStressText.setText( niveauDeStressCombo.getValue());
                qualiteDeSommeilText.setText( qualiteDeSommeilCombo.getValue());
                activitePhysiqueText.setText( activitePhysiqueCombo.getValue());
                situationFamilialeText.setText(situationFamilialeCombo.getValue());

                tailleText.setVisible(true);
                poidsText.setVisible(true);
                maladiesText.setVisible(true);
                antecedentsText.setVisible(true);
                asthmatiqueText.setVisible(true);
                suiviDentaireText.setVisible(true);
                antecedentsChirurgicauxText.setVisible(true);
                allergiesText.setVisible(true);
                professionText.setVisible(true);
                niveauDeStressText.setVisible(true);
                qualiteDeSommeilText.setVisible(true);
                activitePhysiqueText.setVisible(true);
                situationFamilialeText.setVisible(true);

                // Hide "Annuler" button
                annulerButton.setVisible(false);

                // Update the dossier object with new values
                dossier.setTaille(Float.parseFloat(tailleField.getText()));
                dossier.setPoids(Float.parseFloat(poidsField.getText()));
                dossier.setMaladies("Oui".equals(maladiesCombo.getValue()));
                dossier.setAntecedentsCardiovasculairesFamiliaux(antecedentsCardioCombo.getValue());
                dossier.setAsthmatique(asthmatiqueCombo.getValue());
                dossier.setSuiviDentaireRegulier(suiviDentaireCombo.getValue());
                dossier.setAntecedentsChirurgicaux(antecedentsChirurgicauxCombo.getValue());
                dossier.setAllergies(allergiesCombo.getValue());
                dossier.setProfession(professionField.getText());
                dossier.setNiveauDeStress(niveauDeStressCombo.getValue());
                dossier.setQualiteDeSommeil(qualiteDeSommeilCombo.getValue());
                dossier.setActivitePhysique(activitePhysiqueCombo.getValue());
                dossier.setSituationFamiliale(situationFamilialeCombo.getValue());

                // Save the updated dossier to the database
                serviceInformations.update(dossier);

                // Optional: Show a confirmation message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("Les informations ont été mises à jour avec succès !");
                alert.showAndWait();
            }
        });

        // Handle "Annuler" button click
        annulerButton.setOnAction(event -> {
            // Reset the "Modifier" button to its original state
            modifierButton.setText("Modifier");
            modifierButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;");

            // Hide editable fields
            tailleField.setVisible(false);
            poidsField.setVisible(false);
            maladiesCombo.setVisible(false);
            antecedentsCardioCombo.setVisible(false);
            asthmatiqueCombo.setVisible(false);
            suiviDentaireCombo.setVisible(false);
            antecedentsChirurgicauxCombo.setVisible(false);
            allergiesCombo.setVisible(false);
            professionField.setVisible(false);
            niveauDeStressCombo.setVisible(false);
            qualiteDeSommeilCombo.setVisible(false);
            activitePhysiqueCombo.setVisible(false);
            situationFamilialeCombo.setVisible(false);

            // Show non-editable text fields with original values
            tailleText.setVisible(true);
            poidsText.setVisible(true);
            maladiesText.setVisible(true);
            antecedentsText.setVisible(true);
            asthmatiqueText.setVisible(true);
            suiviDentaireText.setVisible(true);
            antecedentsChirurgicauxText.setVisible(true);
            allergiesText.setVisible(true);
            professionText.setVisible(true);
            niveauDeStressText.setVisible(true);
            qualiteDeSommeilText.setVisible(true);
            activitePhysiqueText.setVisible(true);
            situationFamilialeText.setVisible(true);

            // Hide "Annuler" button
            annulerButton.setVisible(false);
        });

        // Add all elements to the card
        card.getChildren().addAll(

                tailleLabel, tailleText, tailleField,
                poidsLabel, poidsText, poidsField,
                maladiesLabel, maladiesText, maladiesCombo,
                antecedentsLabel, antecedentsText, antecedentsCardioCombo,
                asthmatiqueLabel, asthmatiqueText, asthmatiqueCombo,
                suiviDentaireLabel, suiviDentaireText, suiviDentaireCombo,
                antecedentsChirurgicauxLabel, antecedentsChirurgicauxText, antecedentsChirurgicauxCombo,
                allergiesLabel, allergiesText, allergiesCombo,
                professionLabel, professionText, professionField,
                niveauDeStressLabel, niveauDeStressText, niveauDeStressCombo,
                qualiteDeSommeilLabel, qualiteDeSommeilText, qualiteDeSommeilCombo,
                activitePhysiqueLabel, activitePhysiqueText, activitePhysiqueCombo,
                situationFamilialeLabel, situationFamilialeText, situationFamilialeCombo,
                modifierButton, annulerButton
        );

        return card;
    }}