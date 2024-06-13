package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de filtrado de artistas.
 * Esta clase maneja la lógica para filtrar y mostrar artistas según varios criterios.
 */
@Data
public class VentanaFiltrarArtistasController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private BinaryTree<Artista> artistas = mfm.getTiendaMusica().getArtistas();
    private Cancion cancion;
    private boolean esVentanaBandaSolista = false;
    private boolean esBanda = false;

    @FXML
    private Text txtTipoArtista;

    @FXML
    private Pane btnFiltrarMax;

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

    @FXML
    private ComboBox<String> comboTipoArtista;

    @FXML
    private ComboBox<String> comboNacionalidad;

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
     * Actualiza la tabla de artistas con los datos filtrados.
     */
    public void actualizarTabla() {
        tablaArtistas.getItems().clear();
        ObservableList<Artista> listaArtistasProperty = FXCollections.observableArrayList();
        // Asignar las propiedades de las columnas
        columnaTipoArtista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().obtenerTipoArtistaString()));
        columnaCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        columnaNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        columnaNacionalidad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidad()));
        columnaNumCanciones.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().obtenerCantidadCanciones())));

        listaArtistasProperty.clear();
        listaArtistasProperty.addAll(mfm.getTiendaMusica().convertirArbolALista(artistas));
        tablaArtistas.setItems(listaArtistasProperty);
    }

    /**
     * Filtra los artistas según los criterios mínimos seleccionados en los ComboBox.
     */
    public void filtrarMinimo() {
        String minimoNacionalidad = comboNacionalidad.getValue();
        String minimoTipoArtista = comboTipoArtista.getValue();
        if (!esVentanaBandaSolista) { // ventana normal
            BinaryTree<Artista> artistasArbolMin = mfm.getTiendaMusica().obtenerMinimoFiltroArtistas(minimoNacionalidad, minimoTipoArtista);
            this.artistas = artistasArbolMin;
            actualizarTabla();
        } else { // ventana solista o banda
            if (esBanda) { // en caso de que la ventana filtro artista sea solo banda
                minimoTipoArtista = "BANDA";
            } else {
                minimoTipoArtista = "SOLISTA";
            }
            BinaryTree<Artista> artistasArbolMin = mfm.getTiendaMusica().obtenerMaximoFiltroArtistas(minimoNacionalidad, minimoTipoArtista);
            this.artistas = artistasArbolMin;
            actualizarTabla();
        }
    }

    /**
     * Filtra los artistas según los criterios máximos seleccionados en los ComboBox.
     */
    public void filtrarMaximo() {
        String minimoNacionalidad = comboNacionalidad.getValue();
        String minimoTipoArtista = comboTipoArtista.getValue();

        BinaryTree<Artista> artistasArbolMax = mfm.getTiendaMusica().obtenerMaximoFiltroArtistas(minimoNacionalidad, minimoTipoArtista);
        this.artistas = artistasArbolMax;
        actualizarTabla();
    }

    /**
     * Actualiza los ComboBox con las opciones de nacionalidad y tipo de artista.
     */
    public void actualizarCombos() {
        // Obtener nacionalidades sin repetir
        ObservableList<String> tipoNacionalidad = FXCollections.observableArrayList();
        tipoNacionalidad.addAll(mfm.getTiendaMusica().obtenerNacionalidadesSinRepetir());
        comboNacionalidad.setItems(tipoNacionalidad);

        // Llenar combobox con tipos de artistas
        ObservableList<String> tiposArtistas = FXCollections.observableArrayList();
        tiposArtistas.add("SOLISTA");
        tiposArtistas.add("BANDA");
        comboTipoArtista.setItems(tiposArtistas);
    }

    /**
     * Ordena la ventana para mostrar solo bandas o solistas.
     *
     * @param esBanda Si es true, muestra solo bandas; si es false, muestra solo solistas.
     */
    public void ordenarVentanaBanda(boolean esBanda) {
        comboTipoArtista.setVisible(false);
        txtTipoArtista.setVisible(false);
        btnFiltrarMax.setVisible(false);
        if (esBanda) {
            setEsBanda(true);
            BinaryTree<Artista> artistasArbolMax = mfm.getTiendaMusica().obtenerMinimoFiltroArtistas(null, "BANDA");
            this.artistas = artistasArbolMax;
        } else {
            setEsBanda(false);
            BinaryTree<Artista> artistasArbolMax = mfm.getTiendaMusica().obtenerMinimoFiltroArtistas(null, "SOLISTA");
            this.artistas = artistasArbolMax;
        }
    }

    /**
     * Muestra las canciones del artista seleccionado.
     */
    public void verCanciones() {
        Artista artistaElegido = tablaArtistas.getSelectionModel().getSelectedItem();
        aplicacion.verCancionesDeArtista(artistaElegido);
    }
}
