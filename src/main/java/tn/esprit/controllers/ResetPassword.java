package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ResetPassword {

    @FXML
    private PasswordField Nouveau_Mot_De_passe;

    @FXML
    private TextField email;


    @FXML
    void Valider_Password(ActionEvent event) {
        String userEmail = email.getText();
        String newPassword = Nouveau_Mot_De_passe.getText();


        if (userEmail.isEmpty() || newPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please fill in all fields.");
            return;
        }


        if (!validateEmail(userEmail)) {
            showAlert(AlertType.ERROR, "Email Not Found", "No account associated with this email.");
            return;
        }


        if (updatePassword(userEmail, newPassword)) {
            showAlert(AlertType.INFORMATION, "Success", "Password has been successfully updated.");
        } else {
            showAlert(AlertType.ERROR, "Error", "Failed to reset password. Please try again later.");
        }
    }


    @FXML
    void retourner_login_page(MouseEvent event) {

        System.out.println("Navigating back to the login page...");

    }

    private boolean validateEmail(String email) {
        return true;
    }

    private boolean updatePassword(String email, String newPassword) {
        return true;
    }

    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
