package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

    @FXML
    private void goToRendezVous() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/rendezVousMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Rendez-Vous");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAnalyse() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/analyseMenu.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestion des Analyses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}