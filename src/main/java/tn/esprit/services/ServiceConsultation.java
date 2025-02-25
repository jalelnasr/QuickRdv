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

    public List<Consultation> getConsultationsByPatientId(int idPatient) {
        List<Consultation> consultations = new ArrayList<>();
        String query = "SELECT * FROM rendez_vous WHERE id_patient = ?"; // Updated table name

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idPatient);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Consultation consultation = new Consultation(
                        resultSet.getInt("id"),
                        resultSet.getTimestamp("dateHeure"),
                        resultSet.getString("typeConsultation"),
                        resultSet.getInt("id_patient"),
                        resultSet.getInt("id_medecin")
                );
                consultations.add(consultation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consultations;
    }}