package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AjouterOrdonnance {

    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private DatePicker datePrescription;
    @FXML private ListView<String> listMedicaments;
    @FXML private TextField medicamentNom;
    @FXML private TextField quantite;
    @FXML private Button ajouterMedicament;
    @FXML private Button ajouterOrdonnance;

    private final ObservableList<String> medicamentsList = FXCollections.observableArrayList();
    private final Map<String, Integer> medicamentsMap = new HashMap<>();

    @FXML
    private void ajouterMedicament() {
        String nomMed = medicamentNom.getText().trim();
        String qteStr = quantite.getText().trim();

        if (!nomMed.isEmpty() && !qteStr.isEmpty()) {
            try {
                int qte = Integer.parseInt(qteStr);
                medicamentsMap.put(nomMed, qte);
                medicamentsList.add(nomMed + " : " + qte);
                listMedicaments.setItems(medicamentsList);

                // Réinitialiser les champs après ajout
                medicamentNom.clear();
                quantite.clear();
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer une quantité valide.");
            }
        }
    }

    @FXML
    private void ajouterOrdonnance() {
        String patientNom = nom.getText().trim();
        String patientPrenom = prenom.getText().trim();
        LocalDate datePresc = datePrescription.getValue();

        if (!patientNom.isEmpty() && !patientPrenom.isEmpty() && datePresc != null && !medicamentsMap.isEmpty()) {
            // Créer l'objet Ordonnance avec les données du formulaire
            private ServiceOrdonnance so = new ServiceOrdonnance();
            so.add(new Ordonnance(id.get));
            // Réinitialiser les champs après ajout
            nom.clear();
            prenom.clear();
            datePrescription.setValue(null);
            medicamentsMap.clear();
            medicamentsList.clear();
            listMedicaments.setItems(medicamentsList);

        } else {
            System.out.println("Veuillez remplir tous les champs.");
        }
    }

}
