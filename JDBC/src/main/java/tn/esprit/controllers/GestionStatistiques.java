package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.services.ServiceStatistics;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class GestionStatistiques {

    @FXML
    private TableColumn<ServiceStatistics, Double> averageRatingCol;

    @FXML
    private TableColumn<ServiceStatistics, String> doctorNameCol;

    @FXML
    private TableColumn<ServiceStatistics, Integer> reclamationsCol;

    @FXML
    private AnchorPane root;

    @FXML
    private TableView<ServiceStatistics> statsTable;

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void initialize() {

        if (root != null) {
            root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        } else {
            System.out.println("Root is null. CSS not applied.");
        }
        // Initialize the columns to bind with model properties
        doctorNameCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        averageRatingCol.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
        reclamationsCol.setCellValueFactory(new PropertyValueFactory<>("complaintCount"));

        loadStatistics(); // Load the data
    }
    private void loadStatistics() {
        ObservableList<ServiceStatistics> statsList = FXCollections.observableArrayList();

        try  {
            PreparedStatement statement = MyDatabase.getInstance().getCnx().prepareStatement ("SELECT d.Nom, " +
                    "COALESCE(AVG(a.note), 0) AS averageRating, " +
                    "COALESCE(COUNT(DISTINCT r.id), 0) AS complaintCount " +
                    "FROM medecin d " +
                    "LEFT JOIN avis a ON d.id = a.medecin_id " +
                    "LEFT JOIN reclamation r ON d.id = r.medecin_id " +
                    "GROUP BY d.Nom" );

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("Nom");
                double avgRating = resultSet.getDouble("averageRating");
                int complaintCount = resultSet.getInt("complaintCount");

                statsList.add(new ServiceStatistics(name, avgRating, complaintCount));
            }

            statsTable.setItems(statsList); // Set the data to the TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void LoadReclamationPage(ActionEvent event) {

        try {
            System.out.println("Loading Reclamation Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionReclamation.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) statsTable.getScene().getWindow();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if something goes wrong
            showAlert("Error", "Failed to load the Reclamation page.");
        }


    }

    @FXML
    void LoadAvisPage(ActionEvent event) {

        try {
            System.out.println("Loading Avis Page...");
            // Load the GestionReclamation.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionAvis.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) statsTable.getScene().getWindow();

            // Create a new scene with the loaded FXML file
            Scene scene = new Scene(root);

            // Set the new scene to the stage
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Show an error message if something goes wrong
            showAlert("Error", "Failed to load the Avis page.");
        }


    }


}
