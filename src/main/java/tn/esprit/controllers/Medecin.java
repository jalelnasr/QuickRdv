package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import tn.esprit.utils.MyDatabase;

public class Medecin {

    @FXML
    private TextField Disponibilite;

    @FXML
    private TextField Specialite;

    @FXML
    private TextField nom_medecin;

    @FXML
    private TextField num_rdv_max;

    @FXML
    private TextField numeroRPPS;

    private Connection conn = MyDatabase.getInstance().getCnx();
    private String userEmail;
    private String id; // Stocker l'ID du médecin

    public void setUserEmail(String email) {
        if (email == null || email.isEmpty()) {
            afficherAlerte("Erreur", "L'email de l'utilisateur est manquant.");
            return;
        }
        this.userEmail = email;
        chargerInformationsMedecin();
    }

    /**
     * Charge les informations du médecin à partir de la base de données
     */
    private void chargerInformationsMedecin() {
        if (userEmail == null || userEmail.isEmpty()) {
            return;
        }

        String query = "SELECT u.nom, m.specialite, m.disponibilite, m.num_rdv_max, m.numeroRPPS, m.id " +
                "FROM utilisateur u " +
                "LEFT JOIN medecin m ON u.id = m.id " +
                "WHERE u.email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nom_medecin.setText(rs.getString("nom"));
                id = rs.getString("id"); // Stocker l'ID pour mise à jour

                // Vérifier si les informations sont manquantes et alerter l'utilisateur
                String specialite = rs.getString("specialite");
                String disponibilite = rs.getString("disponibilite");
                String numRdvMax = rs.getString("num_rdv_max");
                String numeroRPPS = rs.getString("numeroRPPS");

                Specialite.setText(specialite != null ? specialite : "");
                Disponibilite.setText(disponibilite != null ? disponibilite : "");
                num_rdv_max.setText(numRdvMax != null ? numRdvMax : "");
                /*numeroRPPS.setText(numeroRPPS != null ? numeroRPPS : "");*/

                // Si tous les champs sont NULL ou vides, informer l'utilisateur de compléter les informations
                if (specialite == null && disponibilite == null && numRdvMax == null && numeroRPPS == null) {
                    afficherAlerte("Informations manquantes", "Les informations du médecin sont manquantes, veuillez les ajouter.");
                }
            } else {
                afficherAlerte("Erreur", "Utilisateur non trouvé.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Impossible de récupérer les informations du médecin.");
        }
    }

    @FXML
    void Valider(ActionEvent event) {
        String specialiteText = Specialite.getText().trim();
        String disponibiliteText = Disponibilite.getText().trim();
        String numRdvMaxText = num_rdv_max.getText().trim();
        String numeroRPPSText = numeroRPPS.getText().trim();

        // Ajouter des valeurs par défaut si les champs sont vides
        if (specialiteText.isEmpty()) specialiteText = "Spécialité non définie";
        if (disponibiliteText.isEmpty()) disponibiliteText = "Non disponible";
        if (numRdvMaxText.isEmpty()) numRdvMaxText = "0";
        if (numeroRPPSText.isEmpty()) numeroRPPSText = "Non défini";

        if (id == null) {
            afficherAlerte("Erreur", "Utilisateur non trouvé.");
            return;
        }

        // Vérifier si l'utilisateur est déjà enregistré comme médecin
        String checkQuery = "SELECT COUNT(*) FROM medecin WHERE id = ?";
        boolean existeDeja = false;

        try (PreparedStatement stmt = conn.prepareStatement(checkQuery)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                existeDeja = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur SQL", "Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
            return;
        }

        if (existeDeja) {
            // Mise à jour des informations du médecin
            String updateQuery = "UPDATE medecin SET specialite = ?, disponibilite = ?, num_rdv_max = ?, numeroRPPS = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, specialiteText);
                stmt.setString(2, disponibiliteText);
                stmt.setString(3, numRdvMaxText);
                stmt.setString(4, numeroRPPSText);
                stmt.setString(5, id);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    afficherAlerte("Succès", "Les informations du médecin ont été mises à jour.");
                } else {
                    afficherAlerte("Erreur", "Aucune mise à jour effectuée.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                afficherAlerte("Erreur SQL", "Erreur lors de la mise à jour des informations du médecin : " + e.getMessage());
            }
        } else {
            // Insertion des nouvelles informations
            String insertQuery = "INSERT INTO medecin (id, specialite, disponibilite, num_rdv_max, numeroRPPS) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                stmt.setString(1, id);
                stmt.setString(2, specialiteText);
                stmt.setString(3, disponibiliteText);
                stmt.setString(4, numRdvMaxText);
                stmt.setString(5, numeroRPPSText);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    afficherAlerte("Succès", "Les informations du médecin ont été enregistrées.");
                } else {
                    afficherAlerte("Erreur", "Erreur lors de l'ajout des informations du médecin.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                afficherAlerte("Erreur SQL", "Erreur lors de l'ajout des informations du médecin : " + e.getMessage());
            }
        }
    }


    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void retourner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("LoginUser");
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
