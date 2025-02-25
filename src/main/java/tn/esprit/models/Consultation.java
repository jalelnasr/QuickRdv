package tn.esprit.models;

import java.sql.Timestamp;

public class Consultation {
    private int id;
    private Timestamp dateHeure;
    private String typeConsultation;
    private int idPatient;
    private int idMedecin;

    // Constructors
    public Consultation() {}

    public Consultation(int id, Timestamp dateHeure, String typeConsultation, int idPatient, int idMedecin) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.typeConsultation = typeConsultation;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Timestamp getDateHeure() { return dateHeure; }
    public void setDateHeure(Timestamp dateHeure) { this.dateHeure = dateHeure; }

    public String getTypeConsultation() { return typeConsultation; }
    public void setTypeConsultation(String typeConsultation) { this.typeConsultation = typeConsultation; }

    public int getIdPatient() { return idPatient; }
    public void setIdPatient(int idPatient) { this.idPatient = idPatient; }

    public int getIdMedecin() { return idMedecin; }
    public void setIdMedecin(int idMedecin) { this.idMedecin = idMedecin; }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", dateHeure=" + dateHeure +
                ", typeConsultation='" + typeConsultation + '\'' +
                ", idPatient=" + idPatient +
                ", idMedecin=" + idMedecin +
                '}';
    }
}