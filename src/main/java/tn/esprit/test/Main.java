package tn.esprit.test;

import tn.esprit.models.InformationsGenerales;
import tn.esprit.services.ServiceInformationsGenerales;
import tn.esprit.models.Assurance;
import tn.esprit.services.ServiceAssurance;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //////////////////// Dossier Médical /////////////////////

        ServiceInformationsGenerales service = new ServiceInformationsGenerales();

        //ajout d'un nouveau dossier médical
        InformationsGenerales newInfo = new InformationsGenerales();
        newInfo.setIdPatient(1);  // ID du patient
        newInfo.setTaille(1.75f);
        newInfo.setPoids(70.5f);
        newInfo.setMaladies(true);
        newInfo.setAntecedentsCardiovasculairesFamiliaux("Oui");
        newInfo.setAsthmatique("Non");
        newInfo.setSuiviDentaireRegulier("Oui");
        newInfo.setAntecedentsChirurgicaux("Oui");
        newInfo.setAllergies("Oui");
        newInfo.setProfession("Ingénieur");
        newInfo.setNiveauDeStress("Moyen");
        newInfo.setQualiteDeSommeil("Bonne");
        newInfo.setActivitePhysique("Sport");
        newInfo.setSituationFamiliale("Célibataire");

        // Ajout du dossier médical dans la base de données
        service.add(newInfo);

        // Exemple de récupération et d'affichage de tous les dossiers médicaux
        List<InformationsGenerales> allDossiers = service.getAll();
        System.out.println("Liste des dossiers médicaux :");
        for (InformationsGenerales dossier : allDossiers) {
            System.out.println(dossier);  // Cela affichera le résultat de la méthode toString()
        }

        // Exemple de mise à jour d'un dossier médical
        System.out.println("\nMise à jour du dossier médical ");

        InformationsGenerales infoToUpdate = new InformationsGenerales();
        infoToUpdate.setId(36);
        infoToUpdate.setIdPatient(1);
        infoToUpdate.setTaille(1.40f);  // Nouvelle taille
        infoToUpdate.setPoids(75.0f);  // Nouveau poids
        infoToUpdate.setMaladies(false); // Supposons qu'il n'a plus de maladies
        infoToUpdate.setAntecedentsCardiovasculairesFamiliaux("Antécédents dans la famille");
        infoToUpdate.setAsthmatique("Oui");
        infoToUpdate.setSuiviDentaireRegulier("Non");
        infoToUpdate.setAntecedentsChirurgicaux("Oui");
        infoToUpdate.setAllergies("Non");
        infoToUpdate.setProfession("Médecin");
        infoToUpdate.setNiveauDeStress("Faible");
        infoToUpdate.setQualiteDeSommeil("Moyenne");
        infoToUpdate.setActivitePhysique("Marche");
        infoToUpdate.setSituationFamiliale("Marié");

        // Mise à jour du dossier médical dans la base de données
        service.update(infoToUpdate);

        // Vérification de la mise à jour en récupérant à nouveau les dossiers médicaux
        allDossiers = service.getAll();
        System.out.println("\nListe des dossiers médicaux après mise à jour :");
        for (InformationsGenerales dossier : allDossiers) {
            System.out.println(dossier);  // Cela affichera le résultat de la méthode toString()
        }

        // Exemple de suppression d'un dossier médical
        System.out.println("\nSuppression du dossier médical ");
        InformationsGenerales infoToDelete = new InformationsGenerales();
        infoToDelete.setId(36);  // Spécifier l'ID du dossier à supprimer

        // Suppression du dossier médical
        service.delete(infoToDelete);

        // Vérification que le dossier a été supprimé en affichant à nouveau tous les dossiers
        allDossiers = service.getAll();
        System.out.println("\nListe des dossiers médicaux après suppression :");
        for (InformationsGenerales dossier : allDossiers) {
            System.out.println(dossier);  // Cela affichera le résultat de la méthode toString()
        }


        //////////////////// Assurance /////////////////////

        ServiceAssurance serviceA = new ServiceAssurance();

        // Adding a new assurance record
        Assurance newAssurance = new Assurance();
        newAssurance.setNom("Assurance Vie");
        newAssurance.setType("Privée");
        newAssurance.setDateDebut(LocalDate.of(2025, 1, 1));
        newAssurance.setDateFin(LocalDate.of(2030, 12, 31));
        //newAssurance.setMontantCouvert(50000.0f);

        serviceA.add(newAssurance);

       // Retrieving and displaying all assurance records
        List<Assurance> allAssurances = serviceA.getAll();
        System.out.println("\nListe des assurances :");
        for (Assurance assurance : allAssurances) {
            System.out.println(assurance);
        }

        // Updating an assurance record
        System.out.println("\nMise à jour d'une assurance");
        Assurance assuranceToUpdate = new Assurance();
        assuranceToUpdate.setIdAssurance(2); // ID to update
        assuranceToUpdate.setNom("Assurance Santé");
        assuranceToUpdate.setType("Privée");
        assuranceToUpdate.setDateDebut(LocalDate.of(2024, 6, 1));
        assuranceToUpdate.setDateFin(LocalDate.of(2029, 6, 1));
        //assuranceToUpdate.setMontantCouvert(75000.0f);

        serviceA.update(assuranceToUpdate); // Fix: Using correct service instance

        // Retrieving and displaying all assurance records after update
        allAssurances = serviceA.getAll();
        System.out.println("\nListe des assurances après mise à jour :");
        for (Assurance assurance : allAssurances) {
            System.out.println(assurance);
        }

        // Deleting an assurance record
        System.out.println("\nSuppression d'une assurance");
        Assurance assuranceToDelete = new Assurance();
        assuranceToDelete.setIdAssurance(1); // ID to delete

        serviceA.delete(assuranceToDelete); // Fix: Using correct service instance

        // Retrieving and displaying all assurance records after deletion
        allAssurances = serviceA.getAll();
        System.out.println("\nListe des assurances après suppression :");
        for (Assurance assurance : allAssurances) {
            System.out.println(assurance);
        }
    }
}
