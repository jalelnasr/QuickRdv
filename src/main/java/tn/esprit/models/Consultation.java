package tn.esprit.models;

import java.sql.Timestamp;

public class Consultation {
    private int id;
    private Timestamp dateHeure;
    private String typeConsultation;
    private int idPatient;
    private int idMedecin;
    private String medecinNom; // New field
    private String medecinPrenom; // New field
    private String medecinSpecialite; // New field

    // Constructors
    public Consultation() {}

    public Consultation(int id, Timestamp dateHeure, String typeConsultation, int idPatient, int idMedecin, String medecinNom, String medecinPrenom, String medecinSpecialite) {
        this.id = id;
        this.dateHeure = dateHeure;
        this.typeConsultation = typeConsultation;
        this.idPatient = idPatient;
        this.idMedecin = idMedecin;
        this.medecinNom = medecinNom;
        this.medecinPrenom = medecinPrenom;
        this.medecinSpecialite = medecinSpecialite;
    }

    // Getters and Setters
    public int getId() {
        return id; }
    public void setId(int id) {
        this.id = id; }

    public Timestamp getDateHeure() {
        return dateHeure; }
    public void setDateHeure(Timestamp dateHeure) {
        this.dateHeure = dateHeure; }

    public String getTypeConsultation() {
        return typeConsultation; }
    public void setTypeConsultation(String typeConsultation) {
        this.typeConsultation = typeConsultation; }

    public int getIdPatient() {
        return idPatient; }
    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient; }

    public int getIdMedecin() {
        return idMedecin; }
    public void setIdMedecin(int idMedecin) {
        this.idMedecin = idMedecin; }

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
        return "Consultation{" +
                "id=" + id +
                ", dateHeure=" + dateHeure +
                ", typeConsultation='" + typeConsultation + '\'' +
                ", idPatient=" + idPatient +
                ", idMedecin=" + idMedecin +
                ", medecinNom='" + medecinNom + '\'' +
                ", medecinPrenom='" + medecinPrenom + '\'' +
                ", medecinSpecialite='" + medecinSpecialite + '\'' +
                '}';
    }
}