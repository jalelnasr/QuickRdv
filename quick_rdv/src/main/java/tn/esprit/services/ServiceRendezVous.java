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
        String qry = "INSERT INTO `rendezvous`(`date`, `patientId`, `medecinId`, `type_consultation_Id`) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            // Ensure the full timestamp is being passed (including both date and time)
            pstm.setTimestamp(1, new java.sql.Timestamp(rendezVous.getDate().getTime()));
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
        String qry = "SELECT rv.id, rv.date, rv.patientId, rv.medecinId, rv.type_consultation_id, " +
                "um.nom AS medecinNom, um.prenom AS medecinPrenom, " +
                "up.nom AS patientNom, up.prenom AS patientPrenom " +
                "FROM rendezvous rv " +
                "JOIN medecin m ON rv.medecinId = m.id " +
                "JOIN utilisateur um ON m.id = um.id " +
                "JOIN patient p ON rv.patientId = p.id " +
                "JOIN utilisateur up ON p.id = up.id";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                RendezVous rv = new RendezVous();
                rv.setId(rs.getInt("id"));
                rv.setDate(rs.getTimestamp("date"));
                rv.setPatientId(rs.getInt("patientId"));
                rv.setMedecinId(rs.getInt("medecinId"));
                rv.setTypeConsultationId(rs.getInt("type_consultation_id"));
                rv.setMedecinNom(rs.getString("medecinNom"));
                rv.setMedecinPrenom(rs.getString("medecinPrenom"));
                rv.setPatientNom(rs.getString("patientNom"));
                rv.setPatientPrenom(rs.getString("patientPrenom"));
                rendezVousList.add(rv);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des rendez-vous : " + e.getMessage());
        }

        return rendezVousList;
    }

    @Override
    public void update(RendezVous rendezVous) {
        String qry = "UPDATE `rendezvous` SET `date` = ?, `patientId` = ?, `medecinId` = ?, `type_consultation_id` = ? WHERE `id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setTimestamp(1, new java.sql.Timestamp(rendezVous.getDate().getTime()));
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
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, rendezVous.getId());
            pstm.executeUpdate();
            System.out.println("Rendez-vous supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du rendez-vous : " + e.getMessage());
        }
    }

    // Validates if a patient ID exists in the database
    public boolean isPatientIdValid(int patientId) {
        String query = "SELECT COUNT(*) FROM patient WHERE id = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, patientId);
            ResultSet rs = pstm.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de l'ID patient : " + e.getMessage());
            return false;
        }
    }

    // Checks if a doctor is available for a given date and time
    public boolean isMedecinAvailable(int medecinId, java.util.Date date) {
        // Prepare the start and end of the appointment (considering both date and time)
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);

        // Set the time to the exact time of the appointment
        java.util.Date appointmentDate = calendar.getTime();

        String query = "SELECT COUNT(*) FROM rendezvous WHERE medecinId = ? AND date = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(query)) {
            pstm.setInt(1, medecinId);
            pstm.setTimestamp(2, new java.sql.Timestamp(appointmentDate.getTime()));
            ResultSet rs = pstm.executeQuery();
            return rs.next() && rs.getInt(1) == 0; // No existing appointment at this exact time
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification de la disponibilité du médecin : " + e.getMessage());
            return false;
        }
    }
}
