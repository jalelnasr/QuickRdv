package tn.esprit.test;

import tn.esprit.models.InformationsGenerales;
import tn.esprit.services.ServiceInformationsGenerales;

public class Main {
    public static void main(String[] args) {
        ServiceInformationsGenerales sdm = new ServiceInformationsGenerales();

        // Ensure this patient ID exists in the `patient` table
        int validPatientId = 2;  // Replace with an actual existing ID in your `patient` table

        // Creating a medical record with taille, poids, maladies, and id_patient
      /*  InformationsGenerales info = new InformationsGenerales(
                1, // id_patient
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
        System.out.println("Liste des dossiers médicaux après ajout :");
        sdm.getAll().forEach(System.out::println);*/

        // Update the medical record
        System.out.println("\nMise à jour du dossier médical pour le patient ID : " + validPatientId);

        sdm.update(new InformationsGenerales(20 ,1, "Ordonnance2" ,"Teleconsultation2", "ConsultationPresentiel2", 100.0f , 557.0f, true  ));

        // Confirm update
        System.out.println("\nListe des dossiers médicaux après mise à jour :");
        sdm.getAll().forEach(System.out::println);



        // Delete the medical record for the specified patient ID
        InformationsGenerales infoToDelete = new InformationsGenerales();
        infoToDelete.setIdPatient(1);
        System.out.println("\nSuppression du dossier médical pour le patient ID : " + validPatientId);
        sdm.delete(infoToDelete);

        // Confirm deletion by retrieving and printing all records
        System.out.println("\nListe des dossiers médicaux après suppression :");
        sdm.getAll().forEach(System.out::println);
    }
}