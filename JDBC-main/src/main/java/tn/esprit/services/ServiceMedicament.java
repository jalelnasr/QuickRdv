package tn.esprit.services;

import ca.uhn.fhir.rest.api.MethodOutcome;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Medicament;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Medication;



public class ServiceMedicament implements IService<Medicament> {
    private final Connection cnx;

    public ServiceMedicament() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un médicament
    @Override
    public void add(Medicament medicament) {
        String qry = "INSERT INTO medicament (nom, stock, pharmacien_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, medicament.getNom());
            pstm.setInt(2, medicament.getStock());
            pstm.setInt(3, medicament.getPharmacienId());

            pstm.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    medicament.setId(rs.getInt(1));
                }
            }
            System.out.println("Médicament ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du médicament : " + e.getMessage());
        }
    }

    // Récupérer tous les médicaments
    @Override
    public List<Medicament> getAll() {
        List<Medicament> medicaments = new ArrayList<>();
        String qry = "SELECT * FROM Medicament";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id"));
                m.setNom(rs.getString("nom"));
                m.setStock(rs.getInt("stock"));
                m.setPharmacienId(rs.getInt("pharmacien_id"));
                medicaments.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des médicaments : " + e.getMessage());
        }

        return medicaments;
    }

    // Modifier un médicament
    @Override
    public void update(Medicament medicament) {
        String qry = "UPDATE medicament SET nom=?, stock=?, pharmacien_id=? WHERE id=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, medicament.getNom());
            pstm.setInt(2, medicament.getStock());
            pstm.setInt(3, medicament.getPharmacienId());
            pstm.setInt(4, medicament.getId());

            pstm.executeUpdate();
            System.out.println("Médicament mis à jour !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du médicament : " + e.getMessage());
        }
    }

    // Supprimer un médicament
    // Supprimer un médicament par son ID

    @Override
    public void delete(Medicament medicament) {
        String qry = "DELETE FROM medicament WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, medicament.getId());

            pstm.executeUpdate();
            System.out.println("Médicament supprimé !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du médicament : " + e.getMessage());
        }
    }



    public void importFromFHIR() {
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        // Récupération de tous les médicaments du serveur FHIR
        Bundle results = client.search()
                .forResource(Medication.class)
                .returnBundle(Bundle.class)
                .execute();

        for (Bundle.BundleEntryComponent entry : results.getEntry()) {
            Medication medFHIR = (Medication) entry.getResource();
            String idFHIR = medFHIR.getIdElement().getIdPart();
            String nom = medFHIR.hasCode() && medFHIR.getCode().hasText() ? medFHIR.getCode().getText() : "Nom inconnu";

            // Vérifier s'il y a une extension pour le stock
            int stock = 0;
            if (!medFHIR.getExtension().isEmpty()) {
                if (medFHIR.getExtensionByUrl("http://example.com/fhir/StructureDefinition/stock").getValue() instanceof org.hl7.fhir.r4.model.IntegerType) {
                    stock = ((org.hl7.fhir.r4.model.IntegerType) medFHIR.getExtensionByUrl("http://example.com/fhir/StructureDefinition/stock").getValue()).getValue();
                }
            }


            // Vérifier si le médicament existe déjà dans la base locale
            if (!medicamentExisteDeja(nom)) {
                ajouterMedicament(nom, stock, idFHIR);
                System.out.println("🟢 Médicament importé depuis FHIR : " + nom + " (Stock: " + stock + ")");
            }
        }
    }

    // Vérifie si le médicament existe déjà dans la base locale
    private boolean medicamentExisteDeja(String nom) {
        String qry = "SELECT COUNT(*) FROM medicament WHERE nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, nom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du médicament : " + e.getMessage());
        }
        return false;
    }

    // Ajoute un médicament récupéré depuis FHIR dans la base locale
    private void ajouterMedicament(String nom, int stock, String idFHIR) {
        String qry = "INSERT INTO medicament (nom, stock, idFHIR) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, nom);
            stmt.setInt(2, stock);
            stmt.setString(3, idFHIR);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du médicament : " + e.getMessage());
        }
    }


    public void exportToFHIR() {
        FhirContext ctx = FhirContext.forR4();
        IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        List<Medicament> medicamentsLocaux = getMedicamentsNonSynchronises();

        for (Medicament med : medicamentsLocaux) {
            Medication medicationFHIR = new Medication();
            medicationFHIR.setCode(new CodeableConcept().addCoding(new Coding()
                    .setSystem("http://www.whocc.no/atc")
                    .setDisplay(med.getNom())));

            // Ajouter le stock en tant qu'extension personnalisée
            medicationFHIR.addExtension()
                    .setUrl("http://example.com/fhir/StructureDefinition/stock")
                    .setValue(new org.hl7.fhir.r4.model.IntegerType(med.getStock()));

            // Envoi au serveur FHIR
            MethodOutcome outcome = client.create()
                    .resource(medicationFHIR)  // ✅ Correction ici
                    .execute();

            // Récupérer l'ID du médicament créé
            String idFHIR = outcome.getId().getIdPart();
            System.out.println("✅ Médicament ajouté avec ID FHIR : " + idFHIR);

            // Enregistrer l'ID FHIR en base de données
            enregistrerIdFHIR(med.getId(), idFHIR);
        }
    }

    // Récupère les médicaments non encore synchronisés avec FHIR
    private List<Medicament> getMedicamentsNonSynchronises() {
        List<Medicament> medicaments = new ArrayList<>();
        String qry = "SELECT * FROM medicament WHERE idFHIR IS NULL";

        try (PreparedStatement stmt = cnx.prepareStatement(qry);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Medicament m = new Medicament();
                m.setId(rs.getInt("id"));
                m.setNom(rs.getString("nom"));
                m.setStock(rs.getInt("stock"));
                m.setPharmacienId(rs.getInt("pharmacien_id"));
                medicaments.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des médicaments non synchronisés : " + e.getMessage());
        }
        return medicaments;
    }

    // Enregistre l'ID FHIR en base de données pour éviter les doublons
    private void enregistrerIdFHIR(int idLocal, String idFHIR) {
        String qry = "UPDATE medicament SET idFHIR = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(qry)) {
            stmt.setString(1, idFHIR);
            stmt.setInt(2, idLocal);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'enregistrement de l'ID FHIR : " + e.getMessage());
        }
    }

    private int[] getIdAndStock(String nomMedicament) {
        String query = "SELECT id, stock FROM medicament WHERE nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, nomMedicament);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new int[]{rs.getInt("id"), rs.getInt("stock")};
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID et du stock : " + e.getMessage());
        }
        return new int[]{-1, 0}; // Retourne -1 si le médicament n'est pas trouvé
    }



    public boolean updateStock(Map<String, Integer> medicaments) {
        try {
            // Récupérer tous les médicaments depuis la base de données
            Map<String, int[]> stockMedicament = new HashMap<>();
            String query = "SELECT id, nom, stock FROM medicament";

            try (PreparedStatement stmt = cnx.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stockMedicament.put(rs.getString("nom"), new int[]{rs.getInt("id"), rs.getInt("stock")});
                }
            }

            for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
                String nomMedicament = entry.getKey();
                int quantiteDemandee = entry.getValue();

                // Vérifier si le médicament existe dans la base de données
                if (!stockMedicament.containsKey(nomMedicament)) {
                    System.out.println("❌ Médicament non trouvé : " + nomMedicament);
                    return false;
                }

                int idMedicament = stockMedicament.get(nomMedicament)[0];
                int stockDisponible = stockMedicament.get(nomMedicament)[1];

                // Vérifier si le stock est suffisant
                if (stockDisponible < quantiteDemandee) {
                    System.out.println("❌ Stock insuffisant pour " + nomMedicament);
                    return false;
                }

                // Mise à jour du stock dans la base de données
                String updateQuery = "UPDATE medicament SET stock = stock - ? WHERE id = ?";
                try (PreparedStatement stmt = cnx.prepareStatement(updateQuery)) {
                    stmt.setInt(1, quantiteDemandee);
                    stmt.setInt(2, idMedicament);
                    stmt.executeUpdate();
                    System.out.println("✅ Stock mis à jour pour : " + nomMedicament);
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du stock : " + e.getMessage());
            return false;
        }
    }



    private int getStock(String nomMedicament) {
        String query = "SELECT stock FROM medicament WHERE nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, nomMedicament);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("stock");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du stock : " + e.getMessage());
        }
        return 0;
    }






}




