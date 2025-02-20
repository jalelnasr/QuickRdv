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

    // Add a new record to dossiermedicale with the new fields
    @Override
    public void add(InformationsGenerales informationsGenerales) {
        String qry = "INSERT INTO dossiermedicale (id_patient, taille, poids, maladies, antecedents_cardiovasculaires_familiaux, asthmatique, " +
                "suivi_dentaire_regulier, antecedents_chirurgicaux, allergies, profession, niveau_de_stress, qualite_de_sommeil, activite_physique, situation_familiale) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, informationsGenerales.getIdPatient());
            pstm.setFloat(2, informationsGenerales.getTaille());
            pstm.setFloat(3, informationsGenerales.getPoids());
            pstm.setString(4, informationsGenerales.hasMaladies() ? "Oui" : "Non");
            pstm.setString(5, informationsGenerales.getAntecedentsCardiovasculairesFamiliaux());
            pstm.setString(6, informationsGenerales.getAsthmatique());
            pstm.setString(7, informationsGenerales.getSuiviDentaireRegulier());
            pstm.setString(8, informationsGenerales.getAntecedentsChirurgicaux());
            pstm.setString(9, informationsGenerales.getAllergies());
            pstm.setString(10, informationsGenerales.getProfession());
            pstm.setString(11, informationsGenerales.getNiveauDeStress());
            pstm.setString(12, informationsGenerales.getQualiteDeSommeil());
            pstm.setString(13, informationsGenerales.getActivitePhysique());
            pstm.setString(14, informationsGenerales.getSituationFamiliale());

            pstm.executeUpdate();
            System.out.println("Dossier médical ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du dossier médical : " + e.getMessage());
        }
    }

    // Retrieve all records from dossiermedicale with the new fields
    @Override
    public List<InformationsGenerales> getAll() {
        List<InformationsGenerales> dossiers = new ArrayList<>();
        String qry = "SELECT * FROM dossiermedicale";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                InformationsGenerales info = new InformationsGenerales(
                        rs.getInt("id"),
                        rs.getInt("id_patient"),
                        rs.getFloat("taille"),
                        rs.getFloat("poids"),
                        "Oui".equals(rs.getString("maladies")),
                        rs.getString("antecedents_cardiovasculaires_familiaux"),
                        rs.getString("asthmatique"),
                        rs.getString("suivi_dentaire_regulier"),
                        rs.getString("antecedents_chirurgicaux"),
                        rs.getString("allergies"),
                        rs.getString("profession"),
                        rs.getString("niveau_de_stress"),
                        rs.getString("qualite_de_sommeil"),
                        rs.getString("activite_physique"),
                        rs.getString("situation_familiale")
                );

                dossiers.add(info);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des dossiers médicaux : " + e.getMessage());
        }

        return dossiers;
    }

    // Update dossiermedicale record with the new fields
    @Override
    public void update(InformationsGenerales info) {
        String qry = "UPDATE dossiermedicale SET id_patient=?, taille=?, poids=?, maladies=?, antecedents_cardiovasculaires_familiaux=?, asthmatique=?, " +
                "suivi_dentaire_regulier=?, antecedents_chirurgicaux=?, allergies=?, profession=?, niveau_de_stress=?, qualite_de_sommeil=?, activite_physique=?, " +
                "situation_familiale=? WHERE id=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, info.getIdPatient());
            pstm.setFloat(2, info.getTaille());
            pstm.setFloat(3, info.getPoids());
            pstm.setString(4, info.hasMaladies() ? "Oui" : "Non");
            pstm.setString(5, info.getAntecedentsCardiovasculairesFamiliaux());
            pstm.setString(6, info.getAsthmatique());
            pstm.setString(7, info.getSuiviDentaireRegulier());
            pstm.setString(8, info.getAntecedentsChirurgicaux());
            pstm.setString(9, info.getAllergies());
            pstm.setString(10, info.getProfession());
            pstm.setString(11, info.getNiveauDeStress());
            pstm.setString(12, info.getQualiteDeSommeil());
            pstm.setString(13, info.getActivitePhysique());
            pstm.setString(14, info.getSituationFamiliale());
            pstm.setInt(15, info.getId());

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
