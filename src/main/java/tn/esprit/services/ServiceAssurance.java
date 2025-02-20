package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Assurance;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAssurance implements IService<Assurance> {
    private final Connection cnx;

    public ServiceAssurance() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Add a new record to assurance
    @Override
    public void add(Assurance assurance) {
        String qry = "INSERT INTO assurance (nom, type, date_debut, date_fin, montant_couvert) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType());
            pstm.setDate(3, Date.valueOf(assurance.getDateDebut()));
            pstm.setDate(4, Date.valueOf(assurance.getDateFin()));
            pstm.setFloat(5, assurance.getMontantCouvert());

            pstm.executeUpdate();
            System.out.println("Assurance ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'assurance : " + e.getMessage());
        }
    }

    // Retrieve all records from assurance
    @Override
    public List<Assurance> getAll() {
        List<Assurance> assurances = new ArrayList<>();
        String qry = "SELECT * FROM assurance";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Assurance assurance = new Assurance(
                        rs.getInt("id_assurance"),
                        rs.getString("nom"),
                        rs.getString("type"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getFloat("montant_couvert")
                );

                assurances.add(assurance);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des assurances : " + e.getMessage());
        }

        return assurances;
    }

    // Update assurance record
    @Override
    public void update(Assurance assurance) {
        String qry = "UPDATE assurance SET nom=?, type=?, date_debut=?, date_fin=?, montant_couvert=? WHERE id_assurance=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType());
            pstm.setDate(3, Date.valueOf(assurance.getDateDebut()));
            pstm.setDate(4, Date.valueOf(assurance.getDateFin()));
            pstm.setFloat(5, assurance.getMontantCouvert());
            pstm.setInt(6, assurance.getIdAssurance());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Assurance mise à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez si l'ID de l'assurance existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'assurance : " + e.getMessage());
        }
    }

    // Delete assurance record by ID
    @Override
    public void delete(Assurance assurance) {
        String qry = "DELETE FROM assurance WHERE id_assurance=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, assurance.getIdAssurance());

            pstm.executeUpdate();
            System.out.println("Assurance supprimée !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'assurance : " + e.getMessage());
        }
    }
}