
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
import tn.esprit.models.Avis;
import tn.esprit.utils.MyDatabase;

public class ServiceAvis implements IService<Avis> {
    private Connection cnx = MyDatabase.getInstance().getCnx();

    public ServiceAvis() {
    }

    public void add(Avis avis) {
        String qry = "INSERT INTO `avis`(`patient_id`, `medecin_id`, `commentaire`, `note`, `date_avis`) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, avis.getPatient_id());
            pstm.setInt(2, avis.getMedecin_id());
            pstm.setString(3, avis.getCommentaire());
            pstm.setInt(4, avis.getNote());
            pstm.setDate(5, new Date(avis.getDate_avis().getTime()));
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<Avis> getAll() {
        List<Avis> avisList = new ArrayList();
        String qry = "SELECT * FROM `avis`";

        try {
            Statement stm = this.cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while(rs.next()) {
                Avis avis = new Avis();
                avis.setId(rs.getInt("id"));
                avis.setPatient_id(rs.getInt("patient_id"));
                avis.setMedecin_id(rs.getInt("medecin_id"));
                avis.setCommentaire(rs.getString("commentaire"));
                avis.setNote(rs.getInt("note"));
                avis.setDate_avis(rs.getDate("date_avis"));
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return avisList;
    }

    public void update(Avis avis) {
        String qry = "UPDATE `avis` SET `patient_id`=?, `medecin_id`=?, `commentaire`=?, `note`=?, `date_avis`=? WHERE `id`=?";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, avis.getPatient_id());
            pstm.setInt(2, avis.getMedecin_id());
            pstm.setString(3, avis.getCommentaire());
            pstm.setInt(4, avis.getNote());
            pstm.setDate(5, new Date(avis.getDate_avis().getTime()));
            pstm.setInt(6, avis.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void delete(Avis avis) {
        String qry = "DELETE FROM `avis` WHERE `id`=?";

        try {
            PreparedStatement pstm = this.cnx.prepareStatement(qry);
            pstm.setInt(1, avis.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
