package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Rotation implements FiltreImage {

    private int nbRotations;

    public Rotation(int nbRotations) {
        // On normalise entre 0 et 3 (par ex si on met 5, cela fait 1)
        this.nbRotations = ((nbRotations % 4) + 4) % 4;
    }

    @Override
    public WritableImage appliquer(WritableImage img) {
        int larg = (int) img.getWidth();
        int haut = (int) img.getHeight();

        WritableImage courante = img;
        int largCourante = larg;
        int hautCourante = haut;

        for (int r = 0; r < nbRotations; r++) {
            // Apres une rotation de 90°, la largeur et la hauteur s'inversent
            WritableImage rotee = new WritableImage(hautCourante, largCourante);
            PixelReader lecteur = courante.getPixelReader();
            PixelWriter ecrivain = rotee.getPixelWriter();

            // On déplace chaque pixel à sa nouvelle position
            for (int y = 0; y < hautCourante; y++) {
                for (int x = 0; x < largCourante; x++) {
                    Color couleur = lecteur.getColor(x, y);

                    // le pixel (x,y) va en (hauteur-1-y,x)
                    int nouvX = hautCourante - 1 - y;
                    int nouvY = x;
                    ecrivain.setColor(nouvX, nouvY, couleur);
                }
            }
            courante = rotee;
            int temp = largCourante;
            largCourante = hautCourante;
            hautCourante = temp;
        }

        return courante;
    }

    @Override
    public String getNom() {
        return "Rotation " + (nbRotations * 90) + "°";
    }
}
