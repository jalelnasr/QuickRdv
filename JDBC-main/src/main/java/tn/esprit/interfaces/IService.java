package tn.esprit.interfaces;

import java.util.List;
import java.util.Map;

public interface IService<T> {

    void add(T t); // Ajouter une entité

    List<T> getAll(); // Récupérer toutes les entités

    void update(T t); // Mettre à jour une entité

    void delete(T t); // Supprimer une entité


}

