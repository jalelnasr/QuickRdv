package tn.esprit.models;

public class Patient extends Utilisateur {
    private String numDossier;
    private String dateNaissance;
    private String adresse;

    public Patient() {
        super();
    }

    public Patient(String nom, String prenom, String email, String motDePasse, String role, String numDossier, String dateNaissance, String adresse) {
        super(nom, prenom, email, motDePasse, role);
        this.numDossier = numDossier;
        this.dateNaissance = dateNaissance;
        this.adresse = adresse;
    }

    public String getNumDossier() {
        return numDossier;
    }

    public void setNumDossier(String numDossier) {
        this.numDossier = numDossier;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    @Override
    public String toString() {
        return "Patient{" + super.toString() +
                "numDossier='" + numDossier + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", adresse='" + adresse + '\'' +
                '}';
    }
}

