package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.AdministradorComandos;
import co.edu.uniquindio.storify.controllers.controladorFlujo.Comando;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;

@Data
public class VentanaInicioController implements Initializable {

    @FXML
    private AnchorPane panelIzquierdo;

    @FXML
    private AnchorPane panelPrincipal;

    @FXML
    private AnchorPane barraSuperior;

    @FXML
    private AnchorPane panelDerecho;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;

    /**
     * Método que se ejecuta al inicializar el controlador.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // No se realiza ninguna acción en la inicialización.
    }

    /**
     * Muestra el panel izquierdo con la vista del cliente.
     */
    public void mostrarPanelIzquierdoCliente() {
        try {
            panelIzquierdo.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/TableroCliente.fxml"));
            Node node = loader.load();
            panelIzquierdo.getChildren().add(node);
            TableroClienteController controlador = loader.getController();
            controlador.setUsuario(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel izquierdo con la vista del administrador.
     */
    public void mostrarPanelIzquierdoAdmin() {
        try {
            panelIzquierdo.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/TableroAdmins.fxml"));
            Node node = loader.load();
            panelIzquierdo.getChildren().add(node);
            TableroAdminController controlador = loader.getController();
            controlador.setUsuario(this.usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho para que el administrador gestione canciones.
     */
    public void mostrarPanelDerechoAdminGestionarCanciones() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaGestionar.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaGestionarController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.establecerListaCancionesGenerales();
            controlador.iniciarGestionCanciones();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho para que el administrador gestione artistas.
     */
    public void mostrarPanelDerechoAdminGestionarArtistas() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaGestionar.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaGestionarController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.iniciarGestionArtistas();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho con las canciones favoritas del cliente.
     */
    public void mostrarPanelDerechoClienteFavoritos() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaFiltrarCanciones.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaFiltrarCancionesController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.establecerListaCancionesFavoritas();
            controlador.setEsVentanaFavoritos(true);
            controlador.setCombosFavs();
            controlador.iniciarGridPane();
            controlador.iniciarCombos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la barra superior con la vista del cliente.
     */
    public void mostrarBarraSuperiorCliente(AdministradorComandos administrador) {
        try {
            barraSuperior.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/BarraUsuario.fxml"));
            Node node = loader.load();
            barraSuperior.getChildren().add(node);
            BarraUsuarioController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.cargarInfo();
            controlador.setAdministradorComandos(administrador);
            if (administrador != null) {
                controlador.actualizarBotones();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la barra superior con la vista del administrador.
     */
    public void mostrarBarraSuperiorAdmin(AdministradorComandos administrador) {
        try {
            barraSuperior.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/BarraUsuario.fxml"));
            Node node = loader.load();
            barraSuperior.getChildren().add(node);
            BarraUsuarioController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.cargarInfo();
            controlador.setAdministradorComandos(administrador);
            if (administrador != null) {
                controlador.actualizarBotones();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho con las canciones generales.
     */
    public void mostrarPanelDerechoCancionesGenerales() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaFiltrarCanciones.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaFiltrarCancionesController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.establecerListaCancionesGenerales();
            controlador.iniciarGridPane();
            controlador.iniciarCombos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Abre la ventana de detalles de una canción específica.
     */
    public void abrirVentanaDetalleCancion(Cancion cancion) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaCancionDetalle.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaCancionDetalleController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.setCancion(cancion);
            controlador.iniciarVideoDatos();
            controlador.permitirVolverAdmin();
            aplicacion.ventanaCancionDetalleController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana para filtrar artistas.
     */
    public void mostrarVentanaFiltrarArtistas() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaFiltrarArtistas.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaFiltrarArtistasController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.actualizarTabla();
            controlador.actualizarCombos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana para filtrar bandas o solistas.
     */
    public void mostrarVentanaBandas(Boolean esBanda) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaFiltrarArtistas.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaFiltrarArtistasController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.setEsVentanaBandaSolista(true);
            controlador.ordenarVentanaBanda(esBanda);
            controlador.actualizarTabla();
            controlador.actualizarCombos();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho con las canciones de un artista específico.
     */
    public void mostrarPanelDerechoCancionesArtista(Artista artistaElegido) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaFiltrarCanciones.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaFiltrarCancionesController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.establecerArbolPorArtista(artistaElegido);
            controlador.iniciarGridPane();
            controlador.iniciarCombos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho para crear o editar una canción.
     */
    public void mostrarPanelDerechoCrearEditarCancion(Cancion cancion) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaCrearEditarCancion.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaCrearEditarCancionController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.setCancion(cancion);
            controlador.iniciarDatosCrearEditar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho para crear o editar un artista.
     */
    public void mostrarPanelDerechoCrearEditarArtista(Artista artista) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaCrearEditarArtista.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaCrearEditarArtistaController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.setArtista(artista);
            controlador.iniciarDatosCrearEditar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho con las estadísticas.
     */
    public void mostrarPanelDerechoEstadisticas(Boolean empiezaGenero) {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaEstadisticas.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaEstadisticasController controlador = loader.getController();
            controlador.iniciarDatos();
            if (empiezaGenero) {
                controlador.actualizarChartsGeneros();
            } else {
                controlador.actualizarChartsArtistas();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra el panel derecho con la vista para gestionar archivos de texto.
     */
    public void mostrarPanelDerechoArchivoTextos() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaArchivos.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaArchivosController controlador = loader.getController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra la ventana del perfil del usuario.
     */
    public void mostrarVentanaPerfil() {
        try {
            panelDerecho.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/VentanaPerfilUsuario.fxml"));
            Node node = loader.load();
            panelDerecho.getChildren().add(node);
            VentanaPerfilUsuarioController controlador = loader.getController();
            controlador.setAplicacion(this.aplicacion);
            controlador.setUsuario(this.usuario);
            controlador.setearUsuario(this.usuario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
