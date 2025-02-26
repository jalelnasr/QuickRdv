package tn.esprit.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import javafx.stage.Stage;
import tn.esprit.utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;



import static com.sun.glass.ui.Cursor.setVisible;

public class AfficherUtilisateur {


    @FXML
    private PieChart statistiqueChart;
    @FXML
    private ToggleButton statistiqueButton;
    @FXML
    private ListView<String> listUtilisateur;
    @FXML
    private TextField email, id, mot_de_passe, nom, prenom, role, rechercher, specialite, num_rdv_max;
    @FXML
    private ComboBox<String> sortCriteriaComboBox;

    private int selectedUserId = -1;
    private Connection conn = MyDatabase.getInstance().getCnx();


    @FXML
    void initialize() {
        AfficherUtilisateurs();
        statistiqueChart.setVisible(false);
        specialite.setVisible(false);
        num_rdv_max.setVisible(false);

        // Ajouter un écouteur sur le champ role
        role.textProperty().addListener((observable, oldValue, newValue) -> {
            if ("medecin".equals(newValue)) {
                specialite.setVisible(true);
                num_rdv_max.setVisible(true);
            } else {
                specialite.setVisible(false);
                num_rdv_max.setVisible(false);
            }
        });
    }

    @FXML
    void retourner() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterUtilisateur.fxml"));
            Stage stage = (Stage) listUtilisateur.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            afficherAlerte("Erreur", "Impossible de retourner à la page AjouterUtilisateur.");
        }
    }



   @FXML
    void handleStatistiqueButton() {
        statistiqueChart.setVisible(statistiqueButton.isSelected());
        if (statistiqueButton.isSelected()) {
            loadChartData();
        } else {
            statistiqueChart.getData().clear();
        }
    }

    private void loadChartData() {
        String query = "SELECT role, COUNT(*) as count FROM utilisateur GROUP BY role";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            statistiqueChart.getData().clear();
            int totalUsers = 0;
            Map<String, Integer> roleCountMap = new HashMap<>();

            while (rs.next()) {
                String role = rs.getString("role");
                int count = rs.getInt("count");
                roleCountMap.put(role, count);
                totalUsers += count;
            }

            if (totalUsers == 0) {
                afficherAlerte("Information", "Aucune donnée disponible pour les statistiques.");
                return;
            }

            for (Map.Entry<String, Integer> entry : roleCountMap.entrySet()) {
                String role = entry.getKey();
                int count = entry.getValue();
                double percentage = ((double) count / totalUsers) * 100;

                PieChart.Data data = new PieChart.Data(role + " (" + count + " - " + String.format("%.2f", percentage) + "%)", count);
                statistiqueChart.getData().add(data);
            }

        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors du chargement des statistiques.");
            e.printStackTrace();
        }
    }
    @FXML
    void Rechercher() {
        listUtilisateur.getItems().clear();
        String searchText = rechercher.getText().trim();

        if (searchText.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez saisir un nom et un prénom.");
            return;
        }

        String[] parts = searchText.split("\\s+");
        if (parts.length < 2) {
            afficherAlerte("Erreur", "Veuillez saisir à la fois un nom et un prénom.");
            return;
        }

        String nom = parts[0];
        String prenom = parts[1];
        String query = "SELECT * FROM utilisateur WHERE nom LIKE ? AND prenom LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + nom + "%");
            pstmt.setString(2, "%" + prenom + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listUtilisateur.getItems().add(formatUser(rs));
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la recherche de l'utilisateur.");
            e.printStackTrace();
        }
    }

    @FXML
    void supprimer() {
        if (selectedUserId == -1) {
            afficherAlerte("Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        // Supprimer d'abord de la table medecin si l'utilisateur est un médecin
        String deleteMedecinQuery = "DELETE FROM medecin WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteMedecinQuery)) {
            pstmt.setInt(1, selectedUserId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la suppression des données du médecin.");
            e.printStackTrace();
        }

        // Ensuite, supprimer de la table utilisateur
        String deleteUtilisateurQuery = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteUtilisateurQuery)) {
            pstmt.setInt(1, selectedUserId);
            pstmt.executeUpdate();
            afficherAlerte("Succès", "Utilisateur supprimé avec succès.");
            selectedUserId = -1;
            AfficherUtilisateurs();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la suppression de l'utilisateur.");
            e.printStackTrace();
        }
    }

    @FXML
    void modifier() {
        if (selectedUserId == -1) {
            afficherAlerte("Erreur", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        String newRole = role.getText();
        String query = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nom.getText());
            pstmt.setString(2, prenom.getText());
            pstmt.setString(3, email.getText());
            pstmt.setString(4, mot_de_passe.getText());
            pstmt.setString(5, newRole);
            pstmt.setInt(6, selectedUserId);
            pstmt.executeUpdate();

            // Supprimer l'ID des tables de rôles non concernés
            String deleteMedecinQuery = "DELETE FROM medecin WHERE id = ?";
            String deletePharmacienQuery = "DELETE FROM pharmacien WHERE id = ?";
            String deleteAdminQuery = "DELETE FROM administrateur WHERE id = ?";

            try (PreparedStatement deleteMedecinStmt = conn.prepareStatement(deleteMedecinQuery);
                 PreparedStatement deletePharmacienStmt = conn.prepareStatement(deletePharmacienQuery);
                 PreparedStatement deleteAdminStmt = conn.prepareStatement(deleteAdminQuery)) {

                deleteMedecinStmt.setInt(1, selectedUserId);
                deletePharmacienStmt.setInt(1, selectedUserId);
                deleteAdminStmt.setInt(1, selectedUserId);

                deleteMedecinStmt.executeUpdate();
                deletePharmacienStmt.executeUpdate();
                deleteAdminStmt.executeUpdate();
            }

            // Insérer l'ID dans la table correspondante au nouveau rôle
            switch (newRole) {
                case "medecin":
                    // Vérifier si l'ID existe déjà dans la table medecin
                    String checkMedecinQuery = "SELECT * FROM medecin WHERE id = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkMedecinQuery)) {
                        checkStmt.setInt(1, selectedUserId);
                        ResultSet rs = checkStmt.executeQuery();
                        if (rs.next()) {
                            // Mettre à jour les données existantes
                            String updateMedecinQuery = "UPDATE medecin SET specialite = ?, num_rdv_max = ? WHERE id = ?";
                            try (PreparedStatement updateStmt = conn.prepareStatement(updateMedecinQuery)) {
                                updateStmt.setString(1, specialite.getText());
                                updateStmt.setString(2, num_rdv_max.getText());
                                updateStmt.setInt(3, selectedUserId);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            // Insérer de nouvelles données
                            String insertMedecinQuery = "INSERT INTO medecin (id, specialite, num_rdv_max) VALUES (?, ?, ?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertMedecinQuery)) {
                                insertStmt.setInt(1, selectedUserId);
                                insertStmt.setString(2, specialite.getText());
                                insertStmt.setString(3, num_rdv_max.getText());
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                    break;

                case "pharmacien":
                    // Insérer l'ID dans la table pharmacien (si ce n'est pas déjà fait)
                    String checkPharmacienQuery = "SELECT * FROM pharmacien WHERE id = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkPharmacienQuery)) {
                        checkStmt.setInt(1, selectedUserId);
                        ResultSet rs = checkStmt.executeQuery();
                        if (!rs.next()) {
                            // Insérer l'ID dans la table pharmacien
                            String insertPharmacienQuery = "INSERT INTO pharmacien (id) VALUES (?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertPharmacienQuery)) {
                                insertStmt.setInt(1, selectedUserId);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                    break;

                case "administrateur":
                    // Insérer l'ID dans la table administrateur (si ce n'est pas déjà fait)
                    String checkAdminQuery = "SELECT * FROM administrateur WHERE id = ?";
                    try (PreparedStatement checkStmt = conn.prepareStatement(checkAdminQuery)) {
                        checkStmt.setInt(1, selectedUserId);
                        ResultSet rs = checkStmt.executeQuery();
                        if (!rs.next()) {
                            // Insérer l'ID dans la table administrateur
                            String insertAdminQuery = "INSERT INTO administrateur (id) VALUES (?)";
                            try (PreparedStatement insertStmt = conn.prepareStatement(insertAdminQuery)) {
                                insertStmt.setInt(1, selectedUserId);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                    break;

                default:
                    // Si le rôle n'est ni "medecin", ni "pharmacien", ni "administrateur", ne rien faire
                    break;
            }

            afficherAlerte("Succès", "Utilisateur modifié avec succès.");
            AfficherUtilisateurs();
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la modification de l'utilisateur.");
            e.printStackTrace();
        }
    }

    @FXML
    void ok() {
        String query = "SELECT * FROM utilisateur WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, Integer.parseInt(id.getText()));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                selectedUserId = rs.getInt("id");
                nom.setText(rs.getString("nom"));
                prenom.setText(rs.getString("prenom"));
                email.setText(rs.getString("email"));
                mot_de_passe.setText(rs.getString("mot_de_passe"));
                role.setText(rs.getString("role"));

                // Charger les données du médecin si l'utilisateur est un médecin
                if ("medecin".equals(rs.getString("role"))) {
                    loadMedecinData(selectedUserId);
                    specialite.setVisible(true);
                    num_rdv_max.setVisible(true);
                } else {
                    specialite.setVisible(false);
                    num_rdv_max.setVisible(false);
                }
            } else {
                afficherAlerte("Erreur", "Utilisateur non trouvé");
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la récupération des informations");
            e.printStackTrace();
        }
    }

    private void loadMedecinData(int medecinId) {
        String query = "SELECT * FROM medecin WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, medecinId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                specialite.setText(rs.getString("specialite"));
                num_rdv_max.setText(rs.getString("num_rdv_max"));
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors du chargement des données du médecin.");
            e.printStackTrace();
        }
    }

    @FXML
    void trier() {
        listUtilisateur.getItems().clear();
        String critere = sortCriteriaComboBox.getValue();

        if ("etatinitial".equals(critere)) {
            AfficherUtilisateurs();
            return;
        }

        String query;
        if ("Rôle".equals(critere)) {
            query = "SELECT * FROM utilisateur ORDER BY FIELD(role, 'Administrateur', 'Médecin', 'Pharmacien', 'Patient')";
        } else {
            query = "SELECT * FROM utilisateur ORDER BY " + critere;
        }

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                listUtilisateur.getItems().add(formatUser(rs));
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors du tri des utilisateurs.");
            e.printStackTrace();
        }
    }

    private void AfficherUtilisateurs() {
        listUtilisateur.getItems().clear();
        String query = "SELECT * FROM utilisateur";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                listUtilisateur.getItems().add(formatUser(rs));
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur", "Erreur lors de la récupération des utilisateurs.");
            e.printStackTrace();
        }
    }

    private String formatUser(ResultSet rs) throws SQLException {
        return "ID: " + rs.getInt("id") + " | " +
                "Nom: " + rs.getString("nom") + " | " +
                "Prénom: " + rs.getString("prenom") + " | " +
                "Email: " + rs.getString("email") + " | " +
                "Rôle: " + rs.getString("role");
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}