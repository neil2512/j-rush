package jrush.app.util;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jrush.app.model.logic.LevelHandler;

import java.io.File;

import static jrush.app.model.logic.LevelHandler.EXTENSION;

/**
 * Classe utilitaire pour afficher des boîtes de dialogue d'erreur et
 * d'information
 */
public final class GuiUtils {

    // CONSTRUCTEURS

    private GuiUtils() {
    }

    // COMMANDES

    /**
     * Affiche une boîte de dialogue d'erreur avec le titre "Erreur" et les
     * informations données en paramètre.
     *
     * @param header Le texte à afficher dans l'en-tête de la boîte de dialogue
     * @param content Le texte à afficher dans le corps de la boîte de dialogue
     */
    public static void showError(String header, String content) {
        showDialog(Alert.AlertType.ERROR, "Erreur", header, content);
    }

    /**
     * Affiche une boîte de dialogue d'information avec les informations données
     * en paramètre.
     *
     * @param title Le titre de la boîte de dialogue
     * @param header Le texte à afficher dans l'en-tête de la boîte de
     * @param content Le texte à afficher dans le corps de la boîte de dialogue
     */
    public static void showInfo(String title, String header, String content) {
        showDialog(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * Centralise la sélection de fichiers pour l'éditeur et le jeu.
     *
     * @param window La fenêtre parente
     * @param isLoad true pour charger, false pour sauvegarder
     */
    public static File showFileChooser(Window window, boolean isLoad) {
        FileChooser chooser = new FileChooser();

        chooser.getExtensionFilters()
               .add(new FileChooser.ExtensionFilter("Niveaux JRush (*.jrush)",
                                                    "*" + EXTENSION));

        if (isLoad) {
            chooser.setTitle("Charger un fichier");
            return chooser.showOpenDialog(window);
        } else {
            chooser.setTitle("Sauvegarder un fichier");
            return chooser.showSaveDialog(window);
        }
    }

    // OUTILS

    /**
     * Affiche une boîte de dialogue avec les informations données en
     * paramètre.
     *
     * @param type Le type de la boîte de dialogue (erreur, information, etc.)
     * @param title Le titre de la boîte de dialogue
     * @param header Le texte à afficher dans l'en-tête de la boîte de
     * @param content Le texte à afficher dans le corps de la boîte de dialogue
     */
    private static void showDialog(
            Alert.AlertType type, String title, String header,
            String content
    ) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
