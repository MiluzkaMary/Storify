package co.edu.uniquindio.storify.app;

import co.edu.uniquindio.storify.controllers.ModelFactoryController;
import co.edu.uniquindio.storify.controllers.VentanaCancionDetalleController;
import co.edu.uniquindio.storify.controllers.VentanaInicioController;
import co.edu.uniquindio.storify.controllers.VentanaRegistroController;
import co.edu.uniquindio.storify.exceptions.AtributoVacioException;
import co.edu.uniquindio.storify.exceptions.UsuarioNoExistenteException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * La clase Aplicacion es la clase principal de la aplicación Storify.
 * Extiende la clase Application de JavaFX y maneja la inicialización de la interfaz gráfica de usuario (GUI).
 */
public class Aplicacion extends Application {

    private Stage stage;
    private AnchorPane rootLayout;

    public VentanaInicioController ventanaInicioController;
    public VentanaCancionDetalleController ventanaCancionDetalleController;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();

    /**
     * Inicia la aplicación y muestra la ventana de registro e ingreso.
     * @param stage el escenario principal de la aplicación.
     * @throws Exception si ocurre un error durante la carga de la ventana.
     */
    @Override
    public void start(Stage stage) throws Exception {
        mfm.setAplicacion(this);
        this.stage = stage;
        this.stage.setTitle("Storify");
        //this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/logoFinalVentana.png")));
        this.stage.setResizable(false);
        mostrarVentanaRegistroIngreso();
    }

    /**
     * Muestra la ventana de registro e ingreso.
     */
    public void mostrarVentanaRegistroIngreso() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ventanaRegistro.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Storify | Inicio de Sesion");
            stage.setResizable(false);
            stage.show();
            VentanaRegistroController controlador = loader.getController();
            controlador.setVentana(stage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana principal de la aplicación después de que el usuario ha iniciado sesión.
     * @param usuario el usuario que ha iniciado sesión.
     * @throws AtributoVacioException si algún atributo requerido está vacío.
     * @throws UsuarioNoExistenteException si el usuario no existe.
     */
    public void mostrarVentanaPrincipal(Usuario usuario) throws AtributoVacioException, UsuarioNoExistenteException {
        try {
            FXMLLoader loader = new FXMLLoader(Aplicacion.class.getResource("/ventanas/VentanaInicio.fxml"));
            rootLayout = (AnchorPane) loader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            VentanaInicioController controlador = loader.getController();
            ventanaInicioController = loader.getController();
            controlador.setUsuario(usuario);
            String tipoUsuario = mfm.getTiendaMusica().obtenerTipoUsuario(usuario.getUsername(), usuario.getPassword());
            switch (tipoUsuario) {
                case "Administrador":
                    controlador.mostrarPanelIzquierdoAdmin();
                    controlador.mostrarPanelDerechoAdminGestionarCanciones();
                    controlador.mostrarBarraSuperiorAdmin(null);
                    break;
                case "Cliente":
                    controlador.mostrarPanelIzquierdoCliente();
                    controlador.mostrarPanelDerechoClienteFavoritos();
                    controlador.mostrarBarraSuperiorCliente(null);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana para crear o editar una canción.
     * @param cancion la canción a crear o editar.
     */
    public void mostrarVentanaCrearEditarCancion(Cancion cancion) {
        ventanaInicioController.mostrarPanelDerechoCrearEditarCancion(cancion);
    }

    /**
     * Muestra la ventana para crear o editar un artista.
     * @param artista el artista a crear o editar.
     */
    public void mostrarVentanaCrearEditarArtista(Artista artista) {
        ventanaInicioController.mostrarPanelDerechoCrearEditarArtista(artista);
    }

    /**
     * Muestra la ventana de las canciones favoritas del cliente.
     */
    public void mostrarVentanaMisCanciones() {
        ventanaInicioController.mostrarPanelDerechoClienteFavoritos();
    }

    /**
     * Muestra la ventana de todas las canciones disponibles en la plataforma.
     */
    public void mostrarVentanaCancionesGenerales() {
        ventanaInicioController.mostrarPanelDerechoCancionesGenerales();
    }

    /**
     * Muestra la ventana de estadísticas.
     * @param empiezaGenero indicador para filtrar estadísticas por género.
     */
    public void mostrarVentanaEstadisticas(Boolean empiezaGenero) {
        ventanaInicioController.mostrarPanelDerechoEstadisticas(empiezaGenero);
    }

    public void mostrarVentanaArchivosTextos(){
        ventanaInicioController.mostrarPanelDerechoArchivoTextos();
    }

    /**
     * Abre una ventana con los detalles de una canción específica.
     * @param cancion la canción cuyos detalles se desean ver.
     */
    public void abrirDetalleCancion(Cancion cancion) {
        ventanaInicioController.abrirVentanaDetalleCancion(cancion);
    }

    /**
     * Muestra la ventana de gestión de artistas.
     */
    public void mostrarVentanaArtistas() {
        ventanaInicioController.mostrarVentanaFiltrarArtistas();
    }

    /**
     * Muestra la ventana de canciones filtradas por si pertenecen a una banda.
     * @param esBanda indicador para filtrar por bandas.
     */
    public void mostrarVentanaCancionesBanda(boolean esBanda) {
        ventanaInicioController.mostrarVentanaBandas(esBanda);
    }

    /**
     * Muestra las canciones de un artista específico.
     * @param artistaElegido el artista cuyas canciones se desean ver.
     */
    public void verCancionesDeArtista(Artista artistaElegido) {
        ventanaInicioController.mostrarPanelDerechoCancionesArtista(artistaElegido);
    }

    /**
     * Muestra la ventana de gestión de canciones para administradores.
     */
    public void motrarVentanaGestionCanciones() {
        ventanaInicioController.mostrarPanelDerechoAdminGestionarCanciones();
    }

    /**
     * Muestra la ventana de gestión de artistas para administradores.
     */
    public void motrarVentanaGestionArtista() {
        ventanaInicioController.mostrarPanelDerechoAdminGestionarArtistas();
    }

    /**
     * Detiene la reproducción del video de YouTube en la ventana de detalle de canción.
     */
    public void detenerVideoYoutube() {
        if (ventanaCancionDetalleController != null) {
            ventanaCancionDetalleController.stopWebView();
        }
    }

    /**
     * Muestra la ventana de perfil del usuario.
     */
    public void mostrarVentanaPerfil() {
        ventanaInicioController.mostrarVentanaPerfil();
    }
}
