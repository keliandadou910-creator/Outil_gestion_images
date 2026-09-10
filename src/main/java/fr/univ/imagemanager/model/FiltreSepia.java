package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FiltreSepia implements FiltreImage {

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

                double r = couleur.getRed();
                double g = couleur.getGreen();

                double b = couleur.getBlue();
                double nvRouge = 0.393 * r + 0.769 * g + 0.189 * b;

                double nvVert = 0.349 * r + 0.686 * g + 0.168 * b;
                double nvBleu = 0.272 * r + 0.534 * g + 0.131 * b;

                nvRouge = plafonner(nvRouge);
                nvVert = plafonner(nvVert);

                nvBleu = plafonner(nvBleu);
                Color couleurSepia = new Color(nvRouge, nvVert, nvBleu, couleur.getOpacity());
                ecrivain.setColor(x, y, couleurSepia);
            }
        }
        return resultat;
    }

    private double plafonner(double valeur) {
        if (valeur > 1.0) {
            return 1.0;
        }
        return valeur;
    }

    @Override
    public String getNom() {
        return "Sépia";
    }
}
