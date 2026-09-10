package fr.univ.imagemanager.model;

import javafx.scene.image.WritableImage;

public interface FiltreImage {

    WritableImage appliquer(WritableImage img);

    String getNom();

}
