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

public class AjouterUtilisateur {

    @FXML
    private TextField email, nom, prenom, specialite, num_rdv_max;

    @FXML
    private PasswordField mot_de_passe;

    @FXML
    private ComboBox<String> role;

    @FXML
    void initialize() {
        role.getItems().addAll("medecin", "pharmacien", "administrateur");
        specialite.setDisable(true);
        num_rdv_max.setDisable(true);
    }

    @FXML
    void ajouter(ActionEvent event) {
        String userEmail = email.getText().trim();
        String userPassword = mot_de_passe.getText().trim();
        String userNom = nom.getText().trim();
        String userPrenom = prenom.getText().trim();
        String userRole = role.getValue();

        if (userEmail.isEmpty() || userPassword.isEmpty() || userNom.isEmpty() || userPrenom.isEmpty() || userRole == null) {
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

        String insertUserQuery = "INSERT INTO utilisateur (nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userNom);
            stmt.setString(2, userPrenom);
            stmt.setString(3, userEmail);
            stmt.setString(4, userPassword);
            stmt.setString(5, userRole);
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                    if ("medecin".equals(userRole)) {
                        String userSpecialite = specialite.getText().trim();
                        String userNumRdvMax = num_rdv_max.getText().trim();

                        if (userSpecialite.isEmpty() || userNumRdvMax.isEmpty()) {
                            afficherAlerte("Erreur", "La spécialité et le nombre maximal de rendez-vous sont requis pour un médecin !");
                            return;
                        }

                        try {
                            int numMax = Integer.parseInt(userNumRdvMax);
                            if (!ajouterMedecin(conn, userId, userSpecialite, numMax)) {
                                afficherAlerte("Erreur", "Impossible d'ajouter les informations du médecin.");
                                return;
                            }
                        } catch (NumberFormatException e) {
                            afficherAlerte("Erreur", "Le nombre maximal de rendez-vous doit être un entier !");
                            return;
                        }
                    } else {
                        if (!ajouterDansTableRole(conn, userId, userRole)) {
                            afficherAlerte("Erreur", "L'utilisateur a été ajouté, mais son rôle n'a pas été attribué.");
                            return;
                        }
                    }

                    afficherAlerte("Succès", "Utilisateur ajouté avec succès !");
                    viderChamps();
                }
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Problème lors de l'ajout de l'utilisateur.");
            e.printStackTrace();
        }
    }

    private boolean ajouterDansTableRole(Connection conn, int userId, String userRole) {
        String insertRoleQuery = switch (userRole) {
            case "pharmacien" -> "INSERT INTO pharmacien (id) VALUES (?)";
            case "administrateur" -> "INSERT INTO administrateur (id) VALUES (?)";
            default -> null;
        };

        if (insertRoleQuery == null) {
            afficherAlerte("Erreur", "Rôle non reconnu !");
            return false;
        }

        try (PreparedStatement roleStmt = conn.prepareStatement(insertRoleQuery)) {
            roleStmt.setInt(1, userId);
            roleStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Impossible d'ajouter l'utilisateur dans la table " + userRole + ".");
            e.printStackTrace();
            return false;
        }
    }

    private boolean ajouterMedecin(Connection conn, int userId, String specialite, int numRdvMax) {
        String insertMedecinQuery = "INSERT INTO medecin (id, specialite, num_rdv_max) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertMedecinQuery)) {
            stmt.setInt(1, userId);
            stmt.setString(2, specialite);
            stmt.setInt(3, numRdvMax);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Impossible d'ajouter les informations du médecin.");
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void onRoleChanged(ActionEvent event) {
        boolean isMedecin = "medecin".equals(role.getValue());
        specialite.setDisable(!isMedecin);
        num_rdv_max.setDisable(!isMedecin);
    }

    // Méthode pour afficher la liste des utilisateurs
    @FXML
    void afficherlist(ActionEvent event) {
        loadPage("/AfficherUtilisateur.fxml", "Afficher la Liste des Utilisateurs");
    }

    // Méthode pour afficher une autre page et fermer la fenêtre actuelle
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

    // Méthode pour fermer la fenêtre actuelle
    @FXML
    void display_page(ActionEvent event) {
        closeWindow();
    }

    @FXML
    void retourner(ActionEvent event) {
        loadPage("/LoginUser.fxml", "Login");
        closeWindow();
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
        role.getSelectionModel().clearSelection();
        specialite.clear();
        num_rdv_max.clear();
        specialite.setDisable(true);
        num_rdv_max.setDisable(true);
    }
}
