import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.CodeableConcept;

public class CreateMedication {
    public static void main(String[] args) {
        // Initialisation du contexte et du client FHIR
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        // Création d'un objet Médicament
        Medication medication = new Medication();
        medication.setCode(new CodeableConcept().addCoding(new Coding()
                .setSystem("http://www.whocc.no/atc")  // Système de codage ATC
                .setCode("N02BE01")  // Code ATC pour le Paracétamol
                .setDisplay("Paracetamol")));

        // Envoi du médicament au serveur FHIR
        MethodOutcome response = client.create()
                .resource(medication)
                .execute();

        // Affichage de l'ID du médicament ajouté
        System.out.println("✅ Médicament ajouté avec ID : " + response.getId().getIdPart());
    }
}
