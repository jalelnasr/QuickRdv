package tn.esprit.models;

public class Medecin extends Utilisateur {
    private String specialite;
    private String disponibilite;
    private String num_rdv_max;
    private String numeroRPPS;

    public Medecin(int id, String specialite, String disponibilite, String numRdvMax, String numeroRPPS) {
        super();
    }

    public Medecin( String nom, String prenom, String email, String motDePasse, String role, String specialite, String disponibilite, String num_rdv_max, String numeroRPPS) {
        super(nom, prenom, email, motDePasse, role);
        this.specialite = specialite;
        this.disponibilite = disponibilite;
        this.num_rdv_max = num_rdv_max;
        this.numeroRPPS = numeroRPPS;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(String disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getNum_rdv_max() {
        return num_rdv_max;
    }

    public void setNum_rdv_max(String num_rdv_max) {
        this.num_rdv_max = num_rdv_max;
    }

    public String getNumeroRPPS() {
        return numeroRPPS;
    }

    public void setNumeroRPPS(String numeroRPPS) {
        this.numeroRPPS = numeroRPPS;
    }

    @Override
    public String toString() {
        return "Medecin{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", prenom='" + getPrenom() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", motDePasse='" + getMotDePasse() + '\'' +
                ", role='" + getRole() + '\'' +
                ", specialite='" + specialite + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", num_rdv_max=" + num_rdv_max +
                ", numeroRPPS='" + numeroRPPS + '\'' +
                '}';
    }
}