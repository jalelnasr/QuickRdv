package tn.esprit.test;

import tn.esprit.models.Medicament;
import tn.esprit.services.ServiceMedicament;

import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        ServiceMedicament serviceMedicament = new ServiceMedicament();

        // Ajouter un médicament
        Medicament paracetamol = new Medicament("Paracétamol", 100, 1); // pharmacien_id = 1
        serviceMedicament.add(paracetamol);

        // Afficher tous les médicaments
        List<Medicament> medicaments = serviceMedicament.getAll();
        System.out.println("Liste des médicaments : " + medicaments);

        // Modifier un médicament
        if (!medicaments.isEmpty()) {
            Medicament m = medicaments.get(0);
            m.setStock(200);
            serviceMedicament.update(m);
        }

        // Supprimer un médicament (par exemple, celui avec ID 1)
        serviceMedicament.delete(1);
    }
}
