package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.models.Analyse;
import tn.esprit.services.ServiceAnalyse;

import java.io.IOException;
import java.util.Optional;

public class AfficherAnalyse {

    @FXML
    private ListView<Analyse> analyseListView;

    @FXML
    private Button modifier;

    @FXML
    private Button supprimer;

    private ServiceAnalyse serviceAnalyse = new ServiceAnalyse();

    @FXML
    public void initialize() {
        refreshAnalyseList();
    }

    private void refreshAnalyseList() {
        analyseListView.getItems().clear();
        analyseListView.getItems().addAll(serviceAnalyse.getAll());
    }

    @FXML
    private void modifierAnalyse() {
        Analyse selectedAnalyse = analyseListView.getSelectionModel().getSelectedItem();
        if (selectedAnalyse != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierAnalyse.fxml"));
                Parent root = loader.load();

                ModifierAnalyse controller = loader.getController();
                controller.setAnalyse(selectedAnalyse); // Passer l’analyse sélectionnée

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Analyse");
                stage.showAndWait(); // Attendre la fermeture pour rafraîchir

                refreshAnalyseList(); // Rafraîchir la liste après modification
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erreur lors du chargement de modifierAnalyse.fxml: " + e.getMessage());
            }
        } else {
            showAlert("Erreur", "Aucune analyse sélectionnée.");
        }
    }

    @FXML
    private void supprimer() {
        Analyse selectedAnalyse = analyseListView.getSelectionModel().getSelectedItem();
        if (selectedAnalyse != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette analyse ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceAnalyse.delete(selectedAnalyse);
                refreshAnalyseList();
                showAlert("Succès", "Analyse supprimée avec succès !");
            }
        } else {
            showAlert("Erreur", "Aucune analyse sélectionnée.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}