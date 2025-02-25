package tn.esprit.services;

import tn.esprit.models.Ordonnance;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ServiceOrdonnance {
    private Connection connection;

    public ServiceOrdonnance() {
        connection = MyDatabase.getInstance().getCnx();
    }

    // Fetch all ordonnances
    public List<Ordonnance> getAllOrdonnances() {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT o.*, m.specialite, u.nom, u.prenom " +
                "FROM ordonnance o " +
                "JOIN medecin m ON o.medecin_id = m.id " +
                "JOIN utilisateur u ON m.id = u.id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ordonnance ordonnance = mapResultSetToOrdonnance(resultSet);
                ordonnances.add(ordonnance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordonnances;
    }

    // Fetch ordonnances by patient ID
    public List<Ordonnance> getOrdonnancesByPatientId(int patientId) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT o.*, m.specialite, u.nom, u.prenom " +
                "FROM ordonnance o " +
                "JOIN medecin m ON o.medecin_id = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE o.patient_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ordonnance ordonnance = mapResultSetToOrdonnance(resultSet);
                ordonnances.add(ordonnance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordonnances;
    }

    // Fetch ordonnances by date
    public List<Ordonnance> getOrdonnancesByDate(String date) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT o.*, m.specialite, u.nom, u.prenom " +
                "FROM ordonnance o " +
                "JOIN medecin m ON o.medecin_id = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE o.date_prescription LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + date + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ordonnance ordonnance = mapResultSetToOrdonnance(resultSet);
                ordonnances.add(ordonnance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordonnances;
    }

    // Fetch ordonnances by doctor's name
    public List<Ordonnance> getOrdonnancesByDoctorName(String doctorName) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT o.*, m.specialite, u.nom, u.prenom " +
                "FROM ordonnance o " +
                "JOIN medecin m ON o.medecin_id = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE u.nom LIKE ? OR u.prenom LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + doctorName + "%");
            preparedStatement.setString(2, "%" + doctorName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ordonnance ordonnance = mapResultSetToOrdonnance(resultSet);
                ordonnances.add(ordonnance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordonnances;
    }

    // Helper method to map ResultSet to Ordonnance object
    private Ordonnance mapResultSetToOrdonnance(ResultSet resultSet) throws Exception {
        return new Ordonnance(
                resultSet.getInt("id"),
                resultSet.getInt("medecin_id"),
                resultSet.getInt("patient_id"),
                resultSet.getString("medicaments"),
                resultSet.getTimestamp("date_prescription"),
                resultSet.getString("instructions"),
                resultSet.getString("statut"),
                resultSet.getString("nom"),
                resultSet.getString("prenom"),
                resultSet.getString("specialite")
        );
    }
}