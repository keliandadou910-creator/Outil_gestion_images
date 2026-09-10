package fr.univ.imagemanager.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestionMetaDonnees {

    private File fichierSauvegarde;

    private List<MetaDonnees> toutesLesMetaDonnes;

    public GestionMetaDonnees(String cheminFichier) {
        this.fichierSauvegarde = new File(cheminFichier);
        this.toutesLesMetaDonnes = new ArrayList<>();

        charger();
    }

    public void charger() {
        if (!fichierSauvegarde.exists()) {
            return;
        }
        toutesLesMetaDonnes.clear();

        try {
            BufferedReader lecteur = new BufferedReader(new FileReader(fichierSauvegarde));
            String ligne;

            String cheminTemp = null;
            List<String> tagsTemp = new ArrayList<>();
            List<TransformationInfo> transfosTemp = new ArrayList<>();

            while ((ligne = lecteur.readLine()) != null) {
                // si on tombe sur le séparateur, on enregistre le bloc precédent
                if (ligne.equals("---")) {
                    if (cheminTemp != null) {
                        enregistrerBloc(cheminTemp, tagsTemp, transfosTemp);
                    }
                    // On réinitialise pour le prochaine bloc
                    cheminTemp = null;
                    tagsTemp = new ArrayList<>();
                    transfosTemp = new ArrayList<>();

                } else if (ligne.startsWith("CHEMIN:")) {
                    cheminTemp = ligne.substring(7);

                } else if (ligne.startsWith("TAGS:")) {
                    // On extrait les tags qui seront separés par des virgules)
                    String contenuTags = ligne.substring(5);
                    tagsTemp = decouperParVirgule(contenuTags);

                } else if (ligne.startsWith("TRANSFOS:")) {
                    // On extrait les transformations (tjs separées par des virgules)
                    String contenuTransfos = ligne.substring(9);
                    List<String> nomsTransfos = decouperParVirgule(contenuTransfos);
                    for (int i = 0; i < nomsTransfos.size(); i++) {
                        transfosTemp.add(new TransformationInfo(nomsTransfos.get(i)));
                    }
                }
            }

            // On oublie pas le dernier bloc :)
            if (cheminTemp != null) {
                enregistrerBloc(cheminTemp, tagsTemp, transfosTemp);
            }

            lecteur.close();

        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des métadonnées : " + e.getMessage());
            toutesLesMetaDonnes = new ArrayList<>();
        }
    }

    private void enregistrerBloc(String chemin, List<String> tags, List<TransformationInfo> transfos) {
        MetaDonnees md = new MetaDonnees(chemin);
        md.setTags(tags);
        md.setTransformations(transfos);
        toutesLesMetaDonnes.add(md);
    }

    private List<String> decouperParVirgule(String texte) {
        List<String> resultats = new ArrayList<>();

        if (texte == null || texte.trim().length() == 0) {
            return resultats;
        }

        // On decoupe juste par les virgule
        String[] morceaux = texte.split(",");
        for (int i = 0; i < morceaux.length; i++) {
            String morceau = morceaux[i].trim();
            if (morceau.length() > 0) {
                resultats.add(morceau);
            }
        }

        return resultats;
    }

    public void sauvegarder() {
        try {
            if (fichierSauvegarde.getParentFile() != null) {
                fichierSauvegarde.getParentFile().mkdirs();
            }

            BufferedWriter ecrivain = new BufferedWriter(new FileWriter(fichierSauvegarde));

            for (int i = 0; i < toutesLesMetaDonnes.size(); i++) {
                MetaDonnees md = toutesLesMetaDonnes.get(i);

                ecrivain.write("CHEMIN:" + md.getCheminImage());
                ecrivain.newLine();

                String ligneTags = construireLigneVirgule(md.getTags());
                ecrivain.write("TAGS:" + ligneTags);
                ecrivain.newLine();

                List<String> nomsTransfos = new ArrayList<>();
                for (int j = 0; j < md.getTransformations().size(); j++) {
                    nomsTransfos.add(md.getTransformations().get(j).getNom());

                }
                String ligneTransfos = construireLigneVirgule(nomsTransfos);
                ecrivain.write("TRANSFOS:" + ligneTransfos);
                ecrivain.newLine();
                ecrivain.write("---");
                ecrivain.newLine();
            }

            ecrivain.close();

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des métadonnées : " + e.getMessage());
        }
    }

    private String construireLigneVirgule(List<String> elements) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(elements.get(i));
        }

        return sb.toString();
    }

    public MetaDonnees getMetaDonnees(String cheminImage) {
        for (int i = 0; i < toutesLesMetaDonnes.size(); i++) {
            MetaDonnees md = toutesLesMetaDonnes.get(i);
            if (md.getCheminImage().equals(cheminImage)) {
                return md;
            }
        }

        MetaDonnees nouvelle = new MetaDonnees(cheminImage);
        toutesLesMetaDonnes.add(nouvelle);
        return nouvelle;
    }

    // Cherche toutes les images qui ont un tag donnée.
    public List<MetaDonnees> rechercherParTag(String tag) {
        List<MetaDonnees> resultats = new ArrayList<>();
        for (int i = 0; i < toutesLesMetaDonnes.size(); i++) {
            MetaDonnees md = toutesLesMetaDonnes.get(i);
            if (md.getTags().contains(tag)) {
                resultats.add(md);
            }

        }
        return resultats;
    }

    public List<MetaDonnees> getToutesLesMetaDonnes() {
        return toutesLesMetaDonnes;
    }
}
