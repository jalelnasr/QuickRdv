package tn.esprit.models;

import java.util.Date;

public class Reclamation {
    private int id;
    private int utilisateur_id;
    private int rendez_vous_id;
    private String sujet;
    private String description;
    private Date date_reclamation;

    public Reclamation() {
    }

    public Reclamation(int id, int utilisateur_id, int rendez_vous_id, String sujet, String description, Date date_reclamation) {
        this.id = id;
        this.utilisateur_id = utilisateur_id;
        this.rendez_vous_id = rendez_vous_id;
        this.sujet = sujet;
        this.description = description;
        this.date_reclamation = date_reclamation;
    }

    public Reclamation(int utilisateur_id, int rendez_vous_id, String sujet, String description, Date date_reclamation) {
        this.utilisateur_id = utilisateur_id;
        this.rendez_vous_id = rendez_vous_id;
        this.sujet = sujet;
        this.description = description;
        this.date_reclamation = date_reclamation;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateur_id() {
        return this.utilisateur_id;
    }

    public void setUtilisateur_id(int utilisateur_id) {
        this.utilisateur_id = utilisateur_id;
    }

    public int getRendez_vous_id() {
        return this.rendez_vous_id;
    }

    public void setRendez_vous_id(int rendez_vous_id) {
        this.rendez_vous_id = rendez_vous_id;
    }

    public String getSujet() {
        return this.sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_reclamation() {
        return this.date_reclamation;
    }

    public void setDate_reclamation(Date date_reclamation) {
        this.date_reclamation = date_reclamation;
    }

    @Override
    public String toString() {
        return "Reclamation{id=" + this.id +
                ", utilisateur_id=" + this.utilisateur_id +
                ", rendez_vous_id=" + this.rendez_vous_id +
                ", sujet='" + this.sujet + '\'' +
                ", description='" + this.description + '\'' +
                ", date_reclamation=" + this.date_reclamation + "}\n";
    }
}
