package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para el tablero de administración en la interfaz de la aplicación.
 * Maneja las acciones de gestión y visualización de canciones, artistas, analíticas y archivos.
 */
@Data
public class TableroAdminController implements Initializable {

    public ImageView btnCanciones;
    public ImageView btnGestionarArtistas;
    public ImageView btnAnaliticas;
    public ImageView btnGestionarCanciones;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;

    /**
     * Método de inicialización que se ejecuta cuando se carga la interfaz.
     *
     * @param url La URL de la localización utilizada para resolver rutas relativas.
     * @param resourceBundle El recurso utilizado para localizar objetos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No se requiere ninguna inicialización especial en este momento.
    }

    /**
     * Abre la ventana de gestión de canciones.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void gestionarCanciones() {
        aplicacion.motrarVentanaGestionCanciones();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Abre la ventana de gestión de artistas.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void gestionarArtistas() {
        aplicacion.motrarVentanaGestionArtista();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Abre la ventana para ver las canciones generales.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void verCancionesGenerales() {
        aplicacion.mostrarVentanaCancionesGenerales();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Abre la ventana de estadísticas.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void verEstadisticas() {
        aplicacion.mostrarVentanaEstadisticas(true);
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Abre la ventana de archivos y textos.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void ventanaArchivos() {
        aplicacion.mostrarVentanaArchivosTextos();
        aplicacion.detenerVideoYoutube();
    }
}
