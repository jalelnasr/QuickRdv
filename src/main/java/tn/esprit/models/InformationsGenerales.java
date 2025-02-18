package tn.esprit.models;

public class InformationsGenerales {
    private int id;
    private int idPatient;
    private String historiqueOrdonnance;
    private String historiqueTeleconsultation;
    private String historiqueConsultationPresentiel;
    private float taille;
    private float poids;
    private boolean maladies;

    public InformationsGenerales (int idPatient, String historiqueOrdonnance, String historiqueTeleconsultation, String historiqueConsultationPresentiel, double v, int poids, boolean maladies) {
    }

    public InformationsGenerales(int id, int idPatient, String historiqueOrdonnance, String historiqueTeleconsultation, String historiqueConsultationPresentiel, float taille, float poids, boolean maladies) {
        this.id = id;
        this.idPatient = idPatient;
        this.historiqueOrdonnance = historiqueOrdonnance;
        this.historiqueTeleconsultation = historiqueTeleconsultation;
        this.historiqueConsultationPresentiel = historiqueConsultationPresentiel;
        this.taille = taille;
        this.poids = poids;
        this.maladies = maladies;
    }

    public InformationsGenerales(int idPatient, String historiqueOrdonnance, String historiqueTeleconsultation, String historiqueConsultationPresentiel, float taille, float poids, boolean maladies) {
        this.idPatient = idPatient;
        this.historiqueOrdonnance = historiqueOrdonnance;
        this.historiqueTeleconsultation = historiqueTeleconsultation;
        this.historiqueConsultationPresentiel = historiqueConsultationPresentiel;
        this.taille = taille;
        this.poids = poids;
        this.maladies = maladies;
    }

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

    public String getHistoriqueOrdonnance() {
        return historiqueOrdonnance;
    }

    public void setHistoriqueOrdonnance(String historiqueOrdonnance) {
        this.historiqueOrdonnance = historiqueOrdonnance;
    }

    public String getHistoriqueTeleconsultation() {
        return historiqueTeleconsultation;
    }

    public void setHistoriqueTeleconsultation(String historiqueTeleconsultation) {
        this.historiqueTeleconsultation = historiqueTeleconsultation;
    }

    public String getHistoriqueConsultationPresentiel() {
        return historiqueConsultationPresentiel;
    }

    public void setHistoriqueConsultationPresentiel(String historiqueConsultationPresentiel) {
        this.historiqueConsultationPresentiel = historiqueConsultationPresentiel;
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

    @Override
    public String toString() {
        return "DossierMedicale{" +
                "id=" + id +
                ", idPatient=" + idPatient +
                ", historiqueOrdonnance='" + historiqueOrdonnance + '\'' +
                ", historiqueTeleconsultation='" + historiqueTeleconsultation + '\'' +
                ", historiqueConsultationPresentiel='" + historiqueConsultationPresentiel + '\'' +
                ", taille=" + taille +
                ", poids=" + poids +
                ", maladies=" + maladies +
                "}\n";
    }
}
