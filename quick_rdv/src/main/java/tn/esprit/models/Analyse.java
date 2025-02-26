package tn.esprit.models;

import java.util.Date;

public class Analyse {
    // Champs existants (inchangés)
    private int id;
    private Date date;
    private String type;
    private String instructions;
    private int patientId;
    private int medecinId;
    private String medecinNom;
    private String medecinPrenom;
    private int idRendezVous;

    // Constructeurs (inchangés)
    public Analyse() {}
    public Analyse(int id, Date date, String type, String instructions, int patientId, int medecinId, int idRendezVous) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.instructions = instructions;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.idRendezVous = idRendezVous;
    }
    public Analyse(Date date, String type, String instructions, int patientId, int medecinId, int idRendezVous) {
        this.date = date;
        this.type = type;
        this.instructions = instructions;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.idRendezVous = idRendezVous;
    }

    // Getters et Setters (inchangés)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public int getMedecinId() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }
    public String getMedecinNom() { return medecinNom; }
    public void setMedecinNom(String medecinNom) { this.medecinNom = medecinNom; }
    public String getMedecinPrenom() { return medecinPrenom; }
    public void setMedecinPrenom(String medecinPrenom) { this.medecinPrenom = medecinPrenom; }
    public int getIdRendezVous() { return idRendezVous; }
    public void setIdRendezVous(int idRendezVous) { this.idRendezVous = idRendezVous; }
    @Override
    public String toString() {
        return String.format("ID: %d, Type: %s, Instructions: %s, Patient ID: %d, Médecin: %s %s, Rendez-vous ID: %d",
                id, type, instructions, patientId, medecinNom, medecinPrenom, idRendezVous);
    }
}