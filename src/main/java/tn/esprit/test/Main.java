package tn.esprit.test;

import tn.esprit.models.RendezVous;
import tn.esprit.services.ServiceRendezVous;

import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final ServiceRendezVous serviceRendezVous = new ServiceRendezVous();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(scanner::close));
        gererRendezVous();
    }

    private static void gererRendezVous() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- Gestion des Rendez-vous ---");
            System.out.println("1. Ajouter un rendez-vous");
            System.out.println("2. Afficher tous les rendez-vous");
            System.out.println("3. Mettre à jour un rendez-vous");
            System.out.println("4. Supprimer un rendez-vous");
            System.out.println("5. Quitter");
            System.out.print("Choisissez une option : ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne

                switch (choice) {
                    case 1 -> ajouterRendezVous();
                    case 2 -> afficherRendezVous();
                    case 3 -> mettreAJourRendezVous();
                    case 4 -> supprimerRendezVous();
                    case 5 -> {
                        System.out.println("Fermeture du programme...");
                        running = false;
                    }
                    default -> System.out.println("Option invalide. Veuillez réessayer.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide, veuillez entrer un nombre.");
                scanner.nextLine(); // Nettoyer l’entrée incorrecte
            }
        }
    }

    private static void ajouterRendezVous() {
        try {
            System.out.print("Entrez la date du rendez-vous (format dd/MM/yyyy) : ");
            String dateStr = scanner.nextLine();
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);

            System.out.print("Entrez l'ID du patient : ");
            int patientId = scanner.nextInt();

            System.out.print("Entrez l'ID du médecin : ");
            int medecinId = scanner.nextInt();

            System.out.print("Entrez l'ID du type de consultation : ");
            int typeConsultationId = scanner.nextInt();

            RendezVous rendezVous = new RendezVous(date, patientId, medecinId, typeConsultationId);
            serviceRendezVous.add(rendezVous);
            System.out.println("1 Rendez-vous ajouté avec succès !");
        } catch (ParseException e) {
            System.out.println(" Erreur de format de date.");
        } catch (InputMismatchException e) {
            System.out.println(" Erreur : veuillez entrer des nombres valides.");
            scanner.nextLine(); // Nettoyer l'entrée
        }
    }

    private static void afficherRendezVous() {
        List<RendezVous> rendezVousList = serviceRendezVous.getAll();
        if (rendezVousList.isEmpty()) {
            System.out.println("⚠ Aucun rendez-vous trouvé.");
        } else {
            System.out.println(" Liste des rendez-vous :");
            for (RendezVous rv : rendezVousList) {
                System.out.println(rv);
            }
        }
    }

    private static void mettreAJourRendezVous() {
        try {
            System.out.print("Entrez l'ID du rendez-vous à mettre à jour : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Entrez la nouvelle date (format dd/MM/yyyy) : ");
            String dateStr = scanner.nextLine();
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);

            System.out.print("Entrez le nouvel ID du patient : ");
            int patientId = scanner.nextInt();

            System.out.print("Entrez le nouvel ID du médecin : ");
            int medecinId = scanner.nextInt();

            System.out.print("Entrez le nouvel ID du type de consultation : ");
            int typeConsultationId = scanner.nextInt();

            RendezVous rendezVous = new RendezVous(id, date, patientId, medecinId, typeConsultationId);
            serviceRendezVous.update(rendezVous);
            System.out.println("✅ Rendez-vous mis à jour avec succès !");
        } catch (ParseException e) {
            System.out.println("❌ Erreur : format de date invalide.");
        } catch (InputMismatchException e) {
            System.out.println("❌ Erreur : veuillez entrer des nombres valides.");
            scanner.nextLine(); // Nettoyer l'entrée
        }
    }

    private static void supprimerRendezVous() {
        try {
            System.out.print("Entrez l'ID du rendez-vous à supprimer : ");
            int id = scanner.nextInt();
            scanner.nextLine();

            RendezVous rendezVous = new RendezVous();
            rendezVous.setId(id);
            serviceRendezVous.delete(rendezVous);
            System.out.println(" Rendez-vous supprimé avec succès !");
        } catch (InputMismatchException e) {
            System.out.println(" Erreur : veuillez entrer un ID valide.");
            scanner.nextLine(); // Nettoyer l'entrée
        }
    }
}
