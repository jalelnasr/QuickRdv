package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;
import javafx.stage.Stage;
import tn.esprit.utils.MyDatabase;

public class AfficherUtilisateur {

    @FXML
    private TextField email;

    @FXML
    private TextField id;

    @FXML
    private ListView<String> listUtilisateur;

    @FXML
    private TextField mot_de_passe;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField role;

    private int selectedUserId = -1; // ID de l'utilisateur sélectionné
    private Connection conn = MyDatabase.getInstance().getCnx(); // Connexion à la base de données

    @FXML
    void initialize() {
        afficherUtilisateurs(); // Afficher la liste des utilisateurs lors de l'initialisation
    }

    // Méthode pour afficher la liste des utilisateurs dans la ListView
    private void afficherUtilisateurs() {
        listUtilisateur.getItems().clear();  // Clear the list before loading data

        String query = "SELECT * FROM utilisateur";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String user = "ID: " + rs.getInt("id") + " | " +
                        "Nom: " + rs.getString("nom") + " | " +
                        "Prénom: " + rs.getString("prenom") + " | " +
                        "Email: " + rs.getString("email") + " | " +
                        "Mot de passe: " + rs.getString("mot_de_passe") + " | " +
                        "Rôle: " + rs.getString("role");
                listUtilisateur.getItems().add(user);  // Ajouter l'utilisateur à la ListView
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la récupération des utilisateurs.");
            e.printStackTrace();
        }
    }

    // Méthode pour vider les champs de saisie
    private void viderChamps() {
        id.clear();
        nom.clear();
        prenom.clear();
        email.clear();
        mot_de_passe.clear();
        role.clear();
        selectedUserId = -1;  // Réinitialiser l'ID sélectionné
    }

    // Méthode pour afficher les alertes
    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    void selectionnerUtilisateur(MouseEvent event) {
        String selectedUser = listUtilisateur.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            return;
        }

        try {

            String[] parts = selectedUser.split("\\|");
            int userId = Integer.parseInt(parts[0].replace("ID:", "").trim());

            String query = "SELECT * FROM utilisateur WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    id.setText(String.valueOf(rs.getInt("id")));
                    nom.setText(rs.getString("nom"));
                    prenom.setText(rs.getString("prenom"));
                    email.setText(rs.getString("email"));
                    mot_de_passe.setText(rs.getString("mot_de_passe"));
                    role.setText(rs.getString("role"));
                    selectedUserId = userId;
                }
            }
        } catch (NumberFormatException | SQLException e) {
            afficherAlerte("Erreur", "Impossible de récupérer les détails de l'utilisateur.");
            e.printStackTrace();
        }
    }


    @FXML
    void ok(ActionEvent event) {
        String userIdText = id.getText().trim();

        if (userIdText.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez entrer un ID.");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdText);
            String query = "SELECT * FROM utilisateur WHERE id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {

                    nom.setText(rs.getString("nom"));
                    prenom.setText(rs.getString("prenom"));
                    email.setText(rs.getString("email"));
                    mot_de_passe.setText(rs.getString("mot_de_passe"));
                    role.setText(rs.getString("role"));
                    selectedUserId = userId;
                } else {
                    afficherAlerte("Erreur", "Aucun utilisateur trouvé avec cet ID.");
                    viderChamps();
                    selectedUserId = -1;
                }
            }
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "L'ID doit être un nombre.");
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la récupération des données.");
            e.printStackTrace();
        }
    }


    @FXML
    void modifier(MouseEvent event) {
        if (selectedUserId == -1) {
            afficherAlerte("Erreur", "Sélectionnez un utilisateur à modifier.");
            return;
        }

        String queryUpdateUser = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";

        try (PreparedStatement stmtUpdateUser = conn.prepareStatement(queryUpdateUser)) {
            stmtUpdateUser.setString(1, nom.getText());
            stmtUpdateUser.setString(2, prenom.getText());
            stmtUpdateUser.setString(3, email.getText());
            stmtUpdateUser.setString(4, mot_de_passe.getText());
            stmtUpdateUser.setString(5, role.getText());
            stmtUpdateUser.setInt(6, selectedUserId);
            stmtUpdateUser.executeUpdate();

            afficherAlerte("Succès", "Utilisateur modifié avec succès !");
            afficherUtilisateurs();
            viderChamps();

        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la modification de l'utilisateur.");
            e.printStackTrace();
        }
    }


    @FXML
    void supprimer(MouseEvent event) {
        if (selectedUserId == -1) {
            afficherAlerte("Erreur", "Sélectionnez un utilisateur à supprimer.");
            return;
        }

        String queryDeleteUser = "DELETE FROM utilisateur WHERE id = ?";

        try (PreparedStatement stmtDeleteUser = conn.prepareStatement(queryDeleteUser)) {
            stmtDeleteUser.setInt(1, selectedUserId);
            int rowsDeleted = stmtDeleteUser.executeUpdate();

            if (rowsDeleted > 0) {
                afficherAlerte("Succès", "Utilisateur supprimé avec succès !");
                afficherUtilisateurs();  // Mettre à jour la liste des utilisateurs
                viderChamps();  // Vider les champs après suppression
                selectedUserId = -1;  // Réinitialiser l'ID sélectionné
            } else {
                afficherAlerte("Erreur", "Erreur lors de la suppression de l'utilisateur.");
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la suppression de l'utilisateur.");
            e.printStackTrace();
        }
    }

    @FXML
    void retourner(ActionEvent event) {

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Charger la fenêtre précédente ou une autre fenêtre (ajustez le chemin du FXML en conséquence)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml")); // Remplacez par le bon chemin de votre fichier FXML
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("LoginUser");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Erreur lors du chargement de la fenêtre.");
        }
    }
}
