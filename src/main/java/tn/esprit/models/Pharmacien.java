package tn.esprit.models;

public class Pharmacien extends Utilisateur {


    public Pharmacien(int id, String nom, String prenom, String email, String motDePasse, String role) {
        super(id,nom, prenom, email, motDePasse, role);
    }



    public Pharmacien( String nom, String prenom, String email, String motDePasse, String role) {
        super(nom, prenom, email, motDePasse, role);
    }



    @Override
    public String toString() {
        return "Pharmacien{" + super.toString() + '}' +

                '}';
    }
}