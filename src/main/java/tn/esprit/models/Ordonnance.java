package tn.esprit.models;

import java.sql.Timestamp;

public class Ordonnance {
    private int id;
    private int medecinId;
    private int patientId;
    private String medicaments;
    private Timestamp datePrescription;
    private String instructions;
    private String statut;
    private String medecinNom;
    private String medecinPrenom;
    private String medecinSpecialite;

    // Constructors
    public Ordonnance(int id, int medecinId, int patientId, String medicaments, Timestamp datePrescription, String instructions, String statut) {}

    public Ordonnance(int id, int medecinId, int patientId, String medicaments, Timestamp datePrescription, String instructions, String statut, String medecinNom, String medecinPrenom, String medecinSpecialite) {
        this.id = id;
        this.medecinId = medecinId;
        this.patientId = patientId;
        this.medicaments = medicaments;
        this.datePrescription = datePrescription;
        this.instructions = instructions;
        this.statut = statut;
        this.medecinNom = medecinNom;
        this.medecinPrenom = medecinPrenom;
        this.medecinSpecialite = medecinSpecialite;
    }

    // Getters and Setters
    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }

    public int getMedecinId() {
        return medecinId; }
    public void setMedecinId(int medecinId) {
        this.medecinId = medecinId; }

    public int getPatientId() {
        return patientId; }
    public void setPatientId(int patientId) {
        this.patientId = patientId; }

    public String getMedicaments() {
        return medicaments; }
    public void setMedicaments(String medicaments) {
        this.medicaments = medicaments; }

    public Timestamp getDatePrescription() {
        return datePrescription; }
    public void setDatePrescription(Timestamp datePrescription) {
        this.datePrescription = datePrescription; }

    public String getInstructions() {
        return instructions; }
    public void setInstructions(String instructions) {
        this.instructions = instructions; }

    public String getStatut() {
        return statut; }
    public void setStatut(String statut) {
        this.statut = statut; }

    public String getMedecinNom() {
        return medecinNom; }
    public void setMedecinNom(String medecinNom) {
        this.medecinNom = medecinNom; }

    public String getMedecinPrenom() {
        return medecinPrenom; }
    public void setMedecinPrenom(String medecinPrenom) {
        this.medecinPrenom = medecinPrenom; }

    public String getMedecinSpecialite() {
        return medecinSpecialite; }
    public void setMedecinSpecialite(String medecinSpecialite) {
        this.medecinSpecialite = medecinSpecialite; }

    @Override
    public String toString() {
        return "Ordonnance{" +
                "id=" + id +
                ", medecinId=" + medecinId +
                ", patientId=" + patientId +
                ", medicaments='" + medicaments + '\'' +
                ", datePrescription=" + datePrescription +
                ", instructions='" + instructions + '\'' +
                ", statut='" + statut + '\'' +
                ", medecinNom='" + medecinNom + '\'' +
                ", medecinPrenom='" + medecinPrenom + '\'' +
                ", medecinSpecialite='" + medecinSpecialite + '\'' +
                '}';
    }
}