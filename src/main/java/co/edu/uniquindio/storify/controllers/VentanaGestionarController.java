package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorListeners.MyListenerArtista;
import co.edu.uniquindio.storify.controllers.controladorListeners.MyListenerCancion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimpleCircular;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.exceptions.EmptyNodeException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de gestión de canciones y artistas.
 * Esta clase maneja la lógica para gestionar (crear, editar, eliminar) canciones y artistas.
 */
@Data
public class VentanaGestionarController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private ListaEnlazadaSimpleCircular<Cancion> listaCanciones = new ListaEnlazadaSimpleCircular<>();
    BinaryTree<Artista> listaArtistas = mfm.getTiendaMusica().getArtistas();
    private Boolean esGestionCanciones;
    private Boolean esGestionArtistas;

    // Variables para almacenar los objetos seleccionados en la gestión
    public Cancion cancionElegida;
    public Artista artistaElegido;

    // Listeners
    private MyListenerCancion myListenerCancion;
    private MyListenerArtista myListenerArtista;

    @FXML
    private Button btnDetalles;

    @FXML
    private Text txtAdvertencia;

    @FXML
    private Button btnEliminar;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scrollGrid;

    @FXML
    private ScrollPane scrollTabla;

    @FXML
    private Button btnCrear;

    @FXML
    private Text txtTituloGestion;

    @FXML
    private Button btnEditar;

    @FXML
    private TableView<Artista> tablaArtistas;

    @FXML
    private TableColumn<Artista, String> columnaTipoArtista;

    @FXML
    private TableColumn<Artista, String> columnaCodigo;

    @FXML
    private TableColumn<Artista, String> columnaNombre;

    @FXML
    private TableColumn<Artista, String> columnaNacionalidad;

    @FXML
    private TableColumn<Artista, String> columnaNumCanciones;

    /**
     * Inicializa los componentes de la interfaz.
     *
     * @param url            la URL de inicialización
     * @param resourceBundle el conjunto de recursos
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializan ambas, scroll y tabla, como invisibles
        tablaArtistas.setVisible(false);
        grid.setVisible(false);
        scrollGrid.setVisible(false);
        scrollTabla.setVisible(false);
    }

    /**
     * Inicia la gestión de canciones, configurando la vista y cargando las canciones.
     */
    public void iniciarGestionCanciones() {
        esGestionCanciones = true;
        esGestionArtistas = false;
        txtAdvertencia.setText("Antes de elegir una opción de gestión, tenga en cuenta que debe seleccionar una canción");
        txtTituloGestion.setText("Gestión de Canciones");
        iniciarScrollCanciones();
        grid.setVisible(true);
        scrollGrid.setVisible(true);
        tablaArtistas.setVisible(false);
        scrollTabla.setVisible(false);
    }

    /**
     * Inicia la tabla de artistas, configurando las columnas y cargando los artistas.
     */
    public void iniciarTablaArtistas() {
        establecerListaArtistasGenerales();
        tablaArtistas.getItems().clear();
        ObservableList<Artista> listaArtistasProperty = FXCollections.observableArrayList();
        // Asignar las propiedades de las columnas
        columnaTipoArtista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerTipoArtistaString()));
        columnaCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnaNacionalidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidad()));
        columnaNumCanciones.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().obtenerCantidadCanciones())));

        listaArtistasProperty.clear();
        listaArtistasProperty.addAll(mfm.getTiendaMusica().convertirArbolALista(listaArtistas));
        tablaArtistas.setItems(listaArtistasProperty);
    }

    /**
     * Inicia la gestión de artistas, configurando la vista y cargando los artistas.
     */
    public void iniciarGestionArtistas() {
        esGestionArtistas = true;
        esGestionCanciones = false;
        txtAdvertencia.setText("Antes de elegir una opción de gestión, tenga en cuenta que debe seleccionar un artista");
        txtTituloGestion.setText("Gestión de Artistas");
        grid.setVisible(false);
        scrollGrid.setVisible(false);
        tablaArtistas.setVisible(true);
        scrollTabla.setVisible(true);
        iniciarTablaArtistas();
    }

    /**
     * Inicia el scroll de canciones, configurando la vista y cargando las canciones.
     */
    public void iniciarScrollCanciones() {
        establecerListaCancionesGenerales();
        if (listaCanciones.size() > 0) {
            myListenerCancion = cancion -> setCancionElegida(cancion);
        }

        int column = 0;
        int row = 1;
        try {
            Node<Cancion> currentNode = listaCanciones.getHeadNode(); // Obtener el nodo de inicio de la lista
            if (currentNode != null) {
                do {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ItemCancion.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ItemCancionController controlador = loader.getController();
                    controlador.setEsGestion(true);
                    controlador.setMyListenerCancion(myListenerCancion);
                    controlador.setAplicacion(this.aplicacion);
                    controlador.setUsuario(this.usuario);
                    controlador.cargarDatos(currentNode.getData());

                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                    grid.add(anchorPane, column++, row);

                    // Ajustar el ancho del grid
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);
                    // Ajustar la altura del grid
                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(8, 8, 8, 8));

                    currentNode = currentNode.getNextNode(); // Avanzar al siguiente nodo
                } while (currentNode != null && currentNode != listaCanciones.getHeadNode()); // Seguir iterando hasta que se vuelva al nodo de inicio
            }

            scrollGrid.setVvalue(0.05); // Para que no se vea el espacio en blanco de 2 cm entre el filtro y el panel
        } catch (IOException | ArtistaNoEncontradoException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establece la lista de canciones generales.
     */
    public void establecerListaCancionesGenerales() {
        listaCanciones = mfm.getTiendaMusica().obtenerCancionesGenerales();
    }

    /**
     * Establece la lista de artistas generales.
     */
    public void establecerListaArtistasGenerales() {
        listaArtistas = mfm.getTiendaMusica().getArtistas();
    }

    /**
     * Abre la ventana de creación o edición según el tipo de gestión.
     */
    public void crear() {
        if (esGestionCanciones) {
            Cancion cancionVacia = null;
            aplicacion.mostrarVentanaCrearEditarCancion(cancionVacia);
        } else if (esGestionArtistas) {
            Artista artistaVacio = null;
            aplicacion.mostrarVentanaCrearEditarArtista(artistaVacio);
        }
    }

    /**
     * Elimina la canción o el artista seleccionado.
     *
     * @throws EmptyNodeException si no se puede eliminar el nodo vacío.
     */
    public void eliminar() throws EmptyNodeException {
        if (esGestionCanciones) {
            if (cancionElegida != null) {
                if (confirmarEliminacion("la canción")) {
                    mfm.getTiendaMusica().eliminarCancion(cancionElegida);
                    mfm.guardarDatosBinario();
                    iniciarScrollCanciones();
                    cancionElegida = null;
                }
            } else {
                Alertas.mostrarMensaje("Error", "Entrada no valida", "Por favor seleccione una canción primero", Alert.AlertType.ERROR);
            }
        } else if (esGestionArtistas) {
            artistaElegido = tablaArtistas.getSelectionModel().getSelectedItem();

            if (artistaElegido != null) {
                if (confirmarEliminacion("el artista")) {
                    mfm.getTiendaMusica().eliminarArtistaArbol(artistaElegido);
                    mfm.guardarDatosBinario();
                    iniciarTablaArtistas();
                    artistaElegido = null;
                }
            } else {
                Alertas.mostrarMensaje("Error", "Entrada no valida", "Por favor seleccione un artista primero", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Confirma la eliminación del objeto seleccionado.
     *
     * @param objeto el objeto a eliminar
     * @return true si el usuario confirma, false si cancela
     */
    public boolean confirmarEliminacion(String objeto) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Está seguro de eliminar a " + objeto + " seleccionado/a?");
        alerta.setContentText("Esta acción no se puede deshacer.");

        // Configurar los botones del cuadro de diálogo
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(botonSi, botonCancelar);

        // Mostrar el cuadro de diálogo y esperar la respuesta del usuario
        Optional<ButtonType> resultado = alerta.showAndWait();

        // Devolver true si el usuario ha confirmado, false si ha cancelado
        return resultado.isPresent() && resultado.get() == botonSi;
    }

    /**
     * Confirma la acción de mostrar detalles.
     *
     * @return true si el usuario confirma, false si cancela
     */
    public boolean confirmarDetalle() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Detalle");
        alerta.setHeaderText("Esta acción solo le mostrará las canciones del artista elegido");
        alerta.setContentText("Todos los detalles del artista están en la tabla");

        // Configurar los botones del cuadro de diálogo
        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(botonSi, botonCancelar);

        // Mostrar el cuadro de diálogo y esperar la respuesta del usuario
        Optional<ButtonType> resultado = alerta.showAndWait();

        // Devolver true si el usuario ha confirmado, false si ha cancelado
        return resultado.isPresent() && resultado.get() == botonSi;
    }

    /**
     * Abre la ventana de edición según el tipo de gestión.
     */
    public void editar() {
        if (esGestionCanciones) {
            if (cancionElegida != null) {
                aplicacion.mostrarVentanaCrearEditarCancion(cancionElegida);
            } else {
                Alertas.mostrarMensaje(
                        "Error", "Entrada no valida", "Por favor seleccione una canción primero", Alert.AlertType.ERROR);
            }
        } else if (esGestionArtistas) {
            artistaElegido = tablaArtistas.getSelectionModel().getSelectedItem();
            if (artistaElegido != null) {
                aplicacion.mostrarVentanaCrearEditarArtista(artistaElegido);
            } else {
                Alertas.mostrarMensaje("Error", "Entrada no valida", "Por favor seleccione un artista primero", Alert.AlertType.ERROR);
            }
        }
    }

    /**
     * Muestra los detalles de la canción o el artista seleccionado.
     */
    public void detalles() {
        if (esGestionCanciones) {
            if (cancionElegida != null) {
                aplicacion.abrirDetalleCancion(cancionElegida);
            } else {
                Alertas.mostrarMensaje("Error", "Entrada no valida", "Por favor seleccione una canción primero", Alert.AlertType.ERROR);
            }
        } else if (esGestionArtistas) {
            artistaElegido = tablaArtistas.getSelectionModel().getSelectedItem();
            if (artistaElegido != null) {
                if (confirmarDetalle()) {
                    aplicacion.verCancionesDeArtista(artistaElegido); // Colocar botón volver en esa ventana, if cliente null
                }
            } else {
                Alertas.mostrarMensaje("Error", "Entrada no valida", "Por favor seleccione un artista primero", Alert.AlertType.ERROR);
            }
        }
    }
}
