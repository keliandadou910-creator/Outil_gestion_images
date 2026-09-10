package fr.univ.imagemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override

    public void start(Stage fenetrePrincipale) throws Exception {
        FXMLLoader chargeur = new FXMLLoader(getClass().getResource("/fr/univ/imagemanager/view/main_view.fxml"));
        Parent racine = chargeur.load();

        Scene scene = new Scene(racine, 1100, 750);

        String cheminCSS = getClass().getResource("/fr/univ/imagemanager/view/style.css").toExternalForm();
        scene.getStylesheets().add(cheminCSS);

        fenetrePrincipale.setTitle("Gestionnaire d'Images - POO Java L2");
        fenetrePrincipale.setScene(scene);
        fenetrePrincipale.setMinWidth(900);
        fenetrePrincipale.setMinHeight(600);

        fenetrePrincipale.show();
    }

    // Méthode main classique
    // launch() va appeler start() directement après.
    public static void main(String[] args) {
        launch(args);
    }
}
