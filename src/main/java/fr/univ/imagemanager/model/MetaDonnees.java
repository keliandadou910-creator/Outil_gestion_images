package fr.univ.imagemanager.model;

import java.util.ArrayList;
import java.util.List;

public class MetaDonnees {
    private String cheminImage;
    private List<String> tags;
    private List<TransformationInfo> transformations;

    public MetaDonnees() {
        this.cheminImage = "";
        this.tags = new ArrayList<>();
        this.transformations = new ArrayList<>();
    }

    public MetaDonnees(String cheminImage) {
        this.cheminImage = cheminImage;
        this.tags = new ArrayList<>();
        this.transformations = new ArrayList<>();
    }

    public void ajouterTag(String tag) {
        if (tag == null) {
            return;
        }

        String tagNettoye = tag.trim();

        // On verifie qu'il est pas vide et pas deja dans la liste
        if (tagNettoye.length() > 0 && !tags.contains(tagNettoye)) {
            tags.add(tagNettoye);
        }
    }

    public void supprimerTag(String tag) {
        tags.remove(tag);
    }

    public void ajouterTransformation(TransformationInfo info) {
        transformations.add(info);
    }

    // getters et sztters (necessaires pour la sauvegarde/chargement)

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<TransformationInfo> getTransformations() {
        return transformations;
    }

    public void setTransformations(List<TransformationInfo> transformations) {
        this.transformations = transformations;
    }
}
