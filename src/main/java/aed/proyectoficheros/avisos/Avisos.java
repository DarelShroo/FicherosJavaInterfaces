package aed.proyectoficheros.avisos;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

public class Avisos {
    public static void warningDialog(String titulo, String aviso, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(aviso);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    public static Object errorDialog(String titulo, String aviso, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(aviso);
        alert.setContentText(mensaje);

        alert.showAndWait();
        return null;
    }

    public static String confirmationDialog(String titulo, String aviso, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(aviso);
        alert.setContentText(mensaje);
        String nombreDoc = "";
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK && titulo.equals("Confirmación Nuevo Documento")) {
            nombreDoc = textInputDialog("Nuevo documento", "Vas a crear un nuevo documento", "Escribe el nombre del nuevo documento");
        } else if (result.get() == ButtonType.OK && titulo.equals("Aviso borrar")) {
            nombreDoc = "si";
        }else if(result.get() == ButtonType.OK && titulo.equals("Aviso reemplazar")){
            nombreDoc = "si";
        }
        return nombreDoc;
    }

    public static String textInputDialog(String titulo, String aviso, String mensaje) {
        TextInputDialog dialog = new TextInputDialog("habitaciones.xml");
        dialog.setTitle(titulo);
        dialog.setHeaderText(aviso);
        dialog.setContentText(mensaje);
        String devuelve = "";

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            devuelve = result.get();
        }

        return devuelve;
    }

    public static void informationDialog(String titulo, String aviso, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(aviso);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }
}
