package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.AdministradorComandos;
import co.edu.uniquindio.storify.controllers.controladorFlujo.Comando;
import co.edu.uniquindio.storify.controllers.controladorFlujo.ComandoAgregarCancion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.ComandoEliminarCancion;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.model.*;
import co.edu.uniquindio.storify.util.Alertas;
import co.edu.uniquindio.storify.util.YouTubeHelper;
import co.edu.uniquindio.storify.util.YoutubeEmbedGenerator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de detalles de una canción.
 * Muestra información detallada de la canción seleccionada y permite agregarla o eliminarla de favoritos.
 */
@Data
public class VentanaCancionDetalleController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private AdministradorComandos administradorComandos = mfm.getAdministradorComandos();

    private Cancion cancion;

    @FXML
    private Text txtDuracion;

    @FXML
    private Text txtCancion;

    @FXML
    private Text txtAlbum;

    @FXML
    private WebView webView;

    @FXML
    private Text txtAnio;

    @FXML
    private Text txtArtista;

    @FXML
    private Text txtGenero;

    @FXML
    private Button btnEliminarFav;

    @FXML
    private Button btnAgregarFav;

    @FXML
    private Button btnVolverAdmin;

    @FXML
    private Text txtVistas;

    /**
     * Método de inicialización que se ejecuta cuando se carga la interfaz.
     *
     * @param url La URL de la localización utilizada para resolver rutas relativas.
     * @param resourceBundle El recurso utilizado para localizar objetos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEliminarFav.setVisible(false);
        btnAgregarFav.setVisible(false);
        btnVolverAdmin.setVisible(false);
    }

    /**
     * Detiene la reproducción del video en el WebView.
     */
    public void stopWebView() {
        if (cancion != null) {
            webView.getEngine().load("");
        }
    }

    /**
     * Permite la visibilidad del botón de volver a la administración si el usuario es un administrador.
     */
    public void permitirVolverAdmin() {
        Persona persona = usuario.getPersona();
        if (persona instanceof Administrador) {
            btnVolverAdmin.setVisible(true);
        }
    }

    /**
     * Vuelve a la ventana de gestión de canciones para administradores.
     */
    public void volverAdmin() {
        aplicacion.motrarVentanaGestionCanciones();
        aplicacion.detenerVideoYoutube();
    }

    /**
     * Inicia la reproducción del video y muestra los detalles de la canción.
     */
    public void iniciarVideoDatos() {
        try {
            iniciarTextos();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        WebEngine webEngine = webView.getEngine();
        webEngine.load("https://blank.page/");

        String embedCode = YoutubeEmbedGenerator.obtenerEmbedCode(cancion.getUrlYoutube());

        webEngine.documentProperty().addListener((obs, oldDoc, newDoc) -> {
            if (newDoc != null) {
                // Injectar JavaScript para cambiar el contenido de la página
                String newContent = "<!DOCTYPE html>" +
                        "<html lang=\"es\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                        "<title>Reproductor de Video</title>" +
                        "<style>" +
                        "body, html {" +
                        "height: 100%;" +
                        "margin: 0;" +
                        "display: flex;" +
                        "justify-content: center;" +
                        "align-items: center;" +
                        "background-color: #000;" +
                        "}" +
                        ".video-container {" +
                        "width: 560px;" +
                        "height: 315px;" +
                        "}" +
                        "iframe {" +
                        "width: 100%;" +
                        "height: 100%;" +
                        "border: none;" +
                        "}" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"video-container\">" +
                        "<iframe width=\"560\" height=\"315\" src=\"linkEmbed\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"no-referrer-when-downgrade\" allowfullscreen></iframe>" +
                        "</div>" +
                        "</body>" +
                        "</html>";

                String oldUrl = "linkEmbed";
                String newURL = newContent.replace(oldUrl, embedCode);

                webEngine.executeScript(
                        "document.open();" +
                                "document.write('" + newURL.replaceAll("'", "\\\\'") + "');" +
                                "document.close();"
                );
            }
        });
    }

    /**
     * Agrega la canción a la lista de favoritos del usuario.
     */
    public void agregarFav() {
        stopWebView();

        Comando agregarCancion = new ComandoAgregarCancion((Cliente) usuario.getPersona(), cancion);
        administradorComandos.ejecutarComando(agregarCancion);
        mfm.guardarDatosBinario();

        aplicacion.mostrarVentanaMisCanciones();
        Alertas.mostrarMensaje("Actualización Exitosa", "Operación completada", "¡Haz agregado correctamente una canción a tu lista de favoritos! Puedes deshacer esta acción si lo requieres", Alert.AlertType.INFORMATION);

        aplicacion.ventanaInicioController.mostrarBarraSuperiorCliente(administradorComandos);
    }

    /**
     * Elimina la canción de la lista de favoritos del usuario.
     */
    public void eliminarFav() {
        stopWebView();
        Comando eliminarCancion = new ComandoEliminarCancion((Cliente) usuario.getPersona(), cancion);
        administradorComandos.ejecutarComando(eliminarCancion);
        mfm.guardarDatosBinario();

        aplicacion.mostrarVentanaMisCanciones();
        Alertas.mostrarMensaje("Actualización Exitosa", "Operación completada", "¡Haz eliminado correctamente una canción de tu lista de favoritos! Puedes deshacer esta acción si lo requieres", Alert.AlertType.INFORMATION);
        aplicacion.ventanaInicioController.mostrarBarraSuperiorCliente(administradorComandos);
    }

    /**
     * Inicializa los textos con la información de la canción.
     *
     * @throws GeneralSecurityException En caso de error de seguridad general.
     * @throws IOException En caso de error de E/S.
     */
    public void iniciarTextos() throws GeneralSecurityException, IOException {
        Persona persona = usuario.getPersona();
        if (persona instanceof Cliente) {
            Cliente clienteUsuario = (Cliente) usuario.getPersona();
            if (clienteUsuario.getCancionesFavoritas().contains(cancion)) {
                btnEliminarFav.setVisible(true);
            } else {
                btnAgregarFav.setVisible(true);
            }
        }

        txtCancion.setText(cancion.getNombre());
        txtAlbum.setText("Album: " + cancion.getAlbum());
        txtAnio.setText("Año de lanzamiento: " + cancion.getAnioLanzamiento());
        txtDuracion.setText("Duración: " + cancion.getDuracion());
        txtGenero.setText("Género: " + cancion.obtenerGeneroComoString());
        Artista artista;
        long vistas = YouTubeHelper.obtenerVistasVideo(cancion.getUrlYoutube());
        try {
            artista = mfm.getTiendaMusica().buscarArtistaCancion(cancion);
        } catch (ArtistaNoEncontradoException e) {
            throw new RuntimeException(e);
        }
        txtArtista.setText("Artista: " + artista.getNombre());
        txtVistas.setText("Vistas: " + vistas);
    }
}
