package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

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
    private TextField DateNaissance;

    @FXML
    private TextField adresse;

    @FXML
    void SignIn(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = mot_de_passe.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();
        String naissance = DateNaissance.getText().trim();
        String userAdresse = adresse.getText().trim();

        // Vérification des champs obligatoires
        if (userEmail.isEmpty() || userPassword.isEmpty() || userNom.isEmpty() ||
                userPrenom.isEmpty() || naissance.isEmpty() || userAdresse.isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Vérification de l’email
        if (!userEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert("Erreur", "Veuillez entrer une adresse email valide.", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du format de la date (ex: YYYY-MM-DD)
        if (!naissance.matches("\\d{4}-\\d{2}-\\d{2}")) {
            showAlert("Erreur", "Format de la date incorrect (AAAA-MM-JJ) !", Alert.AlertType.ERROR);
            return;
        }

        // Insertion de l'utilisateur et récupération de l'ID généré
        int userId = insertUser(userNom, userPrenom, userEmail, userPassword, "Patient");

        if (userId != -1) {
            // Insertion des données dans la table patient
            if (insertPatient(userId, naissance, userAdresse)) {
                showAlert("Succès", "Inscription réussie ! Vous pouvez vous connecter.", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Erreur", "Échec de l'ajout du patient.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Erreur", "Échec de l'inscription.", Alert.AlertType.ERROR);
        }
    }

    private int insertUser(String nom, String prenom, String email, String password, String role) {
        String query = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";
        int userId = -1;

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, role);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Problème lors de l'insertion de l'utilisateur : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
        return userId;
    }

    private boolean insertPatient(int userId, String dateNaissance, String adresse) {
        String query = "INSERT INTO patient (id, dateNaissance, adresse) VALUES (?, ?, ?)";

        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            stmt.setString(2, dateNaissance);
            stmt.setString(3, adresse);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showAlert("Erreur SQL", "Problème lors de l'insertion du patient : " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
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

    private void loadPage(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de charger la page : " + fxmlPath, Alert.AlertType.ERROR);
            e.printStackTrace();
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

    private void clearFields() {
        email.clear();
        mot_de_passe.clear();
        nom.clear();
        prenom.clear();
        DateNaissance.clear();
        adresse.clear();
    }
}
