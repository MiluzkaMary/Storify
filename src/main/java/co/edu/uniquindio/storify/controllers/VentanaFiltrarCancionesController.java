package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimpleCircular;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import co.edu.uniquindio.storify.exceptions.ArtistaNoEncontradoException;
import co.edu.uniquindio.storify.exceptions.AtributoVacioException;
import co.edu.uniquindio.storify.exceptions.UsuarioNoExistenteException;
import co.edu.uniquindio.storify.model.*;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de filtrado de canciones.
 * Esta clase maneja la lógica para filtrar y mostrar canciones según varios criterios.
 */
@Data
public class VentanaFiltrarCancionesController implements Initializable {

    @FXML
    private Text labelFiltrarCanciones;

    @FXML
    private Text lblAnio;

    @FXML
    private Text lblGenero;

    @FXML
    private Text lblDuracion;

    @FXML
    private TextField txtFieldNombreArtista;

    @FXML
    private ComboBox<String> comboOrden;

    @FXML
    private Pane paneOrdenar;

    @FXML
    private Text titleOrdenar;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ComboBox<String> comboDuracion;

    @FXML
    private ComboBox<String> comboGenero;

    @FXML
    private ComboBox<String> comboLanzamiento;

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private ListaEnlazadaSimpleCircular<Cancion> listaCancionesFavs = new ListaEnlazadaSimpleCircular<>();
    private ListaEnlazadaSimpleCircular<Cancion> listaCanciones = new ListaEnlazadaSimpleCircular<>();
    private BinaryTree<Artista> arbolArtistas = mfm.getTiendaMusica().getArtistas();
    private boolean esVentanaFavoritos = false;

    /**
     * Método inicializador de la interfaz.
     *
     * @param url La URL de ubicación.
     * @param resourceBundle El conjunto de recursos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboOrden.setVisible(false);
        paneOrdenar.setVisible(false);
        titleOrdenar.setVisible(false);
    }

    /**
     * Inicia el GridPane con las canciones filtradas.
     *
     * @throws ArtistaNoEncontradoException Si no se encuentra el artista.
     */
    public void iniciarGridPane() throws ArtistaNoEncontradoException {
        int column = 0;
        int row = 1;
        try {
            Node<Cancion> currentNode = listaCanciones.getHeadNode(); // Obtener el nodo de inicio de la lista
            if (currentNode != null) {
                do {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ventanas/ItemCancion.fxml"));
                    AnchorPane anchorPane = loader.load();

                    ItemCancionController controlador = loader.getController();
                    controlador.setAplicacion(this.aplicacion);
                    controlador.setUsuario(this.usuario);
                    controlador.cargarDatos(currentNode.getData()); // Establecer la canción en el nodo
                    controlador.setEsVentanaFavs(esVentanaFavoritos);
                    if (column == 3) {
                        column = 0;
                        row++;
                    }
                    grid.add(anchorPane, column++, row);

                    // Establecer tamaño del GridPane
                    grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                    grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    grid.setMaxWidth(Region.USE_PREF_SIZE);

                    grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                    grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    grid.setMaxHeight(Region.USE_PREF_SIZE);

                    GridPane.setMargin(anchorPane, new Insets(8, 8, 8, 8));

                    currentNode = currentNode.getNextNode(); // Avanzar al siguiente nodo
                } while (currentNode != null && currentNode != listaCanciones.getHeadNode()); // Seguir iterando hasta que se vuelva al nodo de inicio
            }

            scroll.setVvalue(0.05); // Para que no se vea el espacio en blanco de 2 cm entre el filtro y el panel
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inicia los ComboBox con las opciones disponibles.
     */
    public void iniciarCombos() {
        // Llenar combobox tipoGenero
        ObservableList<String> tipoGenero = FXCollections.observableArrayList();
        tipoGenero.addAll(mfm.getTiendaMusica().obtenerTipoGeneroCancionesSinRepetir());
        comboGenero.setItems(tipoGenero);

        // Llenar combobox lanzamientos
        ObservableList<String> listaLanzamientos = FXCollections.observableArrayList();
        listaLanzamientos.addAll(mfm.getTiendaMusica().obtenerAniosLanzamientoSinRepetir());
        comboLanzamiento.setItems(listaLanzamientos);

        // Llenar combobox duracion
        ObservableList<String> listaDuracion = FXCollections.observableArrayList();
        listaDuracion.addAll(mfm.getTiendaMusica().obtenerDuracionCancionesSinRepetir());
        comboDuracion.setItems(listaDuracion);

        //llenar combobox orden
        ObservableList<String> listaOrden=FXCollections.observableArrayList();
        listaOrden.addAll(mfm.getTiendaMusica().obtenerOrdenamientos());
        comboOrden.setItems(listaOrden);

    }

    /**
     * Filtra las canciones según los criterios máximos seleccionados en los ComboBox.
     *
     * @throws InterruptedException Si el hilo es interrumpido.
     * @throws ArtistaNoEncontradoException Si no se encuentra el artista.
     */
    public void filtrarMaximo() throws InterruptedException, ArtistaNoEncontradoException {
        grid.getChildren().clear();
        String genero = comboGenero.getValue();
        String lanzamiento = comboLanzamiento.getValue();
        String duracion = comboDuracion.getValue();

        if (genero == null && lanzamiento == null && duracion == null) { // Si ninguno fue seleccionado
            Alertas.mostrarMensaje("Entradas Erroneas", "Operación Incompleta", "¡Tiene que seleccionar al menos un criterio de filtro!", Alert.AlertType.WARNING);
        } else { // Al menos uno de los filtros fue modificado
            if (genero == null) {
                genero = "Vacio";
            }
            if (lanzamiento == null) {
                lanzamiento = "Vacio";
            }
            if (duracion == null) {
                duracion = "Vacio";
            }

            if (esVentanaFavoritos) {
                this.listaCanciones = mfm.getTiendaMusica().obtenerListaMaximoFiltroDeFavoritos(listaCancionesFavs, genero, lanzamiento, duracion);
            } else {
                this.listaCanciones = arbolArtistas.obtenerBusquedaTodosFiltros(genero, lanzamiento, duracion).convertirListaDobleASimpleCircular();
            }

            if (listaCanciones.size() == 0) {
                Alertas.mostrarMensaje("Filtro de canciones", "Resultados obtenidos", "No se encontraron canciones que cumplan con los criterios de búsqueda", Alert.AlertType.INFORMATION);
            } else {
                iniciarGridPane();
            }
        }
    }

    /**
     * Filtra las canciones según los criterios mínimos seleccionados en los ComboBox.
     *
     * @throws InterruptedException Si el hilo es interrumpido.
     * @throws ArtistaNoEncontradoException Si no se encuentra el artista.
     */
    public void filtrarMinimo() throws InterruptedException, ArtistaNoEncontradoException {
        grid.getChildren().clear();
        String genero = comboGenero.getValue();
        String lanzamiento = comboLanzamiento.getValue();
        String duracion = comboDuracion.getValue();

        if (genero == null && lanzamiento == null && duracion == null) { // Si ninguno fue seleccionado
            Alertas.mostrarMensaje("Entradas Erroneas", "Operación Incompleta", "¡Tiene que seleccionar al menos un criterio de filtro!", Alert.AlertType.WARNING);
        } else { // Al menos uno de los filtros fue modificado
            if (genero == null) {
                genero = "Vacio";
            }
            if (lanzamiento == null) {
                lanzamiento = "Vacio";
            }
            if (duracion == null) {
                duracion = "Vacio";
            }

            if (esVentanaFavoritos) {
                this.listaCanciones = mfm.getTiendaMusica().obtenerListaMinimoFiltroDeFavoritos(listaCancionesFavs, genero, lanzamiento, duracion);
            } else {
                this.listaCanciones = arbolArtistas.obtenerBusquedaAlMenosUnFiltro(genero, lanzamiento, duracion).convertirListaDobleASimpleCircular();
            }

            if (listaCanciones.size() == 0) {
                Alertas.mostrarMensaje("Filtro de canciones", "Resultados obtenidos", "No se encontraron canciones que cumplan con los criterios de búsqueda", Alert.AlertType.INFORMATION);
            } else {
                iniciarGridPane();
            }
        }
    }

    /**
     * Establece la lista de canciones favoritas del usuario.
     */
    public void establecerListaCancionesFavoritas() {
        Persona persona = usuario.getPersona();
        if (persona instanceof Cliente) {
            Cliente cliente = (Cliente) persona;
            listaCanciones = cliente.getCancionesFavoritas();
            listaCancionesFavs = cliente.getCancionesFavoritas();
        }
    }

    /**
     * Establece la lista de canciones generales.
     */
    public void establecerListaCancionesGenerales() {
        listaCanciones = mfm.getTiendaMusica().obtenerCancionesGenerales();
    }

    /**
     * Establece el árbol de canciones por artista.
     *
     * @param artista El artista para el cual se obtendrá el árbol de canciones.
     */
    public void establecerArbolPorArtista(Artista artista) {
        arbolArtistas = mfm.getTiendaMusica().obtenerArbolPorArtista(artista);
        listaCanciones = artista.getCanciones().convertirListaDobleASimpleCircular();
    }

    public void setCombosFavs(){
        comboOrden.setVisible(true);
        paneOrdenar.setVisible(true);
        titleOrdenar.setVisible(true);


    }

    /**
     * Busca un artista por su nombre y muestra las canciones asociadas a dicho artista.
     * Si no se encuentra ningún artista con el nombre proporcionado, se muestra un mensaje de error.
     */
    public void buscarNombreArtista(){
        try{
            if (txtFieldNombreArtista.getText()!=null){
                Artista artistaDigitado= mfm.getTiendaMusica().buscarArtistaPorNombre(txtFieldNombreArtista.getText());

                aplicacion.verCancionesDeArtista(artistaDigitado);
            }
        }
        catch (ArtistaNoEncontradoException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    /**
     * Ordena las canciones según el tipo de ordenamiento seleccionado en el combo box.
     * Los tipos de ordenamiento disponibles son: "Mas Recientes", "Mas antiguas", "Mayor duracion" y "Menor duracion".
     * Si no se selecciona ningún tipo de ordenamiento, se muestra un mensaje de error.
     *
     * @throws ArtistaNoEncontradoException Si ocurre un error al buscar un artista por su nombre.
     */
    public void ordenarCanciones() throws ArtistaNoEncontradoException {

        String orden=comboOrden.getSelectionModel().getSelectedItem();
        if (orden.equals("Mas Recientes")){
            ordenarRecienteAntiguoLista();
        }else if (orden.equals("Mas antiguas")){
            ordenarAntiguoRecienteLista();
        }else if(orden.equals("Mayor duracion")){
            ordenarMasLargo();
        }else if(orden.equals("Menor duracion")){
            ordenarMasCorto();
        }else{
            Alertas.mostrarMensaje("ERROR", "Entrada invalida", "Debe seleccionar una opción de ordenamiento", Alert.AlertType.ERROR);
        }

    }


    /**
     * Ordena las canciones de la lista de canciones favoritas por fecha de lanzamiento, de más reciente a más antigua,
     * y actualiza la lista de canciones para reflejar el nuevo orden.
     *
     * @throws ArtistaNoEncontradoException Si ocurre un error al buscar un artista por su nombre.
     */
    public void ordenarRecienteAntiguoLista() throws ArtistaNoEncontradoException {
        this.listaCanciones=mfm.getTiendaMusica().ordenarCancionesPorFechaMasReciente(listaCancionesFavs);
        iniciarGridPane();
    }


    /**
     * Ordena las canciones de la lista de canciones favoritas por fecha de lanzamiento, de más antigua a más reciente,
     * y actualiza la lista de canciones para reflejar el nuevo orden.
     *
     * @throws ArtistaNoEncontradoException Si ocurre un error al buscar un artista por su nombre.
     */
    public void ordenarAntiguoRecienteLista() throws ArtistaNoEncontradoException {
        this.listaCanciones=mfm.getTiendaMusica().ordenarCancionesPorFechaMasAntigua(listaCancionesFavs);
        iniciarGridPane();
    }


    /**
     * Ordena las canciones de la lista de canciones favoritas por duración, de más larga a más corta,
     * y actualiza la lista de canciones para reflejar el nuevo orden.
     *
     * @throws ArtistaNoEncontradoException Si ocurre un error al buscar un artista por su nombre.
     */
    public void ordenarMasLargo() throws ArtistaNoEncontradoException {
        this.listaCanciones=mfm.getTiendaMusica().ordenarCancionesPorDuracionMasLarga(listaCancionesFavs);
        iniciarGridPane();
    }

    /**
     * Ordena las canciones de la lista de canciones favoritas por duración, de más corta a más larga,
     * y actualiza la lista de canciones para reflejar el nuevo orden.
     *
     * @throws ArtistaNoEncontradoException Si ocurre un error al buscar un artista por su nombre.
     */
    public void ordenarMasCorto() throws ArtistaNoEncontradoException {
        this.listaCanciones=mfm.getTiendaMusica().ordenarCancionesPorDuracionMasCorta(listaCancionesFavs);
        iniciarGridPane();
    }


}
