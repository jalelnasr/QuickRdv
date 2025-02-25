package tn.esprit.services;

import tn.esprit.models.Consultation;
import tn.esprit.utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ServiceConsultation {
    private Connection connection;

    public ServiceConsultation() {
        connection = MyDatabase.getInstance().getCnx();
    }

    // Fetch consultations by patient ID
    public List<Consultation> getConsultationsByPatientId(int idPatient) {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT r.*, u.nom AS medecinNom, u.prenom AS medecinPrenom, m.specialite AS medecinSpecialite " +
                "FROM rendez_vous r " +
                "JOIN medecin m ON r.id_medecin = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE r.id_patient = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idPatient);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Consultation consultation = mapResultSetToConsultation(resultSet);
                consultations.add(consultation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }

    // Fetch consultations by date
    public List<Consultation> getConsultationsByDate(String date) {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT r.*, u.nom AS medecinNom, u.prenom AS medecinPrenom, m.specialite AS medecinSpecialite " +
                "FROM rendez_vous r " +
                "JOIN medecin m ON r.id_medecin = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE r.dateHeure LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + date + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Consultation consultation = mapResultSetToConsultation(resultSet);
                consultations.add(consultation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }

    // Fetch consultations by doctor's name
    public List<Consultation> getConsultationsByDoctorName(String doctorName) {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT r.*, u.nom AS medecinNom, u.prenom AS medecinPrenom, m.specialite AS medecinSpecialite " +
                "FROM rendez_vous r " +
                "JOIN medecin m ON r.id_medecin = m.id " +
                "JOIN utilisateur u ON m.id = u.id " +
                "WHERE u.nom LIKE ? OR u.prenom LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + doctorName + "%");
            preparedStatement.setString(2, "%" + doctorName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Consultation consultation = mapResultSetToConsultation(resultSet);
                consultations.add(consultation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }

    // Helper method to map ResultSet to Consultation object
    private Consultation mapResultSetToConsultation(ResultSet resultSet) throws Exception {
        return new Consultation(
                resultSet.getInt("id"),
                resultSet.getTimestamp("dateHeure"),
                resultSet.getString("typeConsultation"),
                resultSet.getInt("id_patient"),
                resultSet.getInt("id_medecin"),
                resultSet.getString("medecinNom"),
                resultSet.getString("medecinPrenom"),
                resultSet.getString("medecinSpecialite")
        );
    }
}