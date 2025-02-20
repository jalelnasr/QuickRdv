package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import tn.esprit.models.InformationsGenerales;
import tn.esprit.services.ServiceInformationsGenerales;

public class AjoutInformationsGenerales {

    @FXML
    private ComboBox<String> activiteCombo;

    @FXML
    private ComboBox<String> allergiesCombo;

    @FXML
    private ComboBox<String> asthmeCombo;

    @FXML
    private ComboBox<String> cardioCombo;

    @FXML
    private ComboBox<String> chirurgieCombo;

    @FXML
    private ComboBox<String> dentaireCombo;

    @FXML
    private ComboBox<String> maladiesCombo;

    @FXML
    private TextField poidsField;

    @FXML
    private TextField professionField;

    @FXML
    private ComboBox<String> situationCombo;

    @FXML
    private ComboBox<String> sommeilCombo;

    @FXML
    private ComboBox<String> stressCombo;

    @FXML
    private TextField tailleField;

    private ServiceInformationsGenerales sig = new ServiceInformationsGenerales();

    @FXML
    void Save(ActionEvent event) {
        try {
            // Retrieve values from input fields
            float taille = Float.parseFloat(tailleField.getText().trim());
            float poids = Float.parseFloat(poidsField.getText().trim());
            String profession = professionField.getText().trim();

            // Retrieve values from combo boxes
            boolean maladies = "Oui".equalsIgnoreCase(maladiesCombo.getValue());
            String antecedentsCardio = cardioCombo.getValue();
            String asthmatique = asthmeCombo.getValue();
            String suiviDentaire = dentaireCombo.getValue();
            String antecedentsChirurgicaux = chirurgieCombo.getValue();
            String allergies = allergiesCombo.getValue();
            String niveauDeStress = stressCombo.getValue();
            String qualiteDeSommeil = sommeilCombo.getValue();
            String activitePhysique = activiteCombo.getValue();
            String situationFamiliale = situationCombo.getValue();

            // Validate required fields
            if (taille <= 0 || poids <= 0 || profession.isEmpty() || activitePhysique == null || situationFamiliale == null) {
                throw new IllegalArgumentException("Veuillez remplir tous les champs obligatoires.");
            }

            // Create a new `InformationsGenerales` instance
            InformationsGenerales newInfo = new InformationsGenerales();
            newInfo.setIdPatient(1);
            newInfo.setTaille(taille);
            newInfo.setPoids(poids);
            newInfo.setProfession(profession);
            newInfo.setMaladies(maladies);
            newInfo.setAntecedentsCardiovasculairesFamiliaux(antecedentsCardio);
            newInfo.setAsthmatique(asthmatique);
            newInfo.setSuiviDentaireRegulier(suiviDentaire);
            newInfo.setAntecedentsChirurgicaux(antecedentsChirurgicaux);
            newInfo.setAllergies(allergies);
            newInfo.setNiveauDeStress(niveauDeStress);
            newInfo.setQualiteDeSommeil(qualiteDeSommeil);
            newInfo.setActivitePhysique(activitePhysique);
            newInfo.setSituationFamiliale(situationFamiliale);

            // Save data in the database
            sig.add(newInfo);

            // Show confirmation alert
            showAlert(Alert.AlertType.CONFIRMATION, "Succès", "Dossier médical ajouté avec succès !");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Format invalide", "Veuillez entrer des valeurs valides pour la taille et le poids.");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Champs obligatoires", e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du dossier médical.");
        }
    }

    // Utility method to display alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
