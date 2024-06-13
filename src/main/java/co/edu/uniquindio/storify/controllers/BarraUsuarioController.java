package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.AdministradorComandos;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Controlador para la barra de usuario en la interfaz de la aplicación.
 * Maneja las acciones de deshacer, rehacer y salir de la cuenta de usuario.
 */
@Data
public class BarraUsuarioController implements Initializable {

    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuario;
    private AdministradorComandos administradorComandos;

    @FXML
    private Text txtBienvenida;

    @FXML
    private Label lblFecha;

    @FXML
    private Button btnDeshacer;

    @FXML
    private Button btnRehacer;

    /**
     * Método de inicialización que se ejecuta cuando se carga la interfaz.
     * Establece la visibilidad inicial de los botones de deshacer y rehacer.
     *
     * @param url La URL de la localización utilizada para resolver rutas relativas.
     * @param resourceBundle El recurso utilizado para localizar objetos.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDeshacer.setVisible(false);
        btnRehacer.setVisible(false);
    }

    public void runTime(){
        new Thread(){
            public void run(){
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
                while(true){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    Platform.runLater(() -> {
                        Date date = new Date(System.currentTimeMillis());
                        lblFecha.setText(format.format(date));
                    });
                }
            }
        }.start();
    }

    /**
     * Carga la información del usuario y muestra un mensaje de bienvenida.
     * (Implementación pendiente).
     */
    public void cargarInfo() {
        runTime();
        String nombre= usuario.getUsername();
        int espacio=nombre.length()*10;
        txtBienvenida.setText("¡Bienvenid@, "+nombre+"!");
        txtBienvenida.setX(txtBienvenida.getX()-espacio);
    }

    /**
     * Actualiza la visibilidad de los botones de deshacer y rehacer
     * según el estado de las pilas correspondientes en el administrador de comandos.
     */
    public void actualizarBotones() {
        aplicacion.detenerVideoYoutube();
        btnDeshacer.setVisible(!administradorComandos.getPilaDeshacer().isEmpty());
        btnRehacer.setVisible(!administradorComandos.getPilaRehacer().isEmpty());
    }

    /**
     * Deshace la última acción realizada, actualiza los botones y muestra un mensaje de confirmación.
     */
    public void deshacer() {
        aplicacion.detenerVideoYoutube();
        administradorComandos.deshacer();
        mfm.guardarDatosBinario();
        actualizarBotones();
        aplicacion.ventanaInicioController.mostrarPanelDerechoClienteFavoritos();
        Alertas.mostrarMensaje("Acción Comando", "Operación completada",
                "¡Deshiciste correctamente la anterior acción! Puedes rehacerla si lo requieres", Alert.AlertType.INFORMATION);
    }

    /**
     * Rehace la última acción deshecha, actualiza los botones y muestra un mensaje de confirmación.
     */
    public void rehacer() {
        aplicacion.detenerVideoYoutube();
        administradorComandos.rehacer();
        mfm.guardarDatosBinario();
        actualizarBotones();
        Alertas.mostrarMensaje("Acción Comando", "Operación completada",
                "¡Rehiciste correctamente la anterior acción! Puedes deshacerla si lo requieres", Alert.AlertType.INFORMATION);
        aplicacion.ventanaInicioController.mostrarPanelDerechoClienteFavoritos();
    }

    /**
     * Sale de la cuenta del usuario actual y muestra la ventana de registro e ingreso.
     */
    public void salirCuenta() {
        aplicacion.detenerVideoYoutube();
        aplicacion.mostrarVentanaRegistroIngreso();
    }
}
