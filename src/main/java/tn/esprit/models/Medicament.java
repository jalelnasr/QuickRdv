package tn.esprit.models;

public class Medicament {
    private int id;
    private String nom;
    private int stock;
    private int pharmacienId; // Clé étrangère vers la table Pharmacien

    // Constructeurs
    public Medicament() {
    }

    public Medicament(String nom, int stock, int pharmacienId) {
        this.nom = nom;
        this.stock = stock;
        this.pharmacienId = pharmacienId;
    }

    public Medicament(int id, String nom, int stock, int pharmacienId) {
        this.id = id;
        this.nom = nom;
        this.stock = stock;
        this.pharmacienId = pharmacienId;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPharmacienId() {
        return pharmacienId;
    }

    public void setPharmacienId(int pharmacienId) {
        this.pharmacienId = pharmacienId;
    }

    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", stock=" + stock +
                ", pharmacienId=" + pharmacienId +
                '}';
    }
}
