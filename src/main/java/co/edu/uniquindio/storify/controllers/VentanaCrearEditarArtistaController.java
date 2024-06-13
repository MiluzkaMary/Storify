package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaDoble;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.exceptions.ArtistasYaEnTiendaException;
import co.edu.uniquindio.storify.exceptions.AtributoVacioException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de creación y edición de artistas.
 * Esta clase maneja la lógica para crear nuevos artistas, editar los existentes
 * y gestionar las canciones asociadas a un artista.
 */
@Data
public class VentanaCrearEditarArtistaController implements Initializable {

    @FXML
    private TextField txtNombreArtista;

    @FXML
    private ComboBox<String> comboNacionalidad;

    @FXML
    private TextField txtCodigo;

    @FXML
    private Button btnCrear;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<Cancion, String> columnaGenero;

    @FXML
    private TableView<Cancion> tablaCancionesArtista;

    @FXML
    private TableColumn<Cancion, String> columnaCodigo;

    @FXML
    private TableColumn<Cancion, String> columnaNombre;

    @FXML
    private TableColumn<Cancion, String> columnaAlbum;

    @FXML
    private ComboBox<String> comboTipo;

    @FXML
    private TableColumn<Cancion, String> columnaDuracion;

    @FXML
    public Button btnCargarArtista;

    private String rutaArchivo;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private Artista artista;
    private ListaEnlazadaDoble<Cancion> listaCancionesArtista = new ListaEnlazadaDoble<>();
    private ListaEnlazadaDoble<Cancion> listaCancionesModificada;
    private ListaEnlazadaDoble<Cancion> listaCancionesEliminadas = new ListaEnlazadaDoble<>();

    /**
     * Inicializa los componentes de la interfaz.
     *
     * @param url La URL de ubicación.
     * @param resourceBundle El conjunto de recursos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Método inicial vacío
    }

    /**
     * Inicializa los datos para la creación o edición de un artista.
     * Configura los campos y la tabla según si se está creando o editando un artista.
     */
    public void iniciarDatosCrearEditar() {
        iniciarCombos();
        if (artista != null) {
            iniciarCancionesDeArtista();
            iniciarTabla();
            txtNombreArtista.setText(artista.getNombre());
            txtCodigo.setText(artista.getCodigo());
            comboNacionalidad.setValue(artista.getNacionalidad());
            comboTipo.setValue(artista.obtenerTipoArtistaString());
            btnCrear.setVisible(false);
            btnGuardar.setVisible(true);
        } else {
            btnCrear.setVisible(true);
            btnGuardar.setVisible(false);
        }
    }

    /**
     * Inicializa la tabla de canciones del artista con los datos actuales.
     */
    public void iniciarTabla() {
        tablaCancionesArtista.getItems().clear();
        ObservableList<Cancion> listaCancionesProperty = FXCollections.observableArrayList();
        columnaCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnaAlbum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
        columnaDuracion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracion()));
        columnaGenero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerGeneroComoString()));

        listaCancionesProperty.addAll(mfm.getTiendaMusica().convertirAArrayList(listaCancionesArtista));
        tablaCancionesArtista.setItems(listaCancionesProperty);
    }

    /**
     * Inicializa la tabla de canciones del artista con una lista específica de canciones.
     *
     * @param listaCanciones La lista de canciones a mostrar en la tabla.
     */
    public void iniciarTabla(ListaEnlazadaDoble<Cancion> listaCanciones) {
        tablaCancionesArtista.getItems().clear();
        ObservableList<Cancion> listaCancionesProperty = FXCollections.observableArrayList();
        columnaCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnaAlbum.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlbum()));
        columnaDuracion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuracion()));
        columnaGenero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerGeneroComoString()));

        listaCancionesProperty.addAll(mfm.getTiendaMusica().convertirAArrayList(listaCanciones));
        tablaCancionesArtista.setItems(listaCancionesProperty);
    }

    /**
     * Inicializa las canciones asociadas al artista para la edición.
     */
    public void iniciarCancionesDeArtista() {
        this.listaCancionesArtista = artista.getCanciones();
        this.listaCancionesModificada = new ListaEnlazadaDoble<>(listaCancionesArtista);
    }

    /**
     * Inicializa los combos de nacionalidad y tipo de artista.
     */
    public void iniciarCombos() {
        ObservableList<String> tipoNaciones = FXCollections.observableArrayList();
        tipoNaciones.addAll(mfm.getTiendaMusica().obtenerNombresPaises());
        comboNacionalidad.setItems(tipoNaciones);

        ObservableList<String> tiposArtistas = FXCollections.observableArrayList();
        tiposArtistas.add("SOLISTA");
        tiposArtistas.add("BANDA");
        comboTipo.setItems(tiposArtistas);
    }

    /**
     * Elimina una canción seleccionada de la lista de canciones del artista.
     * Si la eliminación es confirmada, la canción es removida de la lista.
     */
    public void eliminarCancion() {
        Cancion cancionEliminar = tablaCancionesArtista.getSelectionModel().getSelectedItem();
        if (cancionEliminar != null) {
            if (confirmarEliminacion(cancionEliminar)) {
                listaCancionesModificada.removeData(cancionEliminar);
                listaCancionesEliminadas.add(cancionEliminar);
                iniciarTabla(listaCancionesModificada);
            }
        } else {
            Alertas.mostrarMensaje("Error", "Entrada no válida", "Debe elegir una canción", Alert.AlertType.ERROR);
        }
    }

    /**
     * Confirma la eliminación de una canción mostrando un cuadro de diálogo de confirmación.
     *
     * @param cancionElim La canción a eliminar.
     * @return true si la eliminación es confirmada, false en caso contrario.
     */
    public boolean confirmarEliminacion(Cancion cancionElim) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar Eliminación");
        alerta.setHeaderText("¿Está seguro de eliminar la canción: " + cancionElim.getNombre() + " ?");
        alerta.setContentText("Esta acción no se puede deshacer.");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(botonSi, botonCancelar);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == botonSi;
    }

    /**
     * Crea un nuevo artista con los datos proporcionados en el formulario.
     * Muestra un mensaje de confirmación o error según el resultado de la operación.
     */
    public void crearArtista() {
        try {
            Artista artistaNuevo = mfm.getTiendaMusica().crearArtista(
                    txtNombreArtista.getText(),
                    txtCodigo.getText(),
                    comboNacionalidad.getValue(),
                    comboTipo.getValue()
            );
            mfm.guardarDatosBinario();
            mfm.getTiendaMusica().agregarArtista(artistaNuevo);
            Alertas.mostrarMensaje("Registro Confirmado", "Operación completada", "Se ha registrado correctamente el artista: " + artistaNuevo.getNombre(), Alert.AlertType.INFORMATION);
            aplicacion.motrarVentanaGestionArtista();
        } catch (AtributoVacioException | ArtistasYaEnTiendaException | InterruptedException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Guarda los cambios realizados en un artista existente.
     * Actualiza los datos del artista y elimina las canciones marcadas para eliminación.
     * Muestra un mensaje de confirmación o error según el resultado de la operación.
     */
    public void guardarCambios() {
        try {
            Artista artistaEditado = mfm.getTiendaMusica().editarArtista(
                    this.artista,
                    txtNombreArtista.getText(),
                    txtCodigo.getText(),
                    comboNacionalidad.getValue(),
                    comboTipo.getValue(),
                    listaCancionesModificada
            );

            for (Cancion cancion : listaCancionesEliminadas) {
                mfm.getTiendaMusica().eliminarCancionUsuario(cancion);
            }

            mfm.guardarDatosBinario();
            Alertas.mostrarMensaje("Edición Confirmada", "Operación completada", "Se ha editado correctamente el artista: " + artistaEditado.getNombre(), Alert.AlertType.INFORMATION);
            aplicacion.motrarVentanaGestionArtista();
        } catch (AtributoVacioException | ArtistaNoEncontradoException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Vuelve a la ventana de gestión de artistas.
     */
    public void volver() {
        aplicacion.motrarVentanaGestionArtista();
    }

    /**
     * Carga artistas desde un archivo de texto seleccionado por el usuario.
     * Muestra un cuadro de diálogo de selección de archivo y carga los datos.
     *
     * @param actionEvent El evento de acción.
     */
    public void cargarArtista(ActionEvent actionEvent) throws ArtistasYaEnTiendaException, IOException, InterruptedException {
        try {
            openFileChooser();
            mfm.getTiendaMusica().cargarArtistasDesdeArchivo(rutaArchivo);
            mfm.guardarDatosBinario();
            Alertas.mostrarMensaje("Carga de Artistas", "Artistas cargados con éxito", "Se han cargado correctamente los artistas desde el archivo " + rutaArchivo, Alert.AlertType.INFORMATION);
            aplicacion.motrarVentanaGestionArtista();
        } catch (FileNotFoundException e) {
            Alertas.mostrarMensaje("Error", "Archivo no encontrado", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Abre un cuadro de diálogo para seleccionar un archivo de texto.
     * Almacena la ruta del archivo seleccionado.
     */
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de texto");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de texto (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(ventana);

        if (selectedFile != null) {
            rutaArchivo = selectedFile.getAbsolutePath();
        }
    }
}
