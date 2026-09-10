package fr.univ.imagemanager.model;

import java.util.HashMap;
import java.util.Map;

public class TransformationInfo {

    private String nom;
    private Map<String, String> parametres;

    public TransformationInfo() {
        this.nom = "";
        this.parametres = new HashMap<>();
    }

    public TransformationInfo(String nom) {
        this.nom = nom;
        this.parametres = new HashMap<>();
    }

    public TransformationInfo(String nom, Map<String, String> parametres) {
        this.nom = nom;
        this.parametres = parametres;
    }

    // getters et Setters

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Map<String, String> getParametres() {
        return parametres;
    }

    public void setParametres(Map<String, String> parametres) {
        this.parametres = parametres;
    }

    @Override
    public String toString() {
        if (parametres.isEmpty()) {
            return nom;
        } else {
            return nom + " " + parametres;
        }
    }
}
