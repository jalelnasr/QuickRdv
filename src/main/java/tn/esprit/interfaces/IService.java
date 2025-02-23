package tn.esprit.interfaces;

import tn.esprit.models.Utilisateur;

import java.util.List;

public interface IService<T> {

    void add(T t);

    // List<T> getAll();

    List<Utilisateur> getAll();

    void update(T t);

    //void delete(int id);

}
