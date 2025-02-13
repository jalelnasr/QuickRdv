package tn.esprit.test;

import tn.esprit.models.Utilisateur;
import tn.esprit.services.ServiceUtilisateur;

public class Main {

    public static void main(String[] args) {
        ServiceUtilisateur su = new ServiceUtilisateur();

        // Ajouter un utilisateur
        Utilisateur user = new Utilisateur(1,"achref", "kachai", "achref.kachai25@esprit.tn", "password123");
        //su.add(user);

        // Afficher tous les utilisateurs
        System.out.println(su.getAll());
       // su.delete(user);
        su.update(user);

        // Vérifier la mise à jour
        System.out.println("Utilisateur mis à jour !");
        System.out.println(su.getAll());

    }
}
