package tn.esprit.test;

import tn.esprit.models.*;
import tn.esprit.services.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceUtilisateur su = new ServiceUtilisateur();
        su.add(new Administrateur("jalel","nasr","ajaalels.nasr@esprit.tn","123","Administrateur"));
        su.add(new Pharmacien("jalel","nasr","a.nasr@esprit.tn","123","Pharmacien"));
        su.add(new Patient("jalel","nasr","bvcvbgvcvncvna.nasr@esprit.tn","123","Pharmacien","1","1990-01-01","dfg"));
        su.add(new Medecin("jalel","nasr","bvcsda.nasr@esprit.tn","123","Medecin","cx","no","1","1990-01-01"));




        /*su.delete(87);*/




        su.update(new Pharmacien(126,"Youssef","aaaaa","ann.nasr@esprit.tn","123","Pharmacien"));
    }
}