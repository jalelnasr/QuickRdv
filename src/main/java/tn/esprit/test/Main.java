package tn.esprit.test;

import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceOrdonnance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ServiceOrdonnance serviceOrdonnance = new ServiceOrdonnance();

        // Liste des médicaments (ID, Quantité)
        Map<Integer, Integer> medicaments = new HashMap<>();
        medicaments.put(1, 2); // Médicament ID 1, Quantité 2
        medicaments.put(2, 1); // Médicament ID 2, Quantité 1

        // Ajouter une ordonnance
        Ordonnance ordonnance = new Ordonnance(1, 2, new Date(), "Instructions", "En cours", medicaments);
        serviceOrdonnance.add(ordonnance);

        // Valider l'ordonnance
        serviceOrdonnance.validerOrdonnance(1);

        // Suppression de l'ordonnance
        serviceOrdonnance.delete(1);
    }
}
