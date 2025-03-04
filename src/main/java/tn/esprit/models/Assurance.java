package tn.esprit.models;

import java.time.LocalDate;

public class Assurance {
    private int idAssurance;
    private String nom;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int idPatientAs; // Add this field

    // Default constructor
    public Assurance() {
    }

    // Constructor without idAssurance (for creating new assurances)
    public Assurance(String nom, String type, LocalDate dateDebut, LocalDate dateFin, int idPatientAs) {
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idPatientAs = idPatientAs; // Initialize the field
    }

    // Constructor with idAssurance (for retrieving existing assurances)
    public Assurance(int idAssurance, String nom, String type, LocalDate dateDebut, LocalDate dateFin, int idPatientAs) {
        this.idAssurance = idAssurance;
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idPatientAs = idPatientAs; // Initialize the field
    }

    // Getters and setters for all fields
    public int getIdAssurance() {
        return idAssurance;
    }

    public void setIdAssurance(int idAssurance) {
        this.idAssurance = idAssurance;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getIdPatientAs() {
        return idPatientAs;
    }

    public void setIdPatientAs(int idPatientAs) {
        this.idPatientAs = idPatientAs;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "idAssurance=" + idAssurance +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", idPatientAs=" + idPatientAs +
                '}';
    }
}