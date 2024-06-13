package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.exceptions.ArtistasYaEnTiendaException;
import co.edu.uniquindio.storify.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controlador para la ventana de gestión de archivos en la aplicación.
 * Permite cargar archivos de texto y visualizar su contenido, así como guardar cambios.
 */
@Data
public class VentanaArchivosController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private boolean archivoCargado = false;
    private File archivoSeleccionado;

    @FXML
    private TextArea textArea;

    @FXML
    private Button btnCargar;

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
     * Maneja la carga y guarda de archivos de texto.
     * Si un archivo no ha sido cargado, se abre un selector de archivos para elegir uno y mostrar su contenido.
     * Si un archivo ya ha sido cargado, se guarda su contenido en la tienda de música.
     */
    public void cargarArchivo() {
        if (!archivoCargado) {
            FileChooser fileChooser = new FileChooser();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("File Viewer");

            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                archivoSeleccionado = file;
                displayFileContent(file, textArea);
                archivoCargado = true;
                btnCargar.setText("Guardar cambios");
            }
        } else {
            try {
                mfm.getTiendaMusica().cargarArtistasDesdeArchivo(archivoSeleccionado.getAbsolutePath());
                mfm.guardarDatosBinario();
                aplicacion.motrarVentanaGestionArtista();
            } catch (IOException | InterruptedException | ArtistasYaEnTiendaException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Muestra el contenido de un archivo de texto en el área de texto de la interfaz.
     *
     * @param file      El archivo cuyo contenido se va a mostrar.
     * @param textArea  El área de texto donde se mostrará el contenido del archivo.
     */
    private void displayFileContent(File file, TextArea textArea) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.setText(content.toString());
    }
}
