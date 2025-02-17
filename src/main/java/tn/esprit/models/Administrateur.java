package tn.esprit.models;

public class Administrateur extends Utilisateur {
    private int id ;
    public Administrateur(String nom, String prenom, String email, String motDePasse, String role) {
        super(nom, prenom, email, motDePasse, role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Administrateur{" + super.toString() +
                "id=" + id +
                '}';
    }
}