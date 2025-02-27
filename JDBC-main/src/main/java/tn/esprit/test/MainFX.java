package tn.esprit.test;



import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOrdonnance.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Ordonnance");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /*@Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterMedicament.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Ajouter Medicament");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }*/

    /*@Override
    public void start(Stage primaryStage) {
        try {
            // Chargez le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ValidationOrdonnance.fxml"));

            // Chargez le root de l'interface
            AnchorPane root = loader.load();

            // Créez une scène et affectez-la à la fenêtre principale
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            // Définir un titre pour la fenêtre
            primaryStage.setTitle("Validation Ordonnance");

            // Affichez la fenêtre
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


}
