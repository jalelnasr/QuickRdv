package tn.esprit.test;

import tn.esprit.models.*;
import tn.esprit.services.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceUtilisateur su = new ServiceUtilisateur();
        su.add(new Administrateur("mohamed","nasr","mohamed.nasr@esprit.tn","1111111","Administrateur"));
        su.add(new Pharmacien("safe","nasr","safe.nasr@esprit.tn","222222","Pharmacien"));
        su.add(new Patient("hhhh","hhhhh","hhhhhhhhhhh@esprit.tn","77777","patient","2","1990","dddddd"));
        su.add(new Medecin("fatma","kthirinasr","fatmakthiri.nasr@esprit.tn","444444","Medecin","cx","no","1","1990-01-01"));




        /*su.delete(87);*/




       // su.update(new Pharmacien(126,"Youssef","aaaaa","ann.nasr@esprit.tn","123","Pharmacien"));
    }
}