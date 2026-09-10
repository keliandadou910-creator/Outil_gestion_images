package fr.univ.imagemanager.controller;

import fr.univ.imagemanager.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    // Eléments de l'interface

    @FXML
    private ImageView imageView;

    @FXML
    private Label labelInfo;

    @FXML
    private TextField champTag;

    @FXML
    private FlowPane conteneurTags;

    @FXML
    private Label labelTransformations;

    @FXML
    private ScrollPane scrollPane;

    // Les Attribut du modèle

    private WritableImage imgCourante;

    private WritableImage imgOriginale;

    private String cheminImgCourante;

    private GestionMetaDonnees gestionMeta;

    private MetaDonnees metaCourantes;

    @FXML
    public void initialize() {
        String cheminMeta = System.getProperty("user.dir") + File.separator + "image_manager_metadata.txt";
        gestionMeta = new GestionMetaDonnees(cheminMeta);

        imageView.setPreserveRatio(true);

        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                    Bounds ancienneValeur, Bounds nouvelleValeur) {
                imageView.setFitWidth(nouvelleValeur.getWidth() - 20);
            }
        });

        labelInfo.setText("Bienvenue ! Ouvrez une image pour commencer.");
    }

    // gestion fichiers
    @FXML
    private void ouvrirImage() {
        FileChooser selecteur = new FileChooser();
        selecteur.setTitle("Sélectionner une image");

        FileChooser.ExtensionFilter filtreImages = new FileChooser.ExtensionFilter(
                "Images", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif");
        FileChooser.ExtensionFilter filtreTous = new FileChooser.ExtensionFilter(
                "Tous les fichiers", "*.*");
        selecteur.getExtensionFilters().add(filtreImages);
        selecteur.getExtensionFilters().add(filtreTous);

        Stage fenetre = (Stage) imageView.getScene().getWindow();
        File fichier = selecteur.showOpenDialog(fenetre);

        if (fichier != null) {
            try {
                chargerEtAfficherImage(fichier);
            } catch (Exception e) {
                afficherErreur("Erreur lors du chargement de l'image", e.getMessage());
            }
        }
    }

    private void chargerEtAfficherImage(File fichier) {
        Image image = new Image(fichier.toURI().toString());

        imgCourante = convertirEnWritable(image);
        imgOriginale = convertirEnWritable(image);
        cheminImgCourante = fichier.getAbsolutePath();

        imageView.setImage(imgCourante);

        metaCourantes = gestionMeta.getMetaDonnees(cheminImgCourante);
        mettreAJourAffichageTags();
        mettreAJourAffichageTransformations();

        int largeur = (int) image.getWidth();
        int hauteur = (int) image.getHeight();
        labelInfo.setText("Image chargée : " + fichier.getName()
                + " (" + largeur + "×" + hauteur + ")");
    }

    @FXML
    private void sauvegarderImage() {
        if (imgCourante == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        FileChooser selecteur = new FileChooser();
        selecteur.setTitle("Sauvegarder l'image");
        selecteur.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG", "*.png"));
        selecteur.setInitialFileName("image_modifiee.png");

        Stage fenetre = (Stage) imageView.getScene().getWindow();
        File fichier = selecteur.showSaveDialog(fenetre);

        if (fichier != null) {
            try {
                BufferedImage buffered = SwingFXUtils.fromFXImage(imgCourante, null);
                ImageIO.write(buffered, "png", fichier);
                labelInfo.setText("Image sauvegardée : " + fichier.getName());
            } catch (IOException e) {
                afficherErreur("Erreur de sauvegarde", e.getMessage());
            }
        }
    }

    // filtres

    private void appliquerFiltre(FiltreImage filtre) {
        if (imgCourante == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        imgCourante = filtre.appliquer(imgCourante);
        imageView.setImage(imgCourante);

        TransformationInfo infoTransfo = new TransformationInfo(filtre.getNom());
        metaCourantes.ajouterTransformation(infoTransfo);
        gestionMeta.sauvegarder();
        mettreAJourAffichageTransformations();

        labelInfo.setText("Filtre appliqué : " + filtre.getNom());
    }

    // Les different Boutons de filtre

    @FXML
    private void appliquerEchangeRGB() {
        appliquerFiltre(new FiltreEchangeComposantes());
    }

    @FXML
    private void appliquerNoirEtBlanc() {
        appliquerFiltre(new FiltreNoirEtBlanc());
    }

    @FXML
    private void appliquerSepia() {
        appliquerFiltre(new FiltreSepia());
    }

    @FXML
    private void appliquerPrewitt() {
        appliquerFiltre(new FiltrePrewitt());
    }

    @FXML
    private void appliquerRotation90() {
        appliquerFiltre(new Rotation(1));
    }

    @FXML
    private void appliquerRotation180() {
        appliquerFiltre(new Rotation(2));
    }

    @FXML
    private void appliquerRotation270() {
        appliquerFiltre(new Rotation(3));
    }

    @FXML
    private void appliquerSymetrieH() {
        appliquerFiltre(new Symetrie(true));
    }

    @FXML
    private void appliquerSymetrieV() {
        appliquerFiltre(new Symetrie(false));
    }

    @FXML
    private void reinitialiserImage() {
        if (imgOriginale == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        imgCourante = copierImage(imgOriginale);
        imageView.setImage(imgCourante);
        metaCourantes.getTransformations().clear();
        gestionMeta.sauvegarder();
        mettreAJourAffichageTransformations();

        labelInfo.setText("Image réinitialisée à l'original.");
    }

    // chiffrement
    @FXML
    private void chiffrerImage() {
        if (imgCourante == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        String motDePasse = demanderMotDePasse("Chiffrement d'image",
                "Entrez un mot de passe pour chiffrer l'image :");

        if (motDePasse != null && motDePasse.trim().length() > 0) {
            appliquerFiltre(new FiltreChiffrement(motDePasse, true));
        } else if (motDePasse != null) {
            afficherErreur("Mot de passe vide", "Veuillez entrer un mot de passe valide.");
        }
    }

    @FXML
    private void dechiffrerImage() {
        if (imgCourante == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        String motDePasse = demanderMotDePasse("Déchiffrement d'image",
                "Entrez le mot de passe pour déchiffrer l'image :");

        if (motDePasse != null && motDePasse.trim().length() > 0) {
            appliquerFiltre(new FiltreChiffrement(motDePasse, false));
        } else if (motDePasse != null) {
            afficherErreur("Mot de passe vide", "Veuillez entrer un mot de passe valide.");
        }
    }

    private String demanderMotDePasse(String titre, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titre);
        dialog.setHeaderText(message);
        dialog.setContentText("Mot de passe :");

        java.util.Optional<String> resultat = dialog.showAndWait();

        if (resultat.isPresent()) {
            return resultat.get();
        } else {
            return null;
        }
    }

    // Gestion des tags
    @FXML
    private void ajouterTag() {
        if (metaCourantes == null) {
            afficherErreur("Aucune image", "Veuillez d'abord ouvrir une image.");
            return;
        }

        String tag = champTag.getText().trim();

        if (tag.length() > 0) {
            metaCourantes.ajouterTag(tag);
            gestionMeta.sauvegarder();
            mettreAJourAffichageTags();
            champTag.clear();
            labelInfo.setText("Tag ajouté : " + tag);
        }
    }

    /*
     * Note à nous -même : on met à jour l'affichage des tags dans le FlowPane.
     * et chaque tags est un bouton cliquable donc si on clique dessus, on le
     * supprime.
     */
    private void mettreAJourAffichageTags() {
        conteneurTags.getChildren().clear();

        if (metaCourantes == null) {
            return;
        }

        List<String> listeTags = metaCourantes.getTags();
        for (int i = 0; i < listeTags.size(); i++) {
            final String tagCourant = listeTags.get(i);

            Button boutonTag = new Button(tagCourant + " ×");
            boutonTag.getStyleClass().add("tag-button");

            boutonTag.setOnAction(new EventHandler<ActionEvent>() {
                @Override

                public void handle(ActionEvent event) {
                    metaCourantes.supprimerTag(tagCourant);
                    gestionMeta.sauvegarder();
                    mettreAJourAffichageTags();
                    labelInfo.setText("Tag supprimé : " + tagCourant);
                }
            });
            conteneurTags.getChildren().add(boutonTag);
        }
    }

    // Met a jour le label qui affiche la liste des transformations.
    private void mettreAJourAffichageTransformations() {
        if (metaCourantes == null || metaCourantes.getTransformations().isEmpty()) {
            labelTransformations.setText("Aucune transformation appliquée.");
            return;
        }

        // On choisi de construire la chaine "Transformations : filtre1 → filtre2 →
        // filtre3"
        StringBuilder sb = new StringBuilder("Transformations : ");
        List<TransformationInfo> listeTransfos = metaCourantes.getTransformations();

        for (int i = 0; i < listeTransfos.size(); i++) {
            if (i > 0) {
                sb.append(" → ");
            }
            sb.append(listeTransfos.get(i).getNom());
        }

        labelTransformations.setText(sb.toString());
    }

    // Convertit une Image javafx en WritableImage.
    private WritableImage convertirEnWritable(Image image) {
        int larg = (int) image.getWidth();
        int haut = (int) image.getHeight();

        WritableImage writable = new WritableImage(larg, haut);

        writable.getPixelWriter().setPixels(
                0, 0, larg, haut,
                image.getPixelReader(), 0, 0);
        return writable;
    }

    // Fait une copie d'une WritableImage (Attention, c'est une copie profonde).
    private WritableImage copierImage(WritableImage source) {
        int larg = (int) source.getWidth();
        int haut = (int) source.getHeight();

        WritableImage copie = new WritableImage(larg, haut);

        copie.getPixelWriter().setPixels(
                0, 0, larg, haut,
                source.getPixelReader(), 0, 0);
        return copie;
    }

    // Affiche une boite de dialogue d'erreur.
    private void afficherErreur(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle("Erreur");
        alerte.setHeaderText(titre);
        alerte.setContentText(message);
        alerte.showAndWait();
    }
}
