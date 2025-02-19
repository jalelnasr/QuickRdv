package tn.esprit.services;

import tn.esprit.models.Medicament;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceMedicament {
    private Connection cnx;

    public ServiceMedicament() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter un médicament
    public void add(Medicament medicament) {
        String qry = "INSERT INTO medicament (nom, stock, pharmacien_id) VALUES (?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
            pstm.setString(1, medicament.getNom());
            pstm.setInt(2, medicament.getStock());
            pstm.setInt(3, medicament.getPharmacienId());

            pstm.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                medicament.setId(rs.getInt(1));
            }

            System.out.println("Médicament ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du médicament : " + e.getMessage());
        }
    }

    // Récupérer tous les médicaments
    public List<Medicament> getAll() {
        List<Medicament> medicaments = new ArrayList<>();
        String qry = "SELECT * FROM medicament";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Medicament m = new Medicament(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("stock"),
                        rs.getInt("pharmacien_id")
                );
                medicaments.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des médicaments : " + e.getMessage());
        }

        return medicaments;
    }

    // Modifier un médicament
    public void update(Medicament medicament) {
        String qry = "UPDATE medicament SET nom=?, stock=?, pharmacien_id=? WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
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
    public void delete(int id) {
        String qry = "DELETE FROM medicament WHERE id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);

            pstm.executeUpdate();
            System.out.println("Médicament supprimé !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du médicament : " + e.getMessage());
        }
    }
}
