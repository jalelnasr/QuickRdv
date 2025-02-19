package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.InformationsGenerales;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceInformationsGenerales implements IService<InformationsGenerales> {
    private final Connection cnx;

    public ServiceInformationsGenerales() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Add a new record to dossiermedicale
    @Override
    public void add(InformationsGenerales informationsGenerales) {
        String qry = "INSERT INTO dossiermedicale (id_patient, historique_ordonnance, historique_teleconsultation, historique_consultation_presentiel, taille, poids, maladies) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, informationsGenerales.getIdPatient());  // Assuming `id` is the patient ID
            pstm.setString(2, ""); // Placeholder for historique_ordonnance
            pstm.setString(3, ""); // Placeholder for historique_teleconsultation
            pstm.setString(4, ""); // Placeholder for historique_consultation_presentiel
            pstm.setFloat(5, informationsGenerales.getTaille());
            pstm.setFloat(6, informationsGenerales.getPoids());
            pstm.setString(7, informationsGenerales.hasMaladies() ? "Oui" : "Non");

            pstm.executeUpdate();

            System.out.println("Dossier médical ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du dossier médical : " + e.getMessage());
        }
    }

    // Retrieve all records from dossiermedicale
    @Override
    public List<InformationsGenerales> getAll() {
        List<InformationsGenerales> dossiers = new ArrayList<>();
        String qry = "SELECT * FROM dossiermedicale";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                InformationsGenerales info = new InformationsGenerales(1, "", "", "", 1.7, 45, true);
                info.setId(rs.getInt("id_patient")); // Linking to patient ID
                info.setTaille(rs.getFloat("taille"));
                info.setPoids(rs.getFloat("poids"));
                info.setMaladies("Oui".equals(rs.getString("maladies")));

                dossiers.add(info);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dossiers médicaux : " + e.getMessage());
        }

        return dossiers;
    }

    // Update dossiermedicale record
    @Override
    public void update(InformationsGenerales info) {
        String qry = "UPDATE dossiermedicale SET id_patient=? ,historique_ordonnance=?, historique_teleconsultation=?, historique_consultation_presentiel=?, taille=?, poids=?, maladies=? WHERE id=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, info.getIdPatient());  // Ensure this ID exists in DB
            pstm.setString(2, info.getHistoriqueOrdonnance());
            pstm.setString(3, info.getHistoriqueTeleconsultation());
            pstm.setString(4, info.getHistoriqueConsultationPresentiel());
            pstm.setFloat(5, info.getTaille());
            pstm.setFloat(6, info.getPoids());
            pstm.setString(7, info.hasMaladies() ? "Oui" : "Non");
            pstm.setInt(8, info.getId());


            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Dossier médical mis à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez si l'ID du patient existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du dossier médical : " + e.getMessage());
        }
    }




    // Delete dossiermedicale record by patient ID
    @Override
    public void delete(InformationsGenerales informationsGenerales) {
        String qry = "DELETE FROM dossiermedicale WHERE id=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, informationsGenerales.getId());

            pstm.executeUpdate();
            System.out.println("Dossier médical supprimé !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du dossier médical : " + e.getMessage());
        }
    }
}
