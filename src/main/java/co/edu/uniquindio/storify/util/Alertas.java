package co.edu.uniquindio.storify.util;

import javafx.scene.control.Alert;

@SuppressWarnings("all")
public class Alertas {

    /**
     * Muestra una alerta de error con el mensaje proporcionado.
     *
     * @param mensaje El mensaje a mostrar en la alerta de error
     */
    public static void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    /**
     * Muestra una alerta de informacion con el mensaje proporcionado.
     *
     * @param mensaje El mensaje a mostrar en la alerta de error
     */
    public static void mostrarAlertaInformacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    /**
     * Metodo que muestra un mensaje
     * @param titulo Titulo del mensaje
     * @param encabezado Cabecera del mensaje
     * @param texto Texto principal del mensaje
     * @param alerta Alerta del mensaje
     */
    public static void mostrarMensaje(String titulo, String encabezado, String texto, Alert.AlertType alertaTipo) {
        Alert alert = new Alert(alertaTipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(texto);
        alert.showAndWait();
    }

}
