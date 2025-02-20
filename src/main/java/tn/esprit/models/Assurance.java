package tn.esprit.models;

import java.time.LocalDate;

public class Assurance {
    private int idAssurance;
    private String nom;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float montantCouvert;

    public Assurance() {
    }

    // Added constructor without idAssurance
    public Assurance(String nom, String type, LocalDate dateDebut, LocalDate dateFin, float montantCouvert) {
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantCouvert = montantCouvert;
    }

    public Assurance(int idAssurance, String nom, String type, LocalDate dateDebut, LocalDate dateFin, float montantCouvert) {
        this.idAssurance = idAssurance;
        this.nom = nom;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montantCouvert = montantCouvert;
    }

    @Override
    public String toString() {
        return "Assurance{" +
                "idAssurance=" + idAssurance +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", montantCouvert=" + montantCouvert +
                '}';
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

    public float getMontantCouvert() {
        return montantCouvert;
    }

    public void setMontantCouvert(float montantCouvert) {
        this.montantCouvert = montantCouvert;
    }
}
