package tn.esprit.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import tn.esprit.interfaces.IService;
import tn.esprit.models.Administrateur;
import tn.esprit.models.Patient;
import tn.esprit.models.Pharmacien;
import tn.esprit.models.Medecin;
import tn.esprit.models.Utilisateur;
import tn.esprit.utils.MyDatabase;

public class ServiceUtilisateur implements IService<Utilisateur> {
    private Connection cnx = MyDatabase.getInstance().getCnx();

    public ServiceUtilisateur() {
    }
    @Override
    public void add(Utilisateur utilisateur) {
        String qry = "INSERT INTO utilisateur(nom, prenom, email, mot_de_passe, role) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = cnx.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, utilisateur.getNom());
            pstm.setString(2, utilisateur.getPrenom());
            pstm.setString(3, utilisateur.getEmail());
            pstm.setString(4, utilisateur.getMotDePasse());
            pstm.setString(5, utilisateur.getRole());
            pstm.executeUpdate();


            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);

                if (utilisateur instanceof Administrateur) {
                    String qryAdmin = "INSERT INTO administrateur(id) VALUES (?)";
                    try (PreparedStatement admins = cnx.prepareStatement(qryAdmin)) {
                        admins.setInt(1, generatedId);  // Utiliser l'ID généré
                        admins.executeUpdate();
                    }
                }
                if (utilisateur instanceof Pharmacien) {
                    String qrypharmacien = "INSERT INTO pharmacien(id) VALUES (?)";
                    try (PreparedStatement pharmaciens = cnx.prepareStatement(qrypharmacien)) {
                        pharmaciens.setInt(1, generatedId);  // Utiliser l'ID généré
                        pharmaciens.executeUpdate();
                    }
                }

                if (utilisateur instanceof Patient) {
                    String qryPatient = "INSERT INTO `patient`(`id`, `numDossier`, `dateNaissance`, `adresse`) VALUES (?, ?, ?, ?)";
                    Patient p = (Patient) utilisateur;
                    try (PreparedStatement ps = cnx.prepareStatement(qryPatient)) {
                        ps.setInt(1, generatedId);  // ID généré
                        ps.setString(2, p.getNumDossier());
                        ps.setString(3, p.getDateNaissance());
                        ps.setString(4, p.getAdresse());
                        ps.executeUpdate();
                    }
                }
                if (utilisateur instanceof Medecin) {
                    String qryMedecin = "INSERT INTO `medecin`(`id`, `specialite`, `disponibilite`, `num_rdv_max`, `numeroRPPS`) VALUES (?,?,?,?,?)";
                    Medecin m = (Medecin) utilisateur;
                    try (PreparedStatement ps = cnx.prepareStatement(qryMedecin)) {
                        ps.setInt(1, generatedId);
                        ps.setString(2, m.getSpecialite());
                        ps.setString(3, m.getDisponibilite());
                        ps.setString(4,m.getNum_rdv_max());
                        ps.setString(5,m.getNumeroRPPS());
                        ps.executeUpdate();

                    }
                }


            }

            System.out.println("✅ Utilisateur ajouté avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }


    @Override
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String qry = "SELECT * FROM utilisateur";

        try (Statement stm = cnx.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setEmail(rs.getString("email"));
                u.setMotDePasse(rs.getString("mot_de_passe"));
                u.setRole(rs.getString("role"));
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return utilisateurs;
    }

    @Override
    public void update(Utilisateur utilisateur) {
        String qry = "UPDATE utilisateur SET nom=?, prenom=?, email=?, mot_de_passe=?, role=? WHERE id=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setString(1, utilisateur.getNom());
            pstm.setString(2, utilisateur.getPrenom());
            pstm.setString(3, utilisateur.getEmail());
            pstm.setString(4, utilisateur.getMotDePasse());
            pstm.setString(5, utilisateur.getRole());
            pstm.setInt(6, utilisateur.getId());
            System.out.println(utilisateur.getId());
            pstm.executeUpdate();

            // Mise à jour spécifique selon le rôle de l'utilisateur
            if (utilisateur instanceof Patient) {
                String qryPatient = "UPDATE patient SET numDossier=?, dateNaissance=?, adresse=? WHERE id=?";
                Patient p = (Patient) utilisateur;
                try (PreparedStatement ps = cnx.prepareStatement(qryPatient)) {
                    ps.setString(1, p.getNumDossier());
                    ps.setString(2, p.getDateNaissance());
                    ps.setString(3, p.getAdresse());
                    ps.setInt(4, utilisateur.getId());
                    ps.executeUpdate();
                }
            }

            if (utilisateur instanceof Medecin) {
                String qryMedecin = "UPDATE medecin SET specialite=?, disponibilite=?, num_rdv_max=?, numeroRPPS=? WHERE id=?";
                Medecin m = (Medecin) utilisateur;
                try (PreparedStatement ps = cnx.prepareStatement(qryMedecin)) {
                    ps.setString(1, m.getSpecialite());
                    ps.setString(2, m.getDisponibilite());
                    ps.setString(3, m.getNum_rdv_max());
                    ps.setString(4, m.getNumeroRPPS());
                    ps.setInt(5, utilisateur.getId());
                    ps.executeUpdate();
                }
            }

            if (utilisateur instanceof Administrateur) {
                // La table administrateur n'a qu'une seule colonne `id`, donc aucune mise à jour n'est nécessaire.
                // Si d'autres attributs sont ajoutés à `administrateur`, vous pourrez les mettre à jour ici.
            }

            if (utilisateur instanceof Pharmacien) {
                // Idem pour la table pharmacien, actuellement elle ne contient que `id`
                // Si elle contient d'autres champs, ajoutez leur mise à jour ici.
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            }


        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }




    @Override
    public void delete(int id) {
        String qry = "DELETE FROM utilisateur WHERE id=?";

        try (PreparedStatement pstm = cnx.prepareStatement(qry)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
            System.out.println("✅ Utilisateur supprimé avec succès !");
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }

    // ✅ Correction : La méthode emailExists est maintenant dans la classe
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // L'email existe déjà
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la vérification de l'email : " + e.getMessage());
        }
        return false;
    }
}
