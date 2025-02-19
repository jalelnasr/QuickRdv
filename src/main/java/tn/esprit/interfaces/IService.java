package tn.esprit.interfaces;

import java.util.List;
import java.util.Map;

public interface IService<T> {

    void add(T t); // Ajouter une entité

    List<T> getAll(); // Récupérer toutes les entités

    void update(T t); // Mettre à jour une entité

    void delete(T t); // Supprimer une entité

    void validerOrdonnance(int id); // Valider une ordonnance et mettre à jour le stock

    void insertOrdonnanceMedicaments(int ordonnanceId, Map<Integer, Integer> medicaments); // Associer médicaments à une ordonnance
}

