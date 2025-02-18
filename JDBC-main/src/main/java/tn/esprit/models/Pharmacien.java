package tn.esprit.models;

public class Pharmacien extends Utilisateur {
    private Hopital hopital;

    public Pharmacien() {
    }

    public Pharmacien(int id, String nom, String prenom, String email, String motDePasse, Hopital hopital) {
        super(id, nom, prenom, email, motDePasse);
        this.hopital = hopital;
    }

    public Hopital getHopital() {
        return hopital;
    }

    public void setHopital(Hopital hopital) {
        this.hopital = hopital;
    }

    @Override
    public String toString() {
        return "Pharmacien{" +
                "utilisateur_id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", hopital_id=" + hopital.getId() +
                "}";
    }
}
