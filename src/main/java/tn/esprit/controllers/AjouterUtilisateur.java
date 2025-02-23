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

public class AjouterUtilisateur {

    @FXML
    private TextField email;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField role;

    @FXML
    void afficherlist(ActionEvent event) {
        loadPage("/AfficherUtilisateur.fxml", "Afficher la liste");
    }

    @FXML
    void ajouter(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = mot_de_passe.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();
        String userRole = role.getText().trim();

        if (userEmail.isEmpty() || userPassword.isEmpty() || userNom.isEmpty() || userPrenom.isEmpty() || userRole.isEmpty()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (!userEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            afficherAlerte("Erreur", "Veuillez entrer une adresse email valide.");
            return;
        }

        Connection conn = MyDatabase.getInstance().getCnx();
        if (conn == null) {
            afficherAlerte("Erreur", "Connexion à la base de données fermée !");
            return;
        }

        String query = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userNom);
            stmt.setString(2, userPrenom);
            stmt.setString(3, userEmail);
            stmt.setString(4, userPassword);
            stmt.setString(5, userRole);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                afficherAlerte("Succès", "Utilisateur ajouté avec succès !");
                viderChamps();
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Problème lors de l'ajout : " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de charger la page : " + fxmlPath);
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) email.getScene().getWindow();
        stage.close();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void viderChamps() {
        email.clear();
        mot_de_passe.clear();
        nom.clear();
        prenom.clear();
        role.clear();
    }
}
