package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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


    public void setUserEmail(String email) {
        if (email == null || email.isEmpty()) {
            afficherAlerte("Erreur", "L'email de l'utilisateur est manquant.");
            return;
        }
        this.userEmail = email;
        chargerNomMedecin();
    }

    private void chargerNomMedecin() {
        if (userEmail == null || userEmail.isEmpty()) {
            return;
        }

        String query = "SELECT nom FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nom_medecin.setText(rs.getString("nom"));
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


        if (specialiteText.isEmpty() || disponibiliteText.isEmpty() || numRdvMaxText.isEmpty() || numeroRPPSText.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs.");
            return;
        }


        String idUtilisateur = obtenirIdUtilisateur();

        if (idUtilisateur == null) {
            afficherAlerte("Erreur", "Utilisateur non trouvé.");
            return;
        }


        String query = "INSERT INTO medecin (specialite, disponibilite, num_rdv_max, numeroRPPS, id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, specialiteText);
            stmt.setString(2, disponibiliteText);
            stmt.setString(3, numRdvMaxText);
            stmt.setString(4, numeroRPPSText);
            stmt.setString(5, idUtilisateur);


            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                afficherAlerte("Succès", "Les informations du médecin ont été enregistrées.");
            } else {
                afficherAlerte("Erreur", "Erreur lors de l'ajout des informations du médecin. Aucune ligne insérée.");
            }
        } catch (SQLException e) {
            // Affichage détaillé de l'erreur SQL
            e.printStackTrace();
            afficherAlerte("Erreur SQL", "Erreur lors de l'ajout des informations du médecin : " + e.getMessage());
        }
    }


    private String obtenirIdUtilisateur() {
        String query = "SELECT id FROM utilisateur WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            } else {
                afficherAlerte("Erreur", "Utilisateur non trouvé pour l'email : " + userEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur SQL", "Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }
        return null;
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
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
