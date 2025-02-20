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


    public void start(Stage primaryStage) {
        try {
            //FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutInformationsGenerales.fxml"));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutAssurance.fxml"));

            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            //primaryStage.setTitle("Ajouter Informations Generales");
            primaryStage.setTitle("Ajouter Assurance");

            primaryStage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}