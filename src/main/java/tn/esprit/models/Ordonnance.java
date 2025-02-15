package tn.esprit.models;

import java.util.Date;
import java.util.Map;

public class Ordonnance {
    private int id;
    private int medecinId;
    private int patientId;
    private Date datePrescription;
    private String instructions;
    private String statut;


    // Clé : ID du médicament | Valeur : Quantité prescrite
    private Map<Integer, Integer> medicaments;

    public Ordonnance() {}

    public Ordonnance(int medecinId, int patientId, Date datePrescription, String instructions, String statut, Map<Integer, Integer> medicaments) {
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.datePrescription = datePrescription;
        this.instructions = instructions;
        this.statut = statut;
        this.medicaments = medicaments;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMedecinId() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public Date getDatePrescription() { return datePrescription; }
    public void setDatePrescription(Date datePrescription) { this.datePrescription = datePrescription; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }



    public Map<Integer, Integer> getMedicaments() { return medicaments; }
    public void setMedicaments(Map<Integer, Integer> medicaments) { this.medicaments = medicaments; }
}
