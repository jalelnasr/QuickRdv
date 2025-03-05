package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Assurance;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceAssurance implements IService<Assurance> {
    private final Connection cnx;

    public ServiceAssurance() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Assurance assurance) {
        String qry = "INSERT INTO assurance (nom, type, date_debut, date_fin, id_PatientAs) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType());
            pstm.setDate(3, Date.valueOf(assurance.getDateDebut()));
            pstm.setDate(4, Date.valueOf(assurance.getDateFin()));
            pstm.setInt(5, 1); // Hardcode id_PatientAs to 1

            int rowsInserted = pstm.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Assurance ajoutée avec succès !");
            } else {
                System.out.println("Erreur lors de l'ajout.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (ajout) : " + e.getMessage());
        }
    }

    public List<Assurance> getAll() {
        List<Assurance> assurances = new ArrayList<>();
        String qry = "SELECT * FROM assurance WHERE id_PatientAs = 1"; // Ajout de la clause WHERE
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(qry)) {
            while (rs.next()) {
                // Handle NULL values for date_debut and date_fin
                LocalDate dateDebut = rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null;
                LocalDate dateFin = rs.getDate("date_fin") != null ? rs.getDate("date_fin").toLocalDate() : null;

                // Use the correct constructor with id_PatientAs
                Assurance assurance = new Assurance(
                        rs.getInt("id_assurance"), // idAssurance
                        rs.getString("nom"),       // nom
                        rs.getString("type"),      // type
                        dateDebut,                 // dateDebut
                        dateFin,                   // dateFin
                        rs.getInt("id_PatientAs")  // idPatientAs
                );
                assurances.add(assurance);
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (getAll) : " + e.getMessage());
        }
        return assurances;
    }

    public Assurance getById(int id) {
        String qry = "SELECT * FROM assurance WHERE id_assurance = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    // Handle NULL values for date_debut and date_fin
                    LocalDate dateDebut = rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null;
                    LocalDate dateFin = rs.getDate("date_fin") != null ? rs.getDate("date_fin").toLocalDate() : null;

                    // Use the correct constructor with id_PatientAs
                    return new Assurance(
                            rs.getInt("id_assurance"), // idAssurance
                            rs.getString("nom"),       // nom
                            rs.getString("type"),      // type
                            dateDebut,                 // dateDebut
                            dateFin,                   // dateFin
                            rs.getInt("id_PatientAs")  // idPatientAs
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (getById) : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Assurance assurance) {
        String qry = "UPDATE assurance SET nom=?, type=?, date_debut=?, date_fin=? WHERE id_assurance=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, assurance.getNom());
            pstm.setString(2, assurance.getType());
            pstm.setDate(3, Date.valueOf(assurance.getDateDebut()));
            pstm.setDate(4, Date.valueOf(assurance.getDateFin()));
            pstm.setInt(5, assurance.getIdAssurance());

            int rowsUpdated = pstm.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Assurance mise à jour avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée. Vérifiez si l'ID de l'assurance existe.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (update) : " + e.getMessage());
        }
    }

    @Override
    public void delete(Assurance assurance) {
        String qry = "DELETE FROM assurance WHERE id_assurance=?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, assurance.getIdAssurance());

            int rowsDeleted = pstm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Assurance supprimée avec succès !");
            } else {
                System.out.println("Aucune assurance trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur SQL (delete) : " + e.getMessage());
        }
    }
}