package tn.esprit.test;

import tn.esprit.models.Medicament;
import tn.esprit.models.Ordonnance;
import tn.esprit.services.ServiceMedicament;
import tn.esprit.services.ServiceOrdonnance;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ServiceOrdonnance so = new ServiceOrdonnance();
        Ordonnance ord = new Ordonnance();

        // Initialisation des médicaments associés à l'ordonnance
        //Map<Integer, Integer> medicaments = new HashMap<>();
        //medicaments.put(1, 2); // Médicament ID 1, quantité 2
        //medicaments.put(2, 1); // Médicament ID 2, quantité 1




        ServiceMedicament sm = new ServiceMedicament();
        //sm.add(new Medicament("pills",150,1));
        //sm.add(new Medicament("doliprane",100,2));
        //sm.update(new Medicament(1,"pills",200,2));
        //Medicament medicamentToDelete = new Medicament();
        //medicamentToDelete.setId(1);
        //sm.delete(medicamentToDelete);

        //System.out.println(sm.getAll());


        


        Map<String, Integer> medicaments = new HashMap<>();
        medicaments.put("Doliprane", 2);
        medicaments.put("Ibuprofène", 1);
        so.add(new Ordonnance(1, 2, new Date(), "À prendre après les repas", "en attente", medicaments));
        if (ord.getId() > 0) {
            so.insertOrdonnanceMedicaments(ord.getId(), medicaments);
        } else {
            System.out.println("Erreur : l'ordonnance n'a pas été enregistrée correctement.");
        }
    }


    }

