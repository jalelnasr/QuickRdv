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
    private Map<String, Integer> medicaments;  // Clé: Nom du médicament | Valeur: Quantité

    public Ordonnance() {}

    public Ordonnance(int medecinId, int patientId, Date datePrescription, String instructions, String statut, Map<String, Integer> medicaments) {
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.datePrescription = datePrescription;
        this.instructions = instructions;
        this.statut = statut;
        this.medicaments = medicaments;
    }

    public Ordonnance(int id, int medecinId, int patientId, Date datePrescription, String instructions, String statut, Map<String, Integer> medicaments) {
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.datePrescription = datePrescription;
        this.instructions = instructions;
        this.statut = statut;
        this.medicaments = medicaments;
        this.id = id;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }



    public int getMedecinId() { return medecinId; }
    public int getPatientId() { return patientId; }
    public Date getDatePrescription() { return datePrescription; }
    public String getInstructions() { return instructions; }
    public String getStatut() { return statut; }
    public Map<String, Integer> getMedicaments() { return medicaments; }
    public void setMedicaments(Map<String, Integer> medicaments) { this.medicaments = medicaments; }

    public void setMedecinId(int medecinId) {
        this.medecinId = medecinId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setDatePrescription(Date datePrescription) {
        this.datePrescription = datePrescription;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
