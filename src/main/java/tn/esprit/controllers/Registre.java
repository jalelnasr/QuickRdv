package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import tn.esprit.utils.MyDatabase;

public class Registre {

    @FXML
    private TextField email;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;


    @FXML
    void SignIn(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = mot_de_passe.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();


        if (userEmail.isEmpty() || userPassword.isEmpty() || userNom.isEmpty() || userPrenom.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }


        if (insertUser(userNom, userPrenom, userEmail, userPassword, "Patient")) {
            showAlert("Succès", "Inscription réussie ! Vous pouvez vous connecter.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Échec de l'inscription. Veuillez réessayer.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    void display_page(ActionEvent event) {
        closeWindow();
    }


    @FXML
    void retourner(ActionEvent event) {
        loadPage("/LoginUser.fxml", "Login");
        closeWindow();
    }


    private boolean insertUser(String nom, String prenom, String email, String password, String role) {
        String query = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Problème lors de l'insertion en base : " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    private void loadPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page : " + fxmlPath, Alert.AlertType.ERROR);
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) nom.getScene().getWindow();
        stage.close();
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
