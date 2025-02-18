
package tn.esprit.models;

import java.util.Date;

public class Avis {
    private int id;
    private int patient_id;
    private int medecin_id;
    private String commentaire;
    private int note;
    private Date date_avis;

    public Avis() {
    }

    public Avis(int id, int patient_id, int medecin_id, String commentaire, int note, Date date_avis) {
        this.id = id;
        this.patient_id = patient_id;
        this.medecin_id = medecin_id;
        this.commentaire = commentaire;
        this.note = note;
        this.date_avis = date_avis;
    }

    public Avis(int patient_id, int medecin_id, String commentaire, int note, Date date_avis) {
        this.patient_id = patient_id;
        this.medecin_id = medecin_id;
        this.commentaire = commentaire;
        this.note = note;
        this.date_avis = date_avis;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatient_id() {
        return this.patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getMedecin_id() {
        return this.medecin_id;
    }

    public void setMedecin_id(int medecin_id) {
        this.medecin_id = medecin_id;
    }

    public String getCommentaire() {
        return this.commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNote() {
        return this.note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    public Date getDate_avis() {
        return this.date_avis;
    }

    public void setDate_avis(Date date_avis) {
        this.date_avis = date_avis;
    }

    public String toString() {
        int var10000 = this.id;
        return "Avis{id=" + var10000 + ", patient_id=" + this.patient_id + ", medecin_id=" + this.medecin_id + ", commentaire='" + this.commentaire + "', note=" + this.note + ", date_avis=" + String.valueOf(this.date_avis) + "}\n";
    }
}
