package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FiltrePrewitt implements FiltreImage {

    private static final int[][] NOYAU_GX = {
            { -1, 0, 1 }, { -1, 0, 1 }, { -1, 0, 1 }
    };

    private static final int[][] NOYAU_GY = {
            { -1, -1, -1 }, { 0, 0, 0 }, { 1, 1, 1 }
    };

    @Override
    public WritableImage appliquer(WritableImage img) {
        int larg = (int) img.getWidth();
        int haut = (int) img.getHeight();

        WritableImage resultat = new WritableImage(larg, haut);
        PixelReader lecteur = img.getPixelReader();
        PixelWriter ecrivain = resultat.getPixelWriter();

        for (int y = 1; y < haut - 1; y++) {
            for (int x = 1; x < larg - 1; x++) {
                double gxR = 0, gyR = 0;
                double gxV = 0, gyV = 0;
                double gxB = 0, gyB = 0;

                // Parcours du voisinage
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        Color voisin = lecteur.getColor(x + dx, y + dy);
                        int iy = dy + 1;
                        int ix = dx + 1;

                        gxR = gxR + voisin.getRed() * NOYAU_GX[iy][ix];
                        gyR = gyR + voisin.getRed() * NOYAU_GY[iy][ix];

                        gxV = gxV + voisin.getGreen() * NOYAU_GX[iy][ix];
                        gyV = gyV + voisin.getGreen() * NOYAU_GY[iy][ix];

                        gxB = gxB + voisin.getBlue() * NOYAU_GX[iy][ix];
                        gyB = gyB + voisin.getBlue() * NOYAU_GY[iy][ix];
                    }
                }

                // pythagore (ça faisait longtemps :))
                double magR = Math.min(Math.sqrt(gxR * gxR + gyR * gyR), 1.0);
                double magV = Math.min(Math.sqrt(gxV * gxV + gyV * gyV), 1.0);
                double magB = Math.min(Math.sqrt(gxB * gxB + gyB * gyB), 1.0);
                ecrivain.setColor(x, y, new Color(magR, magV, magB, 1.0));
            }
        }
        return resultat;

    }

    @Override
    public String getNom() {
        return "Prewitt (Contours)";
    }
}
