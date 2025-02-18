package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.RendezVous;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRendezVous implements IService<RendezVous> {
    private Connection cnx;

    public ServiceRendezVous() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(RendezVous rendezVous) {
        // Corrected query: removed the extra comma and fixed column order.
        String qry = "INSERT INTO `rendezvous`(`date`, `patientId`, `medecinId`, `type_consultation_Id`) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, new java.sql.Timestamp(rendezVous.getDate().getTime())); // Convert Date to Timestamp
            pstm.setInt(2, rendezVous.getPatientId());
            pstm.setInt(3, rendezVous.getMedecinId());
            pstm.setInt(4, rendezVous.getTypeConsultationId());
            pstm.executeUpdate();
            System.out.println("Rendez-vous ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du rendez-vous : " + e.getMessage());
        }
    }

    @Override
    public List<RendezVous> getAll() {
        List<RendezVous> rendezVousList = new ArrayList<>();
        String qry = "SELECT * FROM `rendezvous`";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                RendezVous rv = new RendezVous();
                rv.setId(rs.getInt("id"));
                rv.setDate(rs.getTimestamp("date")); // Retrieve the date as a Timestamp
                // Make sure the column name matches your database column for type_consultation_id.
                rv.setPatientId(rs.getInt("patientId"));
                rv.setMedecinId(rs.getInt("medecinId"));
                rv.setTypeConsultationId(rs.getInt("type_consultation_id"));
                rendezVousList.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des rendez-vous : " + e.getMessage());
        }

        return rendezVousList;
    }

    @Override
    public void update(RendezVous rendezVous) {
        // Corrected query: removed extra comma before WHERE and fixed column name.
        String qry = "UPDATE `rendezvous` SET `date` = ?, `patientId` = ?, `medecinId` = ?, `type_consultation_id` = ? WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setTimestamp(1, new java.sql.Timestamp(rendezVous.getDate().getTime())); // Convert Date to Timestamp
            pstm.setInt(2, rendezVous.getPatientId());
            pstm.setInt(3, rendezVous.getMedecinId());
            pstm.setInt(4, rendezVous.getTypeConsultationId());
            pstm.setInt(5, rendezVous.getId());
            pstm.executeUpdate();
            System.out.println("Rendez-vous mis à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du rendez-vous : " + e.getMessage());
        }
    }

    @Override
    public void delete(RendezVous rendezVous) {
        String qry = "DELETE FROM `rendezvous` WHERE `id` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, rendezVous.getId());
            pstm.executeUpdate();
            System.out.println("Rendez-vous supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du rendez-vous : " + e.getMessage());
        }
    }

    public boolean isPatientIdValid(int patientId) {
        String query = "SELECT COUNT(*) FROM patient WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, patientId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the patientId exists
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'ID patient : " + e.getMessage());
        }
        return false; // Default to false if an error occurs
    }

}
