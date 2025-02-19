package tn.esprit.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Reclamation;
import tn.esprit.utils.MyDatabase;

public class ServiceReclamation implements IService<Reclamation> {
    private Connection cnx = MyDatabase.getInstance().getCnx();

    public ServiceReclamation() {
    }

    @Override
    public void add(Reclamation reclamation) {
        String qry = "INSERT INTO `reclamation`(`utilisateur_id`, `rendez_vous_id`, `sujet`, `description`, `date_reclamation`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getUtilisateur_id());
            pstm.setInt(2, reclamation.getRendez_vous_id());
            pstm.setString(3, reclamation.getSujet());
            pstm.setString(4, reclamation.getDescription());
            pstm.setDate(5, new Date(reclamation.getDate_reclamation().getTime()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Reclamation> getAll() {
        List<Reclamation> reclamationList = new ArrayList<>();
        String qry = "SELECT * FROM `reclamation`";

        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setId(rs.getInt("id"));
                reclamation.setUtilisateur_id(rs.getInt("utilisateur_id"));
                reclamation.setRendez_vous_id(rs.getInt("rendez_vous_id"));
                reclamation.setSujet(rs.getString("sujet"));
                reclamation.setDescription(rs.getString("description"));
                reclamation.setDate_reclamation(rs.getDate("date_reclamation"));
                reclamationList.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return reclamationList;
    }

    @Override
    public void update(Reclamation reclamation) {
        String qry = "UPDATE `reclamation` SET `utilisateur_id`=?, `rendez_vous_id`=?, `sujet`=?, `description`=?, `date_reclamation`=? WHERE `id`=?";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getUtilisateur_id());
            pstm.setInt(2, reclamation.getRendez_vous_id());
            pstm.setString(3, reclamation.getSujet());
            pstm.setString(4, reclamation.getDescription());
            pstm.setDate(5, new Date(reclamation.getDate_reclamation().getTime()));
            pstm.setInt(6, reclamation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Reclamation reclamation) {
        String qry = "DELETE FROM `reclamation` WHERE `id`=?";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, reclamation.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
