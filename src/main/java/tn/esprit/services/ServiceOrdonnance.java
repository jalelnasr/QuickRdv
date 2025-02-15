package tn.esprit.services;

import tn.esprit.models.Ordonnance;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceOrdonnance {
    private Connection cnx;

    public ServiceOrdonnance() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter une ordonnance avec des médicaments
    public void add(Ordonnance ordonnance) {
        String qry = "INSERT INTO ordonnance (medecin_id, patient_id, datePrescription, instructions, statut) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, ordonnance.getMedecinId());
            pstm.setInt(2, ordonnance.getPatientId());
            pstm.setDate(3, new java.sql.Date(ordonnance.getDatePrescription().getTime()));
            pstm.setString(4, ordonnance.getInstructions());
            pstm.setString(5, ordonnance.getStatut());


            pstm.executeUpdate();

            // Récupérer l'ID de l'ordonnance nouvellement insérée
            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                int ordonnanceId = rs.getInt(1);
                insertOrdonnanceMedicaments(ordonnanceId, ordonnance.getMedicaments());
            }

            System.out.println("Ordonnance ajoutée avec succès.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Associer des médicaments à une ordonnance
    private void insertOrdonnanceMedicaments(int ordonnanceId, Map<Integer, Integer> medicaments) {
        String qry = "INSERT INTO ordonnance_medicament (ordonnance_id, medicament_id, quantite) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            for (Map.Entry<Integer, Integer> entry : medicaments.entrySet()) {
                pstm.setInt(1, ordonnanceId);
                pstm.setInt(2, entry.getKey());
                pstm.setInt(3, entry.getValue());
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Mettre à jour le stock de médicaments après validation
    public void validerOrdonnance(int ordonnanceId) {
        String qry = "SELECT medicament_id, quantite FROM ordonnance_medicament WHERE ordonnance_id = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, ordonnanceId);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                int medicamentId = rs.getInt("medicament_id");
                int quantite = rs.getInt("quantite");

                // Mettre à jour le stock
                updateStockMedicament(medicamentId, quantite);
            }

            // Changer le statut de l'ordonnance à "Validée"
            String updateOrdonnance = "UPDATE ordonnance SET statut = 'Validée' WHERE id = ?";
            PreparedStatement pstmOrdonnance = cnx.prepareStatement(updateOrdonnance);
            pstmOrdonnance.setInt(1, ordonnanceId);
            pstmOrdonnance.executeUpdate();

            System.out.println("Ordonnance validée et stock mis à jour.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Réduire le stock des médicaments
    private void updateStockMedicament(int medicamentId, int quantite) {
        String qry = "UPDATE medicament SET stock = stock - ? WHERE id = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, quantite);
            pstm.setInt(2, medicamentId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void update(Ordonnance ordonnance) {
        String query = "UPDATE ordonnance SET medecin_id = ?, patient_id = ?, date_prescription = ?, instructions = ?, statut = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, ordonnance.getMedecinId());
            stmt.setInt(2, ordonnance.getPatientId());
            stmt.setDate(3, new java.sql.Date(ordonnance.getDatePrescription().getTime()));
            stmt.setString(4, ordonnance.getInstructions());
            stmt.setString(5, ordonnance.getStatut());
            stmt.setInt(6, ordonnance.getId());
            stmt.executeUpdate();
            System.out.println("Ordonnance mise à jour avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "DELETE FROM ordonnance WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Ordonnance supprimée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}