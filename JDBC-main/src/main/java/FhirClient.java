import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Medication;

public class FhirClient {
    private static final String SERVER_BASE = "http://hapi.fhir.org/baseR4";
    private static final FhirContext ctx = FhirContext.forR4();
    private static final IGenericClient client = ctx.newRestfulGenericClient(SERVER_BASE);

    public static Medication fetchMedicationById(String id) {
        try {
            return client.read()
                    .resource(Medication.class)
                    .withId(id)
                    .execute();
        } catch (ResourceNotFoundException e) {
            System.out.println("⚠️ Médicament non trouvé avec l'ID: " + id);
            return null;
        }
    }
    public static void listMedications() {
        try {
            Bundle results = client.search()
                    .forResource(Medication.class)
                    .returnBundle(Bundle.class)
                    .execute();

            if (results.getEntry().isEmpty()) {
                System.out.println("⚠️ Aucun médicament trouvé sur le serveur.");
                return;
            }

            System.out.println("📋 Liste des médicaments disponibles :");
            for (Bundle.BundleEntryComponent entry : results.getEntry()) {
                Medication med = (Medication) entry.getResource();
                String id = med.getIdElement().getIdPart();
                String name = "Nom inconnu";

                // Vérifier si le champ code.text est disponible
                if (med.hasCode() && med.getCode().hasText()) {
                    name = med.getCode().getText();
                }
                // Sinon, essayer de récupérer le premier "display" d'un coding
                else if (med.hasCode() && !med.getCode().getCoding().isEmpty()) {
                    name = med.getCode().getCodingFirstRep().getDisplay();
                }

                System.out.println("ID: " + id + " - Nom: " + name);
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la récupération des médicaments : " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        listMedications();
    }
}
