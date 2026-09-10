package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FiltreNoirEtBlanc implements FiltreImage {

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

                double rouge = couleur.getRed();
                double vert = couleur.getGreen();

                double bleu = couleur.getBlue();
                double moyenne = (rouge + vert + bleu) / 3.0;

                Color gris = new Color(moyenne, moyenne, moyenne, couleur.getOpacity());
                ecrivain.setColor(x, y, gris);
            }
        }

        return resultat;
    }

    @Override
    public String getNom() {
        return "Noir et Blanc";
    }

}
