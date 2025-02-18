package tn.esprit.test;

import tn.esprit.models.InformationsGenerales;
import tn.esprit.services.ServiceInformationsGenerales;

public class Main {
    public static void main(String[] args) {
        ServiceInformationsGenerales sdm = new ServiceInformationsGenerales();

        // Ensure this patient ID exists in the `patient` table
        int validPatientId = 2;  // Replace with an actual existing ID in your `patient` table

        // Creating a medical record with taille, poids, maladies, and id_patient
        InformationsGenerales info = new InformationsGenerales(
                validPatientId, // id_patient
                "Ordonnance1",   // historique_ordonnance
                "Teleconsultation1", // historique_teleconsultation
                "ConsultationPresentiel1", // historique_consultation_presentiel
                150.0f, // taille
                45.0f,  // poids
                true    // maladies
        );

        // Add the medical record
        sdm.add(info);

        // Confirm addition by retrieving and printing all records
        System.out.println("Liste des dossiers médicaux :");
        sdm.getAll().forEach(System.out::println);
    }
}