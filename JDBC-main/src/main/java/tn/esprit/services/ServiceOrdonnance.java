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

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int ordonnanceId = rs.getInt(1);
                    ordonnance.setId(ordonnanceId); // Mettre à jour l'ID de l'objet
                    System.out.println("Ordonnance ajoutée avec succès ! ID : " + ordonnanceId);
                }
            } else {
                System.out.println("❌ Erreur lors de l'ajout de l'ordonnance.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
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
                o.setMedecinId(rs.getInt("medecin_id")); // Vérifier ces noms de colonnes
                o.setPatientId(rs.getInt("patient_id"));
                o.setMedicaments(stringToMap(rs.getString("medicaments"))); // ✅ Conversion après lecture
                o.setDatePrescription(rs.getDate("date_prescription"));
                o.setInstructions(rs.getString("instructions"));
                o.setStatut(rs.getString("statut"));

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
        String qry = "SELECT medicament_nom, quantite FROM ordonnance_medicament WHERE ordonnance_id = ?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, ordonnance.getId());
            try (ResultSet rs = pstm.executeQuery()) {

                boolean foundMedicaments = false;

                while (rs.next()) {
                    foundMedicaments = true;
                    String medicamentNom = rs.getString("medicament_nom");
                    int quantite = rs.getInt("quantite");

                    // 🔹 Vérifier si le médicament existe avant de mettre à jour le stock
                    //if (verifierExistenceMedicament(medicamentNom)) {
                       // updateStockMedicament(medicamentNom, quantite);
                    //} else {
                        //System.out.println("⚠️ Médicament introuvable en base : " + medicamentNom);
                   // }
                }

                // 🔹 Si aucun médicament trouvé, on annule la validation
                if (!foundMedicaments) {
                    System.out.println("⚠️ Aucune donnée trouvée pour l'ordonnance ID: " + ordonnance.getId());
                    return;
                }

                // 🔹 Changer le statut de l'ordonnance à "Validée"
                String updateOrdonnance = "UPDATE ordonnance SET statut = 'Validée' WHERE id = ?";
                try (PreparedStatement pstmOrdonnance = cnx.prepareStatement(updateOrdonnance)) {
                    pstmOrdonnance.setInt(1, ordonnance.getId());
                    int rowsUpdated = pstmOrdonnance.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("✅ Ordonnance validée avec succès !");
                    } else {
                        System.out.println("⚠️ La mise à jour du statut de l'ordonnance a échoué.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la validation de l'ordonnance : " + e.getMessage());
        }
    }

    @Override

    public void insertOrdonnanceMedicaments(int ordonnanceId, Map<String, Integer> medicaments) {
        String sql = "INSERT INTO ordonnance_medicament (ordonnance_id, medicament_nom, quantite) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
                ps.setInt(1, ordonnanceId);
                ps.setString(2, entry.getKey());  // nom du médicament
                ps.setInt(3, entry.getValue()); // Quantité prescrite
                ps.executeUpdate();
            }
            System.out.println("Médicaments ajoutés à l'ordonnance !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout des médicaments : " + e.getMessage());
        }

    }

    @Override
    public void updateStockMedicament(String medicamentNom, int quantite) {
        String sql = "UPDATE medicament SET stock = stock - ? WHERE id = ?";
        try {
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setInt(1, quantite);
            ps.setString(2, medicamentNom);
            ps.executeUpdate();
            System.out.println("Stock mis à jour pour le médicament : " + medicamentNom);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du stock : " + e.getMessage());
        }

    }

    public List<String> searchMedicaments(String searchText) {
        List<String> medicaments = new ArrayList<>();

        // Requête SQL pour rechercher les médicaments contenant le texte saisi
        String query = "SELECT nom FROM medicament WHERE nom LIKE ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/quick_rdv", "root", "");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%"); // Recherche floue
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                medicaments.add(rs.getString("nom"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return medicaments;
    }


    public List<Ordonnance> getOrdonnancesByDate(String date) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM ordonnance WHERE DATE(date_prescription) = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, date); // Filtrer par date
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Ordonnance o = new Ordonnance();
                o.setId(rs.getInt("id"));
                o.setMedecinId(rs.getInt("medecin_id"));
                o.setPatientId(rs.getInt("patient_id"));
                o.setMedicaments(stringToMap(rs.getString("medicaments")));
                o.setDatePrescription(rs.getDate("date_prescription"));
                o.setInstructions(rs.getString("instructions"));
                o.setStatut(rs.getString("statut"));
                ordonnances.add(o);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ordonnances par date : " + e.getMessage());
        }

        return ordonnances;
    }

    public void updateStatut(int ordonnanceId, String statut) {
        String sql = "UPDATE ordonnance SET statut = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setInt(2, ordonnanceId);
            ps.executeUpdate();
            System.out.println("Statut de l'ordonnance mis à jour : " + statut);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du statut de l'ordonnance : " + e.getMessage());
        }
    }

    /*private Ordonnance mapResultSetToOrdonnance(ResultSet resultSet) throws SQLException {
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setId(resultSet.getInt("id"));
        ordonnance.setMedecinId(resultSet.getInt("medecin_id"));
        ordonnance.setPatientId(resultSet.getInt("patient_id"));
        ordonnance.setMedicaments(stringToMap(resultSet.getString("medicaments"))); // Utiliser votre méthode de conversion
        ordonnance.setDatePrescription(resultSet.getDate("date_prescription"));
        ordonnance.setInstructions(resultSet.getString("instructions"));
        ordonnance.setStatut(resultSet.getString("statut"));

        // Vous pouvez ajouter des champs supplémentaires si nécessaire
        String specialite = resultSet.getString("specialite");
        String nomMedecin = resultSet.getString("nom");
        String prenomMedecin = resultSet.getString("prenom");

        // Vous pouvez aussi stocker ces informations dans l'objet Ordonnance si nécessaire
        ordonnance.setMedecinId(nomMedecin);
        ordonnance.setMedecinPrenom(prenomMedecin);
        ordonnance.setMedecinSpecialite(specialite);

        return ordonnance;
    }



    public List<Ordonnance> getOrdonnancesByDoctorName(String doctorName) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT o.*, m.specialite, u.nom, u.prenom " +
                "FROM ordonnance o " +
                "JOIN medecin m ON o.medecin_id = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE u.nom LIKE ? OR u.prenom LIKE ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            // Préparer la requête avec les paramètres de recherche
            preparedStatement.setString(1, "%" + doctorName + "%");
            preparedStatement.setString(2, "%" + doctorName + "%");

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // Parcourir le résultat et ajouter chaque ordonnance à la liste
                while (resultSet.next()) {
                    Ordonnance ordonnance = mapResultSetToOrdonnance(resultSet);
                    ordonnances.add(ordonnance);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des ordonnances par nom de médecin : " + e.getMessage());
        }

        return ordonnances;
    }*/


}







