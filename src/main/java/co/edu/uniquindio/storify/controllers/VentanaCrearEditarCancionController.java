package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.exceptions.AtributoVacioException;
import co.edu.uniquindio.storify.exceptions.CancionYaRegistradaException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de creación y edición de canciones.
 * Esta clase maneja la lógica para crear nuevas canciones, editar las existentes
 * y gestionar las relaciones con los artistas.
 */
@Data
public class VentanaCrearEditarCancionController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private BinaryTree<Artista> artistas = mfm.getTiendaMusica().getArtistas();
    private Usuario usuario;
    private Cancion cancion;
    private String caratulaElegida;
    private Artista artistaElegido;

    @FXML
    private TableView<Artista> tablaAutores;

    @FXML
    private TableColumn<Artista, String> columnaTipo;

    @FXML
    private TableColumn<Artista, String> columnaCodigo;

    @FXML
    private TableColumn<Artista, String> columnaNombre;

    @FXML
    private TableColumn<Artista, String> columnaNacionalidad;

    @FXML
    private TextField txtDuracion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtAlbum;

    @FXML
    private TextField txtAnio;

    @FXML
    private TextField txtYoutube;

    @FXML
    private Button subirCaratula;

    @FXML
    private ComboBox<String> comboGenero;

    @FXML
    private ImageView fotoCaratula;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCrear;

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
     * Inicializa los datos para la creación o edición de una canción.
     * Configura los campos y la tabla según si se está creando o editando una canción.
     */
    public void iniciarDatosCrearEditar() {
        actualizarTabla();
        if (cancion != null) { // Si la canción existe, se trata de una edición
            txtNombre.setText(cancion.getNombre());
            txtAlbum.setText(cancion.getAlbum());
            txtAnio.setText(String.valueOf(cancion.getAnioLanzamiento()));
            txtDuracion.setText(cancion.getDuracion());
            txtYoutube.setText(cancion.getUrlYoutube());
            actualizarCombo();
            comboGenero.setValue(cancion.obtenerGeneroComoString());
            caratulaElegida = cancion.getCaratula();

            try {
                Artista artistaCancion = mfm.getTiendaMusica().buscarArtistaCancion(cancion);
                tablaAutores.getSelectionModel().select(artistaCancion);
                Image image = new Image(getClass().getResourceAsStream(cancion.getCaratula()));
                fotoCaratula.setImage(image);
            } catch (Exception e) {
                if (!cancion.getCaratula().equals("")) {
                    Image image = new Image(cancion.getCaratula());
                    fotoCaratula.setImage(image);
                }
            }
        } else { // Si no, se trata de una creación de canción
            btnCrear.setVisible(true);
            btnGuardar.setVisible(false);
            actualizarCombo();
            Image image = new Image(getClass().getResourceAsStream("/imagenes/Banda Icon.png"));
            fotoCaratula.setImage(image);
        }
    }

    /**
     * Actualiza el combo de géneros musicales.
     */
    public void actualizarCombo() {
        ObservableList<String> tipoGeneroObservable = FXCollections.observableArrayList();
        tipoGeneroObservable.addAll("Rock", "Pop", "Salsa", "Bachata", "Punk", "Reggaeton", "Electronica", "R&B", "Otro");
        comboGenero.setItems(tipoGeneroObservable);
    }

    /**
     * Guarda los cambios realizados en una canción existente.
     */
    public void guardar() {
        try {
            Cancion cancionEditada = mfm.getTiendaMusica().editarCancion(
                    this.cancion,
                    txtNombre.getText(),
                    txtAlbum.getText(),
                    caratulaElegida,
                    txtAnio.getText(),
                    txtDuracion.getText(),
                    comboGenero.getValue(),
                    txtYoutube.getText()
            );
            mfm.guardarDatosBinario();
            Alertas.mostrarMensaje("Edición Confirmada", "Operación completada", "Se ha editado correctamente la canción: " + cancionEditada.getNombre(), Alert.AlertType.INFORMATION);
            aplicacion.motrarVentanaGestionCanciones();
        } catch (AtributoVacioException | ArtistaNoEncontradoException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Crea una nueva canción con los datos proporcionados en el formulario.
     */
    public void crear() {
        try {
            Cancion cancionNueva = mfm.getTiendaMusica().crearCancion(
                    txtNombre.getText(),
                    txtAlbum.getText(),
                    caratulaElegida,
                    txtAnio.getText(),
                    txtDuracion.getText(),
                    comboGenero.getValue(),
                    txtYoutube.getText()
            );
            Artista artistaElegido = tablaAutores.getSelectionModel().getSelectedItem();
            mfm.getTiendaMusica().agregarCancion(cancionNueva, artistaElegido);
            mfm.guardarDatosBinario();
            Alertas.mostrarMensaje("Registro Confirmado", "Operación completada", "Se ha registrado correctamente la canción: " + cancionNueva.getNombre(), Alert.AlertType.INFORMATION);
            aplicacion.motrarVentanaGestionCanciones();
        } catch (AtributoVacioException | CancionYaRegistradaException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Vuelve a la ventana de gestión de canciones.
     */
    public void volver() {
        aplicacion.motrarVentanaGestionCanciones();
    }

    /**
     * Actualiza la tabla de artistas con los datos actuales.
     */
    public void actualizarTabla() {
        tablaAutores.getItems().clear();
        ObservableList<Artista> listaArtistasProperty = FXCollections.observableArrayList();
        columnaTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerTipoArtistaString()));
        columnaCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnaNacionalidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidad()));

        listaArtistasProperty.clear();
        listaArtistasProperty.addAll(mfm.getTiendaMusica().convertirArbolALista(artistas));
        tablaAutores.setItems(listaArtistasProperty);
    }

    /**
     * Abre un cuadro de diálogo para seleccionar una imagen y la asigna como carátula de la canción.
     */
    public void subirFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.jpg", "*.png", "*.jpeg"));

        // Mostrar el cuadro de diálogo para seleccionar un archivo
        File selectedFile = fileChooser.showOpenDialog(ventana);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            fotoCaratula.setImage(image);
            this.caratulaElegida = fotoCaratula.getImage().getUrl();
        } else {
            // Restaurar la imagen a null si no se selecciona ninguna
            Image image = new Image("/imagenes/user.png");
            fotoCaratula.setImage(image);
            this.caratulaElegida = fotoCaratula.getImage().getUrl();
        }
    }
}
