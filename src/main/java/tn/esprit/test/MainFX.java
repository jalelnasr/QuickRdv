package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Load the AjouterRendezVous.fxml by default when the application starts
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterRendezVous.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Ajouter Rendez-vous");
        stage.setScene(scene);
        stage.show();
    }

    // Method to open the AfficherRendezVous.fxml view
    public static void showAfficherRendezVous() {
        try {
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/afficherRendezVous.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Rendez-vous");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the ModifierRendezVous.fxml view


    public static void main(String[] args) {
        launch(args);
    }
}