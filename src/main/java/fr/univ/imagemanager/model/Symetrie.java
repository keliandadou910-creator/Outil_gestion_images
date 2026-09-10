package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Symetrie implements FiltreImage {

    private boolean horizontale;

    public Symetrie(boolean horizontale) {
        this.horizontale = horizontale;
    }

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

                if (horizontale) {
                    // le pixel tout a gauche va tout à droite
                    int nouvX = larg - 1 - x;
                    ecrivain.setColor(nouvX, y, couleur);
                } else {
                    // Le pixel tout en haut va tout en bas
                    int nouvY = haut - 1 - y;
                    ecrivain.setColor(x, nouvY, couleur);
                }
            }
        }
        return resultat;
    }

    @Override

    public String getNom() {
        if (horizontale) {
            return "Symétrie Horizontale";
        } else {
            return "Symétrie Verticale";
        }
    }
}
