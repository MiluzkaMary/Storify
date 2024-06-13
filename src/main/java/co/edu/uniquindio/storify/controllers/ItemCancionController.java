package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorListeners.MyListenerCancion;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para el ítem de canción en la interfaz de la aplicación.
 * Maneja la visualización y las interacciones del usuario con un elemento de canción.
 */
@Data
public class ItemCancionController implements Initializable {

    @FXML
    private Text txtAnio;

    @FXML
    private Text txtDuracion;

    @FXML
    private Text lblAutorNombre;

    @FXML
    private Text lblNombreCancion;

    @FXML
    private ImageView portada;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private Cancion cancion = null;
    private boolean esVentanaFavs;
    private boolean esGestion;
    private MyListenerCancion myListenerCancion;

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
     * Método invocado cuando se hace clic en el elemento de canción.
     * Notifica al listener de clic con la canción asociada.
     */
    @FXML
    private void click() {
        myListenerCancion.onClickListener(cancion);
    }

    /**
     * Carga los datos de una canción en la interfaz.
     *
     * @param cancion La canción cuyos datos se cargarán.
     * @throws ArtistaNoEncontradoException Si no se encuentra el artista de la canción.
     */
    public void cargarDatos(Cancion cancion) throws ArtistaNoEncontradoException {
        this.cancion = cancion;

        String nombreCancion = cancion.getNombre();
        Artista nombreAutor = mfm.getTiendaMusica().buscarArtistaCancion(cancion);
        lblNombreCancion.setText(nombreCancion);
        txtAnio.setText("("+cancion.getAnioLanzamiento()+")");
        txtDuracion.setText(cancion.getDuracion());
        lblAutorNombre.setText(nombreAutor.getNombre());

        // Para insertar la foto en el ImageView
        String foto = cancion.getCaratula();
        try {
            Image image = new Image(getClass().getResourceAsStream(foto));
            portada.setImage(image);
        } catch (Exception e) {
            if (!foto.isEmpty()) {
                Image image = new Image(foto);
                portada.setImage(image);
            }
        }
    }

    /**
     * Abre una ventana de YouTube para reproducir el video de la canción.
     * Si la ventana no es de gestión, abre los detalles de la canción.
     */
    public void abrirVentanaYoutube() {
        if (!esGestion) {
            aplicacion.abrirDetalleCancion(cancion);
        } else {
            click();
        }
    }
}
