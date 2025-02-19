package tn.esprit.test;

import tn.esprit.models.Analyse;
import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceAnalyse;
import tn.esprit.services.ServiceRendezVous;

import java.sql.Date; // Use java.sql.Date instead of java.util.Date
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ServiceRendezVous serviceRendezVous = new ServiceRendezVous();
    private static final ServiceAnalyse serviceAnalyse = new ServiceAnalyse();
    private static boolean running = true;

    public static void main(String[] args) {
        while (running) {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Gérer Rendez-Vous");
            System.out.println("2. Gérer Analyses");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumes the newline character

            switch (choice) {
                case 1 -> gererRendezVous();
                case 2 -> gererAnalyses();
                case 3 -> {
                    System.out.println("Fermeture du programme...");
                    running = false;
                }
                default -> System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void gererRendezVous() {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("\nGestion des Rendez-Vous:");
            System.out.println("1. Ajouter un rendez-vous");
            System.out.println("2. Afficher tous les rendez-vous");
            System.out.println("3. Mettre à jour un rendez-vous");
            System.out.println("4. Supprimer un rendez-vous");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> ajouterRendezVous();
                case 2 -> afficherRendezVous();
                case 3 -> mettreAJourRendezVous();
                case 4 -> supprimerRendezVous();
                case 5 -> backToMainMenu = true;
                default -> System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterRendezVous() {
        System.out.print("Entrez la date du rendez-vous (format YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date = Date.valueOf(dateStr); // Convert String to java.sql.Date
        System.out.print("Entrez l'ID du patient: ");
        int patientId = scanner.nextInt();
        System.out.print("Entrez l'ID du médecin: ");
        int medecinId = scanner.nextInt();
        System.out.print("Entrez l'ID du type de consultation: ");
        int typeConsultationId = scanner.nextInt();
        scanner.nextLine();

        RendezVous rendezVous = new RendezVous(date, patientId, medecinId, typeConsultationId);
        serviceRendezVous.add(rendezVous);
        System.out.println("Rendez-vous ajouté avec succès !");
    }

    private static void afficherRendezVous() {
        System.out.println("Liste des rendez-vous:");
        for (RendezVous rendezVous : serviceRendezVous.getAll()) {
            System.out.println(rendezVous);
        }
    }

    private static void mettreAJourRendezVous() {
        System.out.print("Entrez l'ID du rendez-vous à mettre à jour: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Entrez la nouvelle date (format YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        Date date = Date.valueOf(dateStr); // Convert String to java.sql.Date
        System.out.print("Entrez le nouvel ID du patient: ");
        int patientId = scanner.nextInt();
        System.out.print("Entrez le nouvel ID du médecin: ");
        int medecinId = scanner.nextInt();
        System.out.print("Entrez le nouvel ID du type de consultation: ");
        int typeConsultationId = scanner.nextInt();
        scanner.nextLine();

        RendezVous rendezVous = new RendezVous(id, date, patientId, medecinId, typeConsultationId);
        serviceRendezVous.update(rendezVous);
        System.out.println("Rendez-vous mis à jour avec succès !");
    }

    private static void supprimerRendezVous() {
        System.out.print("Entrez l'ID du rendez-vous à supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        RendezVous rendezVous = new RendezVous();
        rendezVous.setId(id);
        serviceRendezVous.delete(rendezVous);
        System.out.println("Rendez-vous supprimé avec succès !");
    }

    private static void gererAnalyses() {
        boolean backToMainMenu = false;
        while (!backToMainMenu) {
            System.out.println("\nGestion des Analyses:");
            System.out.println("1. Ajouter une analyse");
            System.out.println("2. Afficher toutes les analyses");
            System.out.println("3. Mettre à jour une analyse");
            System.out.println("4. Supprimer une analyse");
            System.out.println("5. Retour au menu principal");
            System.out.print("Choisissez une option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> ajouterAnalyse();
                case 2 -> afficherAnalyses();
                case 3 -> mettreAJourAnalyse();
                case 4 -> supprimerAnalyse();
                case 5 -> backToMainMenu = true;
                default -> System.out.println("Option invalide, veuillez réessayer.");
            }
        }
    }

    private static void ajouterAnalyse() {
        System.out.print("Entrez le type d'analyse: ");
        String type = scanner.nextLine();
        System.out.print("Entrez les instructions: ");
        String instructions = scanner.nextLine();
        System.out.print("Entrez l'ID du patient: ");
        int idPatient = scanner.nextInt();
        System.out.print("Entrez l'ID du médecin: ");
        int idMedecin = scanner.nextInt();
        scanner.nextLine();

        Analyse analyse = new Analyse(new Date(System.currentTimeMillis()), type, instructions, idPatient, idMedecin);
        serviceAnalyse.add(analyse);
        System.out.println("Analyse ajoutée avec succès !");
    }

    private static void afficherAnalyses() {
        System.out.println("Liste des analyses:");
        for (Analyse analyse : serviceAnalyse.getAll()) {
            System.out.println(analyse);
        }
    }

    private static void mettreAJourAnalyse() {
        System.out.print("Entrez l'ID de l'analyse à mettre à jour: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Entrez le nouveau type d'analyse: ");
        String type = scanner.nextLine();
        System.out.print("Entrez les nouvelles instructions: ");
        String instructions = scanner.nextLine();
        System.out.print("Entrez le nouvel ID du patient: ");
        int idPatient = scanner.nextInt();
        System.out.print("Entrez le nouvel ID du médecin: ");
        int idMedecin = scanner.nextInt();
        scanner.nextLine();

        Analyse analyse = new Analyse(id, new Date(System.currentTimeMillis()), type, instructions, idPatient, idMedecin);
        serviceAnalyse.update(analyse);
        System.out.println("Analyse mise à jour avec succès !");
    }

    private static void supprimerAnalyse() {
        System.out.print("Entrez l'ID de l'analyse à supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Analyse analyse = new Analyse();
        analyse.setId(id);
        serviceAnalyse.delete(analyse);
        System.out.println("Analyse supprimée avec succès !");
    }
}