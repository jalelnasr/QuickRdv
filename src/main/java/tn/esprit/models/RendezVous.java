package tn.esprit.models;

import java.util.Date;

public class RendezVous {
    private int id;                   // Identifiant du rendez-vous
    private Date date;                // Date et heure du rendez-vous
    private int patientId;            // ID du patient
    private int medecinId;            // ID du médecin
    private int typeConsultationId;   // Identifiant pour le type de consultation

    // Constructeur par défaut
    public RendezVous() {
    }

    // Constructeur avec tous les attributs (y compris l'ID)
    public RendezVous(int id, Date date, int patientId, int medecinId, int typeConsultationId) {
        this.id = id;
        this.date = date;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.typeConsultationId = typeConsultationId;
    }

    // Constructeur sans l'ID (pour la création d'un nouveau rendez-vous)
    public RendezVous(Date date, int patientId, int medecinId, int typeConsultationId) {
        this.date = date;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.typeConsultationId = typeConsultationId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(int medecinId) {
        this.medecinId = medecinId;
    }

    public int getTypeConsultationId() {
        return typeConsultationId;
    }

    public void setTypeConsultationId(int typeConsultationId) {
        this.typeConsultationId = typeConsultationId;
    }

    @Override
    public String toString() {
        return "RendezVous{" +
                "id=" + id +
                ", date=" + date +
                ", patientId=" + patientId +
                ", medecinId=" + medecinId +
                ", typeConsultationId=" + typeConsultationId +
                "}\n";
    }
}
