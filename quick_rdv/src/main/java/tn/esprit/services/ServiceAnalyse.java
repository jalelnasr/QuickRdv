package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.interfaces.IServiceAnalyse;
import tn.esprit.models.Analyse;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAnalyse implements IServiceAnalyse<Analyse> {
    private Connection cnx;

    public ServiceAnalyse() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Analyse analyse) {
        String qry = "INSERT INTO `demande_analyse`(`date`, `type`, `instructions`, `id_patient`, `id_medecin`, `id_rendezvous`) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setTimestamp(1, new java.sql.Timestamp(analyse.getDate().getTime()));
            pstm.setString(2, analyse.getType());
            pstm.setString(3, analyse.getInstructions());
            pstm.setInt(4, analyse.getPatientId());
            pstm.setInt(5, analyse.getMedecinId());
            pstm.setInt(6, analyse.getIdRendezVous());
            pstm.executeUpdate();
            System.out.println("Analyse ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'analyse : " + e.getMessage());
        }
    }

    @Override
    public List<Analyse> getAll() {
        List<Analyse> analyseList = new ArrayList<>();
        String qry = "SELECT da.id, da.date, da.type, da.instructions, da.id_patient, da.id_medecin, da.id_rendezvous, " +
                "u.nom AS medecinNom, u.prenom AS medecinPrenom " +
                "FROM demande_analyse da " +
                "JOIN medecin m ON da.id_medecin = m.id " +
                "JOIN utilisateur u ON m.id = u.id";

        try (Statement stm = cnx.createStatement(); ResultSet rs = stm.executeQuery(qry)) {
            while (rs.next()) {
                Analyse analyse = new Analyse();
                analyse.setId(rs.getInt("id"));
                analyse.setDate(rs.getTimestamp("date"));
                analyse.setType(rs.getString("type"));
                analyse.setInstructions(rs.getString("instructions"));
                analyse.setPatientId(rs.getInt("id_patient"));
                analyse.setMedecinId(rs.getInt("id_medecin"));
                analyse.setIdRendezVous(rs.getInt("id_rendezvous"));
                analyse.setMedecinNom(rs.getString("medecinNom"));
                analyse.setMedecinPrenom(rs.getString("medecinPrenom"));
                analyseList.add(analyse);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des analyses : " + e.getMessage());
        }
        return analyseList;
    }

    @Override
    public void update(Analyse analyse) {
        String qry = "UPDATE `demande_analyse` SET `type` = ?, `instructions` = ?, `id_patient` = ?, `id_medecin` = ?, `id_rendezvous` = ? WHERE `id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, analyse.getType());
            pstm.setString(2, analyse.getInstructions());
            pstm.setInt(3, analyse.getPatientId());
            pstm.setInt(4, analyse.getMedecinId());
            pstm.setInt(5, analyse.getIdRendezVous());
            pstm.setInt(6, analyse.getId());
            pstm.executeUpdate();
            System.out.println("Analyse mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'analyse : " + e.getMessage());
        }
    }

    @Override
    public void delete(Analyse analyse) {
        String qry = "DELETE FROM `demande_analyse` WHERE `id` = ?";
        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, analyse.getId());
            pstm.executeUpdate();
            System.out.println("Analyse supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'analyse : " + e.getMessage());
        }
    }
}