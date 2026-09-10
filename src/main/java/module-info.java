
module fr.univ.imagemanager {
    // pour l'interface graphique
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing; // pour sauvegarder une image

    // sauvegarde en PNG
    requires java.desktop;

    // pour que le FXML puisse trouver nos controleurs grâce à la réflexion
    opens fr.univ.imagemanager.controller to javafx.fxml;

    // exporte le package principal pour que JavaFX lance l'app
    exports fr.univ.imagemanager;
}
