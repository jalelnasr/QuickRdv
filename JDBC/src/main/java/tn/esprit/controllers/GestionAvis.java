package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import tn.esprit.services.ServiceAvis;
import tn.esprit.models.Avis;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


public class GestionAvis {

    @FXML
    private TextArea Commentaire;

    @FXML
    private TextField Note;

    @FXML
    private ListView<Avis> avisListView;

    private ServiceAvis sa = new ServiceAvis();
    @FXML
    void Save(ActionEvent event) {
        sa.add(new Avis(1, 2, Commentaire.getText(), Integer.parseInt(Note.getText()), new Date()));

    }
    @FXML
    void Show(ActionEvent event) {

            // Fetch all avis from the database
            List<Avis> avisList = sa.getAll();

            // Clear the ListView and add the fetched avis
            avisListView.getItems().clear();
            avisListView.getItems().addAll(avisList);

    }

    }


