
package tn.esprit.test;

import java.util.Date;
import java.util.List;
import tn.esprit.models.Avis;
import tn.esprit.services.ServiceAvis;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        ServiceAvis sa = new ServiceAvis();
        Avis Av = new Avis(1, 2, "Great service!", 5, new Date());
        Avis updatedAvis = new Avis(3, 4, "Service pas bon!!", 1, new Date());
        //updatedAvis.setId(14);
        sa.add(Av);
        List<Avis> avisList = sa.getAll();
        if (avisList.isEmpty()) {
            System.out.println("No reviews found."); 
        } else {
            for(Avis avis : avisList) {
                System.out.println("ID: " + avis.getId());
                System.out.println("Patient ID: " + avis.getPatient_id());
                System.out.println("Medecin ID: " + avis.getMedecin_id());
                System.out.println("Comment: " + avis.getCommentaire());
                System.out.println("Note: " + avis.getNote());
                System.out.println("Date: " + String.valueOf(avis.getDate_avis()));
                System.out.println("-----------------------------");
            }
        }

       // sa.update(updatedAvis);
    }
}
