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

    public List<Ordonnance> getOrdonnancesByPatientId(int patientId) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String query = "SELECT * FROM ordonnance WHERE patient_id = ?"; // Updated table name

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Ordonnance ordonnance = new Ordonnance(
                        resultSet.getInt("id"),
                        resultSet.getInt("medecin_id"),
                        resultSet.getInt("patient_id"),
                        resultSet.getString("medicaments"),
                        resultSet.getTimestamp("date_prescription"), // Updated column name
                        resultSet.getString("instructions"),
                        resultSet.getString("statut")
                );
                ordonnances.add(ordonnance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ordonnances;
    }
}