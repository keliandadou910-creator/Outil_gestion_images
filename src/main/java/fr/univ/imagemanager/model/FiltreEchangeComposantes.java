package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FiltreEchangeComposantes implements FiltreImage {

    @Override
    public WritableImage appliquer(WritableImage img) {
        int larg = (int) img.getWidth();
        int haut = (int) img.getHeight();

        WritableImage resultat = new WritableImage(larg, haut);
        PixelReader lecteur = img.getPixelReader();
        PixelWriter ecrivain = resultat.getPixelWriter();

        for (int y = 0; y < haut; y++) {
            for (int x = 0; x < larg; x++) {
                Color couleur = lecteur.getColor(x, y);

                double ancienRouge = couleur.getRed();
                double ancienVert = couleur.getGreen();
                double ancienBleu = couleur.getBlue();
                double opacite = couleur.getOpacity();

                double nouveauRouge = ancienVert;
                double nouveauVert = ancienBleu;
                double nouveauBleu = ancienRouge;
                Color nouvCouleur = new Color(nouveauRouge, nouveauVert, nouveauBleu, opacite);

                ecrivain.setColor(x, y, nouvCouleur);
            }
        }
        return resultat;
    }

    @Override
    public String getNom() {
        return "Échange Composantes (RGB→GBR)";
    }
}
