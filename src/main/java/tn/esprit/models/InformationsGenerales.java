package tn.esprit.models;

public class InformationsGenerales {
    private int id;
    private int idPatient;
    private float taille;
    private float poids;
    private boolean maladies;
    private String antecedentsCardiovasculairesFamiliaux;
    private String asthmatique;
    private String suiviDentaireRegulier;
    private String antecedentsChirurgicaux;
    private String allergies;
    private String profession;
    private String niveauDeStress;
    private String qualiteDeSommeil;
    private String activitePhysique;
    private String situationFamiliale;

    public InformationsGenerales() {
    }

    public InformationsGenerales(int id, int idPatient, float taille, float poids, boolean maladies,
                                 String antecedentsCardiovasculairesFamiliaux, String asthmatique,
                                 String suiviDentaireRegulier, String antecedentsChirurgicaux, String allergies,
                                 String profession, String niveauDeStress, String qualiteDeSommeil,
                                 String activitePhysique, String situationFamiliale) {
        this.id = id;
        this.idPatient = idPatient;
        this.taille = taille;
        this.poids = poids;
        this.maladies = maladies;
        this.antecedentsCardiovasculairesFamiliaux = antecedentsCardiovasculairesFamiliaux;
        this.asthmatique = asthmatique;
        this.suiviDentaireRegulier = suiviDentaireRegulier;
        this.antecedentsChirurgicaux = antecedentsChirurgicaux;
        this.allergies = allergies;
        this.profession = profession;
        this.niveauDeStress = niveauDeStress;
        this.qualiteDeSommeil = qualiteDeSommeil;
        this.activitePhysique = activitePhysique;
        this.situationFamiliale = situationFamiliale;
    }

    @Override
    public String toString() {
        return "InformationsGenerales{" +
                "id=" + id +
                ", idPatient=" + idPatient +
                ", taille=" + taille +
                ", poids=" + poids +
                ", maladies=" + (maladies ? "Oui" : "Non") +
                ", antecedentsCardiovasculairesFamiliaux='" + antecedentsCardiovasculairesFamiliaux + '\'' +
                ", asthmatique='" + asthmatique + '\'' +
                ", suiviDentaireRegulier='" + suiviDentaireRegulier + '\'' +
                ", antecedentsChirurgicaux='" + antecedentsChirurgicaux + '\'' +
                ", allergies='" + allergies + '\'' +
                ", profession='" + profession + '\'' +
                ", niveauDeStress='" + niveauDeStress + '\'' +
                ", qualiteDeSommeil='" + qualiteDeSommeil + '\'' +
                ", activitePhysique='" + activitePhysique + '\'' +
                ", situationFamiliale='" + situationFamiliale + '\'' +
                '}';
    }

    // Getters and setters for all fields
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public float getTaille() {
        return taille;
    }

    public void setTaille(float taille) {
        this.taille = taille;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public boolean hasMaladies() {
        return maladies;
    }

    public void setMaladies(boolean maladies) {
        this.maladies = maladies;
    }

    public String getAntecedentsCardiovasculairesFamiliaux() {
        return antecedentsCardiovasculairesFamiliaux;
    }

    public void setAntecedentsCardiovasculairesFamiliaux(String antecedentsCardiovasculairesFamiliaux) {
        this.antecedentsCardiovasculairesFamiliaux = antecedentsCardiovasculairesFamiliaux;
    }

    public String getAsthmatique() {
        return asthmatique;
    }

    public void setAsthmatique(String asthmatique) {
        this.asthmatique = asthmatique;
    }

    public String getSuiviDentaireRegulier() {
        return suiviDentaireRegulier;
    }

    public void setSuiviDentaireRegulier(String suiviDentaireRegulier) {
        this.suiviDentaireRegulier = suiviDentaireRegulier;
    }

    public String getAntecedentsChirurgicaux() {
        return antecedentsChirurgicaux;
    }

    public void setAntecedentsChirurgicaux(String antecedentsChirurgicaux) {
        this.antecedentsChirurgicaux = antecedentsChirurgicaux;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getNiveauDeStress() {
        return niveauDeStress;
    }

    public void setNiveauDeStress(String niveauDeStress) {
        this.niveauDeStress = niveauDeStress;
    }

    public String getQualiteDeSommeil() {
        return qualiteDeSommeil;
    }

    public void setQualiteDeSommeil(String qualiteDeSommeil) {
        this.qualiteDeSommeil = qualiteDeSommeil;
    }

    public String getActivitePhysique() {
        return activitePhysique;
    }

    public void setActivitePhysique(String activitePhysique) {
        this.activitePhysique = activitePhysique;
    }

    public String getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(String situationFamiliale) {
        this.situationFamiliale = situationFamiliale;
    }
}
