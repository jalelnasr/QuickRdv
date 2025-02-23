package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import tn.esprit.utils.MyDatabase;

public class Pateint {

    @FXML
    private TextField nom_Pateint;

    @FXML
    private TextField motDePasseField;

    private String userEmail;


    public void setUserEmail(String email) {
        this.userEmail = email;
        fetchPatientName();
    }


    private void fetchPatientName() {
        String query = "SELECT nom FROM utilisateur WHERE email = ? AND mot_de_passe = ? AND role = 'Patient'";
        try (Connection conn = MyDatabase.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query)) {


            String userPassword = motDePasseField.getText();
            stmt.setString(1, userEmail);
            stmt.setString(2, userPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nom_Pateint.setText(rs.getString("nom"));
            } else {
                System.out.println("Aucun patient trouvé avec cet email et mot de passe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retourner(ActionEvent event) {
        retournerALogin(event);
    }

    // Méthode pour revenir à la page de connexion
    private void retournerALogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
