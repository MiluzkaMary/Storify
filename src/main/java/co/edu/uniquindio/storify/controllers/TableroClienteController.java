package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para el tablero del cliente en la interfaz de la aplicación.
 * Maneja las acciones de navegación y gestión de las opciones disponibles para el cliente.
 */
@Data
public class TableroClienteController implements Initializable {

    public Button btnSolistas;
    public Button btnBandas;
    public Button btnArtistas;
    public Button btnMisFavoritos;
    public Button btnGeneros;
    public Button btnCanciones;
    public Button btnAjustesPerfil;
    public ImageView btnUnmute;
    public ImageView btnPausa;
    public ImageView btnBack;
    public ImageView btnNext;
    public Label txtNombreCancion;
    public Label txtNombreArtista;
    public ImageView btnMeGusta;
    public ImageView btnNoMeGusta;

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
     * Muestra la ventana de canciones favoritas del usuario.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void mostrarFavoritos() {
        aplicacion.mostrarVentanaMisCanciones();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Muestra la ventana de todas las canciones disponibles.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void mostrarCanciones() {
        aplicacion.mostrarVentanaCancionesGenerales();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Muestra la ventana de canciones de solistas.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void mostrarSolistas() {
        aplicacion.mostrarVentanaCancionesBanda(false);
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Muestra la ventana de canciones de bandas.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void mostrarBandas() {
        aplicacion.mostrarVentanaCancionesBanda(true);
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Muestra la ventana de artistas.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     */
    public void mostrarArtistas() {
        aplicacion.mostrarVentanaArtistas();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Muestra la ventana del perfil del usuario.
     * Detiene la reproducción de cualquier video de YouTube en curso.
     *
     * @param actionEvent El evento que desencadena esta acción.
     */
    public void mostrarPerfil(ActionEvent actionEvent) {
        aplicacion.mostrarVentanaPerfil();
        aplicacion.detenerVideoYoutube();
    }
}
