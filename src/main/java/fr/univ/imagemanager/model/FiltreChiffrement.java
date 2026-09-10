package fr.univ.imagemanager.model;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;

public class FiltreChiffrement implements FiltreImage {

    private boolean modeChiffrer;
    private String motDePasse;

    public FiltreChiffrement(String motDePasse, boolean modeChiffrer) {
        this.motDePasse = motDePasse;
        this.modeChiffrer = modeChiffrer;
    }

    @Override

    public WritableImage appliquer(WritableImage img) {
        int larg = (int) img.getWidth();
        int haut = (int) img.getHeight();
        int nbPixels = larg * haut;

        PixelReader lecteur = img.getPixelReader();

        // Technique pour lire tout les pixels dans un tableau
        int[] pixels = new int[nbPixels];
        for (int y = 0; y < haut; y++) {
            for (int x = 0; x < larg; x++) {
                int indice = y * larg + x;
                pixels[indice] = lecteur.getArgb(x, y);
            }
        }

        int[] permutation = genererPermutation(nbPixels);

        int[] pixelsResultat = new int[nbPixels];

        if (modeChiffrer) {
            for (int i = 0; i < nbPixels; i++) {
                pixelsResultat[permutation[i]] = pixels[i];
            }
        } else {
            for (int i = 0; i < nbPixels; i++) {
                pixelsResultat[i] = pixels[permutation[i]];
            }
        }
        WritableImage resultat = new WritableImage(larg, haut);
        PixelWriter ecrivain = resultat.getPixelWriter();

        for (int y = 0; y < haut; y++) {
            for (int x = 0; x < larg; x++) {
                int indice = y * larg + x;
                ecrivain.setArgb(x, y, pixelsResultat[indice]);
            }
        }
        return resultat;
    }

    private int[] genererPermutation(int n) {
        byte[] graine;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            graine = digest.digest(motDePasse.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 non disponible", e);
        }
        try {
            SecureRandom aleatoire = SecureRandom.getInstance("SHA1PRNG");
            aleatoire.setSeed(graine);

            int[] permutation = new int[n];
            for (int i = 0; i < n; i++) {
                permutation[i] = i;
            }

            // Algorithme de Fisher-Yates pour le mélange du tableau
            for (int i = n - 1; i > 0; i--) {
                int j = aleatoire.nextInt(i + 1);
                int temp = permutation[i];
                permutation[i] = permutation[j];
                permutation[j] = temp;
            }
            return permutation;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA1PRNG non disponible", e);
        }
    }

    @Override
    public String getNom() {
        if (modeChiffrer) {
            return "Chiffrement";
        } else {
            return "Déchiffrement";
        }
    }
}
