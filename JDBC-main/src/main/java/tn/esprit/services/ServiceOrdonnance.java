package tn.esprit.services;

import tn.esprit.models.Ordonnance;
import tn.esprit.interfaces.IMService;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrdonnance implements IMService<Ordonnance> {
    private Connection cnx;

    public ServiceOrdonnance() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Ordonnance ordonnance) {
        String sql = "INSERT INTO ordonnance (medecin_id, patient_id, date_prescription, instructions, statut, medicaments) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ordonnance.getMedecinId());
            ps.setInt(2, ordonnance.getPatientId());
            ps.setDate(3, new java.sql.Date(ordonnance.getDatePrescription().getTime()));
            ps.setString(4, ordonnance.getInstructions());
            ps.setString(5, ordonnance.getStatut());
            ps.setString(6, mapToString(ordonnance.getMedicaments())); // ✅ Conversion avant stockage

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                ordonnance.setId(rs.getInt(1));
            }

            System.out.println("Ordonnance ajoutée avec succès !"+ ordonnance.getId());
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'ordonnance : " + e.getMessage());
        }
    }

    @Override
    public List<Ordonnance> getAll() {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM Ordonnance";

        try {
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Ordonnance o = new Ordonnance();
                o.setId(rs.getInt("id"));
                o.setMedicaments(stringToMap(rs.getString("medicaments"))); // ✅ Conversion après lecture

                ordonnances.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ordonnances : " + e.getMessage());
        }

        return ordonnances;
    }



    @Override
    public void update(Ordonnance ordonnance) {
        String sql = "UPDATE ordonnance SET medecin_id=?, patient_id=?, date_prescription=?, instructions=?, statut=?, medicaments=? WHERE id=?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, ordonnance.getMedecinId());
            ps.setInt(2, ordonnance.getPatientId());
            ps.setDate(3, new java.sql.Date(ordonnance.getDatePrescription().getTime()));
            ps.setString(4, ordonnance.getInstructions());
            ps.setString(5, ordonnance.getStatut());
            ps.setString(6, mapToString(ordonnance.getMedicaments())); // ✅ Conversion avant stockage
            ps.setInt(7, ordonnance.getId());

            ps.executeUpdate();
            System.out.println("Ordonnance mise à jour !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'ordonnance : " + e.getMessage());
        }
    }




    @Override
    public void delete(Ordonnance ordonnance) {
        String sql = "DELETE FROM ordonnance WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(sql);
            pstm.setInt(1, ordonnance.getId());

            pstm.executeUpdate();
            System.out.println("Ordonnance supprimée !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'ordonnance : " + e.getMessage());
        }
    }




    @Override
    public void validerOrdonnance(Ordonnance ordonnance) {
        String qry = "SELECT medicament_id, quantite FROM ordonnance_medicament WHERE ordonnance_id = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, ordonnance.getId());
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
            pstmOrdonnance.setInt(1, ordonnance.getId());
            pstmOrdonnance.executeUpdate();

            System.out.println("Ordonnance validée et stock mis à jour.");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la validation de l'ordonnance : " + e.getMessage());
        }


    }
    @Override

    public void insertOrdonnanceMedicaments(int ordonnanceId, Map<String, Integer> medicaments) {
        String sql = "INSERT INTO ordonnance_medicament (ordonnance_id, medicament_id, quantite) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
                ps.setInt(1, ordonnanceId);
                ps.setInt(2, Integer.parseInt(entry.getKey()));  // nom du médicament
                ps.setInt(3, entry.getValue()); // Quantité prescrite
                ps.executeUpdate();
            }
            System.out.println("Médicaments ajoutés à l'ordonnance !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout des médicaments : " + e.getMessage());
        }

    }

    @Override
    public void updateStockMedicament(int medicamentId, int quantite) {
        String sql = "UPDATE medicament SET stock = stock - ? WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, quantite);
            ps.setInt(2, medicamentId);
            ps.executeUpdate();
            System.out.println("Stock mis à jour pour le médicament ID : " + medicamentId);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du stock : " + e.getMessage());
        }

    }


}
