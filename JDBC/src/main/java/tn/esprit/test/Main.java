
package tn.esprit.test;

import java.util.Date;
import java.util.List;
import tn.esprit.models.Avis;
import tn.esprit.models.Reclamation;
import tn.esprit.services.ServiceAvis;
import tn.esprit.services.ServiceReclamation;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        ServiceAvis sa = new ServiceAvis();
        Avis Av = new Avis(1, 2, "Great service!", 5, new Date());
        Avis updatedAvis = new Avis(3, 4, "Service pas bon!!", 1, new Date());
        ServiceReclamation sr = new ServiceReclamation();
        Reclamation Rc = new Reclamation(1,4 ,1,"aa","bb",new Date());
       // sr.add(Rc);
        // updatedAvis.setId(14);
        List<Reclamation> ReclamationList = sr.getAll();
        if (ReclamationList.isEmpty()) {
            System.out.println("No Reclamations found.");
        } else {
            for(Reclamation Reclamation : ReclamationList) {
                sr.delete(Reclamation);
            }
        }

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
