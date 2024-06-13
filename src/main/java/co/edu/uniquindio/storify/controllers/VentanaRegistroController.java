package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.exceptions.*;
import co.edu.uniquindio.storify.model.Cliente;
import co.edu.uniquindio.storify.model.TiendaMusica;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class VentanaRegistroController implements Initializable {

    @FXML
    private Pane paneCrearCuenta;
    @FXML
    private TextField txtShowPassword;
    @FXML
    private Button btnRegistrar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCorreo;
    @FXML
    private PasswordField txtHidePassword;
    @FXML
    private TextField txtApellido;
    @FXML
    private Pane paneIngresar;
    @FXML
    private TextField txtUsername2;
    @FXML
    private TextField txtShowPassword2;
    @FXML
    private PasswordField txtHidePassword2;
    @FXML
    private AnchorPane paneSolid;
    @FXML
    private Label txtNoRegistrado;
    @FXML
    private Button btnYaTengoCuenta;
    @FXML
    private Button btnRegistrarmePanel;
    @FXML
    private Label txtYaRegistrado;
    @FXML
    private ImageView openEye1;
    @FXML
    private ImageView closeEye1;
    @FXML
    private ImageView openEye2;
    @FXML
    private ImageView closeEye2;

    private String password2;
    private String password;
    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private TiendaMusica tiendaMusica = mfm.getTiendaMusica();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();
    private Usuario usuarioSesion;

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtContrasenia;

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Se establecen las configuraciones iniciales para los campos y botones.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtShowPassword2.setVisible(false);
        closeEye1.setVisible(true);
        closeEye2.setVisible(true);
        openEye2.setVisible(false);
        txtShowPassword.setVisible(false);
        openEye1.setVisible(false);
    }

    /**
     * Método para ingresar un usuario al sistema.
     * Busca al usuario en la tienda de música y muestra un mensaje de éxito o error.
     */
    @FXML
    public void ingresarUsuario() {
        try {
            Usuario usuario = mfm.getTiendaMusica().buscarUsuario(txtUsername2.getText(), txtShowPassword2.getText());
            String nombreUsuario = usuario.getPersona().getNombre();
            iniciarSesionUsuario(usuario); // Guardar el usuario que inició sesión
            Alertas.mostrarMensaje("Ingreso Exitoso", "Operación completada", "¡Haz ingresado correctamente " + nombreUsuario + "!", Alert.AlertType.INFORMATION);
        } catch (AtributoVacioException | UsuarioNoExistenteException e) {
            Alertas.mostrarMensaje("Error", "Entradas no válidas", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Método para guardar el usuario que inició sesión en la variable de instancia.
     *
     * @param usuario El usuario que inició sesión.
     * @throws AtributoVacioException si algún atributo está vacío.
     * @throws UsuarioNoExistenteException si el usuario no existe.
     */
    public void iniciarSesionUsuario(Usuario usuario) throws AtributoVacioException, UsuarioNoExistenteException {
        this.usuarioSesion = usuario;
        ventana.close();
        aplicacion.mostrarVentanaPrincipal(usuario);
    }

    /**
     * Cambia el formulario para mostrar el panel de ingresar.
     */
    public void switchFormbtnIngresarPanel() {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(paneSolid);
        slider.setToX(0);
        slider.setDuration(Duration.seconds(.7));
        slider.setOnFinished(event -> {
            btnYaTengoCuenta.setVisible(false);
            txtYaRegistrado.setVisible(false);
            btnRegistrarmePanel.setVisible(true);
            txtNoRegistrado.setVisible(true);
            limpiarRegistrarPanel();
        });
        slider.play();
    }

    /**
     * Cambia el formulario para mostrar el panel de registrar.
     */
    public void switchFormbtnRegistrarmePanel() {
        TranslateTransition slider = new TranslateTransition();
        slider.setNode(paneSolid);
        slider.setToX(370);
        slider.setDuration(Duration.seconds(.7));
        slider.setOnFinished(event -> {
            btnYaTengoCuenta.setVisible(true);
            txtYaRegistrado.setVisible(true);
            btnRegistrarmePanel.setVisible(false);
            txtNoRegistrado.setVisible(false);
            limpiarIngresarPanel();
        });
        slider.play();
    }

    /**
     * Muestra la contraseña en texto plano cuando se hace clic en el icono del ojo.
     *
     * @param mousevent Evento de clic del ratón.
     */
    @FXML
    public void Open_Eye_OnClickAction2(MouseEvent mousevent) {
        txtShowPassword2.setVisible(false);
        openEye1.setVisible(false);
        closeEye1.setVisible(true);
        txtHidePassword2.setVisible(true);

        txtShowPassword.setVisible(false);
        openEye2.setVisible(false);
        closeEye2.setVisible(true);
        txtHidePassword.setVisible(true);
    }

    /**
     * Oculta la contraseña cuando se hace clic en el icono del ojo cerrado.
     *
     * @param mousevent Evento de clic del ratón.
     */
    @FXML
    public void Close_Eye_ClickOnAction2(MouseEvent mousevent) {
        txtShowPassword2.setVisible(true);
        openEye1.setVisible(true);
        closeEye1.setVisible(false);
        txtHidePassword2.setVisible(false);

        txtShowPassword.setVisible(true);
        openEye2.setVisible(true);
        closeEye2.setVisible(false);
        txtHidePassword.setVisible(false);
    }

    /**
     * Sincroniza las contraseñas visibles y ocultas cuando se muestran.
     *
     * @param keyevent Evento de tecla presionada.
     */
    @FXML
    public void ShowPasswordOnAction2(KeyEvent keyevent) {
        password2 = txtShowPassword2.getText();
        txtHidePassword2.setText(password2);

        password = txtShowPassword.getText();
        txtHidePassword.setText(password);
    }

    /**
     * Sincroniza las contraseñas visibles y ocultas cuando se ocultan.
     *
     * @param keyevent Evento de tecla presionada.
     */
    @FXML
    public void HidePasswordKeyReleased2(KeyEvent keyevent) {
        password2 = txtHidePassword2.getText();
        txtShowPassword2.setText(password2);

        password = txtHidePassword.getText();
        txtShowPassword.setText(password);
    }

    /**
     * Limpia los campos del panel de ingresar.
     */
    public void limpiarIngresarPanel() {
        txtUsername2.clear();
        txtShowPassword2.clear();
        txtHidePassword2.clear();
        password2 = "";
    }

    /**
     * Limpia los campos del panel de registrar.
     */
    public void limpiarRegistrarPanel() {
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
        txtUsername.clear();
        txtShowPassword.clear();
        txtHidePassword.clear();
        password = "";
    }

    /**
     * Registra un nuevo cliente en el sistema.
     * Crea un nuevo cliente y usuario, y muestra un mensaje de éxito o error.
     */
    public void registrarCliente() {
        try {
            Cliente cliente = tiendaMusica.crearCliente(txtNombre.getText(), txtApellido.getText());
            Usuario usuario = tiendaMusica.crearUsuario(txtUsername.getText(), txtShowPassword.getText(), txtCorreo.getText(), cliente);
            tiendaMusica.agregarUsuario(usuario);
            Alertas.mostrarMensaje("Registro Confirmado", "Operación completada", "Se ha registrado correctamente el cliente: " + cliente.getNombre(), Alert.AlertType.INFORMATION);
            mfm.guardarDatosBinario();
            limpiarRegistrarPanel();
        } catch (AtributoVacioException | UsuarioYaRegistradoException e) {
            Alertas.mostrarAlertaError(e.getMessage());
        }
    }
}
