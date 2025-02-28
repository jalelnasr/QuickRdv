package tn.esprit.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RendezVous {
    private int id;
    private LocalDate date;  // Use LocalDateTime instead of Date
    private int patientId;
    private int medecinId;
    private int typeConsultationId;
    private LocalDateTime startTime;  // New field for start time
    private LocalDateTime endTime;    // New field for end time
    private String medecinNom;
    private String medecinPrenom;
    private String patientNom;
    private String patientPrenom;

    // Constructors
    public RendezVous() {}

    public RendezVous(int id, LocalDate date, int patientId, int medecinId, int typeConsultationId,
                      LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.date = date;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.typeConsultationId = typeConsultationId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public RendezVous(LocalDate date, int patientId, int medecinId, int typeConsultationId,
                      LocalDateTime startTime, LocalDateTime endTime) {
        this.date = date;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.typeConsultationId = typeConsultationId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getMedecinId() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }

    public int getTypeConsultationId() { return typeConsultationId; }
    public void setTypeConsultationId(int typeConsultationId) { this.typeConsultationId = typeConsultationId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getMedecinNom() { return medecinNom; }
    public void setMedecinNom(String medecinNom) { this.medecinNom = medecinNom; }

    public String getMedecinPrenom() { return medecinPrenom; }
    public void setMedecinPrenom(String medecinPrenom) { this.medecinPrenom = medecinPrenom; }

    public String getPatientNom() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public String getPatientPrenom() { return patientPrenom; }
    public void setPatientPrenom(String patientPrenom) { this.patientPrenom = patientPrenom; }

    // Method for translating the consultation type
    private String getTypeConsultationText() {
        switch (typeConsultationId) {
            case 1: return "Consultation";
            case 2: return "Téléconsultation";
            default: return "Inconnu"; // Handle unexpected cases
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Date: %s, Patient: %s %s, Médecin: %s %s, Type: %s, Start: %s, End: %s",
                id, date, patientNom, patientPrenom, medecinNom, medecinPrenom, getTypeConsultationText(), startTime, endTime);
    }
}
