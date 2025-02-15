package tn.esprit.services;

import tn.esprit.models.Pharmacien;
import tn.esprit.models.Hopital;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePharmacien {
    private Connection cnx;

    public ServicePharmacien() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public void add(Pharmacien pharmacien) {
        try {
            // Check if the user already exists
            String checkUserExistence = "SELECT COUNT(*) FROM Utilisateur WHERE email = ?";
            PreparedStatement pstCheck = cnx.prepareStatement(checkUserExistence);
            pstCheck.setString(1, pharmacien.getEmail());
            ResultSet rs = pstCheck.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                // User does not exist, insert the user
                String insertUtilisateur = "INSERT INTO Utilisateur (id, nom, prenom, email, motDePasse) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstUser = cnx.prepareStatement(insertUtilisateur);
                pstUser.setInt(1, pharmacien.getId());
                pstUser.setString(2, pharmacien.getNom());
                pstUser.setString(3, pharmacien.getPrenom());
                pstUser.setString(4, pharmacien.getEmail());
                pstUser.setString(5, pharmacien.getMotDePasse());
                pstUser.executeUpdate();
            } else {
                System.out.println("Un utilisateur avec cet email existe déjà.");
            }

            // Now, add the pharmacien
            String query = "INSERT INTO Pharmacien (utilisateur_id, hopital_id) VALUES (?, ?)";
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, pharmacien.getId());
            pstm.setInt(2, pharmacien.getHopital().getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Pharmacien> getAll() {
        List<Pharmacien> pharmaciens = new ArrayList<>();
        String query = "SELECT u.id, u.nom, u.prenom, u.email, u.motDePasse, h.id, h.nom, h.adresse, h.email, h.telephone " +
                "FROM Pharmacien p " +
                "JOIN Utilisateur u ON p.utilisateur_id = u.id " +
                "JOIN Hopital h ON p.hopital_id = h.id";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                Hopital hopital = new Hopital(
                        rs.getInt(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9),
                        rs.getString(10)
                );

                Pharmacien pharmacien = new Pharmacien(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        hopital
                );

                pharmaciens.add(pharmacien);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return pharmaciens;
    }

    public void update(Pharmacien pharmacien) {
        String query = "UPDATE Pharmacien SET hopital_id=? WHERE utilisateur_id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, pharmacien.getHopital().getId());
            pstm.setInt(2, pharmacien.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(Pharmacien pharmacien) {
        String query = "DELETE FROM Pharmacien WHERE utilisateur_id=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(query);
            pstm.setInt(1, pharmacien.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
