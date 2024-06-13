package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.model.Artista;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de estadísticas.
 * Esta clase maneja la lógica para mostrar estadísticas de géneros musicales y artistas.
 */
public class VentanaEstadisticasController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private BinaryTree<Artista> artistas = mfm.getTiendaMusica().getArtistas();

    @FXML
    private NumberAxis yAxis;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private BarChart<String, Double> chart;

    @FXML
    private PieChart pieChart;

    @FXML
    private Text txtTituloEstadistica;

    @FXML
    private ComboBox<String> comboEstadistica;

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
     * Inicializa los datos para la ventana de estadísticas.
     * Configura el combo box con las opciones de estadísticas.
     */
    public void iniciarDatos() {
        ObservableList<String> opciones = FXCollections.observableArrayList(
                "GENEROS",
                "ARTISTAS"
        );
        comboEstadistica.setItems(opciones);
    }

    /**
     * Analiza la opción seleccionada en el combo box y muestra la estadística correspondiente.
     */
    public void analizar() {
        String chosen = comboEstadistica.getSelectionModel().getSelectedItem();
        if (chosen != null && !chosen.isBlank()) {
            if (chosen.equals("GENEROS")) {
                aplicacion.mostrarVentanaEstadisticas(true);
            } else if (chosen.equals("ARTISTAS")) {
                aplicacion.mostrarVentanaEstadisticas(false);
            }
        }
    }

    /**
     * Actualiza los gráficos con las estadísticas de géneros musicales.
     */
    public void actualizarChartsGeneros() {
        comboEstadistica.setValue("GENEROS");
        xAxis.setLabel("Géneros");
        yAxis.setLabel("Cantidad de Canciones por Género");
        txtTituloEstadistica.setText("Estadísticas de Géneros Musicales");

        Map<String, Integer> conteoPorGenero = mfm.getTiendaMusica().contarCancionesPorGenero();

        XYChart.Series<String, Double> barSeries = new XYChart.Series<>();
        barSeries.setName("Canciones por Género");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : conteoPorGenero.entrySet()) {
            String genero = entry.getKey();
            Double cantidad = entry.getValue().doubleValue();

            barSeries.getData().add(new XYChart.Data<>(genero, cantidad));
            pieChartData.add(new PieChart.Data(genero, cantidad));
        }

        chart.getData().clear();
        chart.getData().add(barSeries);

        pieChart.setData(pieChartData);
    }

    /**
     * Actualiza los gráficos con las estadísticas de vistas por artista.
     *
     * @throws GeneralSecurityException Si ocurre un problema de seguridad.
     * @throws IOException              Si ocurre un problema de entrada/salida.
     */
    public void actualizarChartsArtistas() throws GeneralSecurityException, IOException {
        comboEstadistica.setValue("ARTISTAS");

        xAxis.setLabel("Artistas");
        yAxis.setLabel("Cantidad De Vistas Totales");
        txtTituloEstadistica.setText("Estadísticas de Número de Vistas Artistas");

        Map<String, Double> conteoPorArtistas = mfm.getTiendaMusica().contarVistasPorArtista();

        XYChart.Series<String, Double> barSeries = new XYChart.Series<>();
        barSeries.setName("Vistas Por Artista");

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Double> entry : conteoPorArtistas.entrySet()) {
            String artista = entry.getKey();
            Double vistas = entry.getValue();

            barSeries.getData().add(new XYChart.Data<>(artista, vistas));
            pieChartData.add(new PieChart.Data(artista, vistas));
        }

        chart.getData().clear();
        chart.getData().add(barSeries);

        pieChart.setData(pieChartData);
    }
}
