package tn.esprit.interfaces;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;


public interface IMService<T> {

    void add(T t); // Ajouter une entité

    List<T> getAll(); // Récupérer toutes les entités

    void update(T t); // Mettre à jour une entité

    void delete(T t); // Supprimer une entité

    // Convertir une Map en String
    default String mapToString(Map<String, Integer> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue()) // Transformer chaque entrée en "clé:valeur"
                .collect(Collectors.joining(",")); // Joindre avec des virgules
    }


    // Convertir un String en Map
    default Map<String, Integer> stringToMap(String str) {
        return Arrays.stream(str.split(",")) // Diviser la chaîne en paires "clé:valeur"
                .map(entry -> entry.split(":")) // Diviser chaque paire
                .filter(keyValue -> keyValue.length == 2) // Éviter les erreurs
                .collect(Collectors.toMap(
                        keyValue -> keyValue[0],
                        keyValue -> Integer.parseInt(keyValue[1])
                ));
    }


    void validerOrdonnance(T t); // Valider une ordonnance et mettre à jour le stock

    void insertOrdonnanceMedicaments(int ordonnanceId, Map<String, Integer> medicaments);
    void  updateStockMedicament(int medicamentId, int quantite) ;


}