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
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.input.MouseEvent;
import tn.esprit.utils.MyDatabase;

public class LoginUser {

    @FXML
    private TextField email;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    void SeConnecter(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = mot_de_passe.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return;
        }


        String role = checkRoleInDB(userEmail, userPassword);
        if (role != null) {
            switch (role) {
                case "medecin":
                    loadPage("/Medecin.fxml", "Page Médecin");
                    break;
                case "administrateur":
                    loadPage("/AJouterUtilisateur.fxml", "Page Administrateur");
                    break;
                case "pharmacien":
                    loadPage("/Pharmacien.fxml", "Page Pharmacien");
                    break;
                case "patient":
                    loadPage("/Pateint.fxml", "Page Patient");
                    break;
                default:
                    showAlert("Erreur", "Rôle non reconnu", Alert.AlertType.ERROR);
                    return;
            }
            closeWindow();
        } else {
            showAlert("Erreur", "Email ou mot de passe incorrect", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void SignUp(ActionEvent event) {
        loadPage("/Registre.fxml", "Inscription");
        closeWindow();
    }

    private String checkRoleInDB(String email, String password) {
        String role = null;
        String query = "SELECT role FROM utilisateur WHERE email = ? AND mot_de_passe = ?";

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Problème de connexion à la base de données", Alert.AlertType.ERROR);
        }
        return role;
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

    @FXML
    void display_page(MouseEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*@FXML
    void mot_de_passe_oublié(ActionEvent event) {
        // Laissez vide ou implémentez la logique souhaitée
    }*/
}