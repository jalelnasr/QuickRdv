package tn.esprit.controllers;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import tn.esprit.utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.Random;
import javafx.event.ActionEvent;

public class ResetPassword {

    @FXML
    private PasswordField Confirmer_Mot_De_passe;
    @FXML
    private PasswordField Nouveau_Mot_De_passe;
    @FXML
    private Button Valider_Password;
    @FXML
    private Button valider_code;
    @FXML
    private TextField code;
    @FXML
    private TextField email;
    @FXML
    private Button valider_email;

    private String codeEnvoye = "";

    @FXML
    public void initialize() {
        // Désactiver les champs au début
        code.setDisable(true);
        Nouveau_Mot_De_passe.setDisable(true);
        Confirmer_Mot_De_passe.setDisable(true);
        Valider_Password.setDisable(true);
    }

    @FXML
    void valider_email(ActionEvent event) {
        try {
            String emailText = email.getText().trim();
            if (emailText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer votre email.");
                return;
            }

            // Vérifier si l'email existe dans la base de données
            Connection conn = MyDatabase.getInstance().getCnx();
            String sql = "SELECT email FROM utilisateur WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, emailText);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Email trouvé
                codeEnvoye = generateCode(); // Générer un code unique
                sendCodeToEmail(emailText, codeEnvoye); // Envoyer le code à cet email
                code.setDisable(false);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Un code a été envoyé à votre adresse email.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Email non trouvé.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Détail : " + e.getMessage());
        }
    }

    @FXML
    void valider_code(ActionEvent event) {
        try {
            String codeText = code.getText().trim();
            if (codeText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer le code.");
                return;
            }

            if (codeText.equals(codeEnvoye)) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Code validé. Vous pouvez changer votre mot de passe.");
                Nouveau_Mot_De_passe.setDisable(false);
                Confirmer_Mot_De_passe.setDisable(false);
                Valider_Password.setDisable(false);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Code incorrect.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Détail : " + e.getMessage());
        }
    }

    @FXML
    void Valider_Password(ActionEvent event) {
        try {
            String newPassword = Nouveau_Mot_De_passe.getText().trim();
            String confirmPassword = Confirmer_Mot_De_passe.getText().trim();
            String emailText = email.getText().trim();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer votre nouveau mot de passe.");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les mots de passe ne correspondent pas.");
                return;
            }

            // Mettre à jour le mot de passe dans la base de données
            Connection conn = MyDatabase.getInstance().getCnx();
            String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, emailText);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe changé avec succès.");
                retourner(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de mettre à jour le mot de passe.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Détail : " + e.getMessage());
        }
    }

    @FXML
    void retourner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion : " + e.getMessage());
        }
    }

    private String generateCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendCodeToEmail(String recipientEmail, String code) {
        final String senderEmail = "jalelnasr67@gmail.com";
        final String senderPassword = "naso rleb luxe baph";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Code de Réinitialisation");
            message.setText("Votre code de réinitialisation est : " + code);
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + recipientEmail);
        } catch (MessagingException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'envoi", "L'email n'a pas pu être envoyé. Détail : " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
