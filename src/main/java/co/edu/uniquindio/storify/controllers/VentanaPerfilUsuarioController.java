package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.exceptions.AtributoVacioException;
import co.edu.uniquindio.storify.exceptions.EmailInvalidoException;
import co.edu.uniquindio.storify.exceptions.UsuarioNoExistenteException;
import co.edu.uniquindio.storify.model.TiendaMusica;
import co.edu.uniquindio.storify.model.Usuario;
import co.edu.uniquindio.storify.util.Alertas;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.Data;

import java.net.URL;
import java.util.ResourceBundle;

@Data
public class VentanaPerfilUsuarioController implements Initializable {
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private ImageView btnFavoritos;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtShowPassword;
    @FXML
    private PasswordField txtHidePassword;
    @FXML
    private ImageView closeEye;
    @FXML
    private ImageView openEye;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnConfirmar;

    private String password;
    private Usuario usuario;
    private ModelFactoryController mfm = ModelFactoryController.getInstance();
    private TiendaMusica tiendaMusica = mfm.getTiendaMusica();
    private Stage ventana = mfm.getVentana();
    private Aplicacion aplicacion = mfm.getAplicacion();

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Se establecen las configuraciones iniciales para los campos y botones.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtCorreo.setEditable(false);
        txtHidePassword.setEditable(false);
        txtShowPassword.setEditable(false);
        txtUsername.setEditable(false);
        closeEye.setVisible(true);
        openEye.setVisible(false);
        txtShowPassword.setVisible(false);
    }

    /**
     * Establece el usuario actual y carga sus datos en los campos correspondientes.
     *
     * @param usuario El usuario actual.
     */
    public void setearUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatos(); // Cargar los datos del usuario al iniciar el controlador
        txtShowPassword.setVisible(false);
        openEye.setVisible(false);
        password = usuario.getPassword();
    }

    /**
     * Carga los datos del usuario en los campos de texto.
     */
    public void cargarDatos() {
        txtNombre.setText(usuario.getPersona().getNombre());
        txtApellido.setText(usuario.getPersona().getApellido());
        txtCorreo.setText(usuario.getEmail());
        txtUsername.setText(usuario.getUsername());
        txtHidePassword.setText(usuario.getPassword());
        txtShowPassword.setText(usuario.getPassword());
    }

    /**
     * Evento que se activa al mostrar la contraseña en texto plano.
     *
     * @param keyevent Evento de tecla presionada.
     */
    @FXML
    public void ShowPasswordOnAction(KeyEvent keyevent) {
        password = txtShowPassword.getText();
        txtHidePassword.setText(password);
    }

    /**
     * Confirma la edición de los datos del usuario.
     *
     * @param actionEvent Evento de acción.
     */
    public void confirmarEdicion(ActionEvent actionEvent) {
        try {
            Usuario usuario1 = tiendaMusica.editarUsuario(
                    usuario,
                    txtUsername.getText(),
                    txtShowPassword.getText(),
                    txtCorreo.getText(),
                    txtNombre.getText(),
                    txtApellido.getText()
            );
            Alertas.mostrarMensaje("Registro Confirmado", "Operación completada",
                    "Se ha editado correctamente al cliente: " + usuario1.getUsername(), Alert.AlertType.INFORMATION);
            aplicacion.mostrarVentanaPrincipal(usuario1);
            mfm.guardarDatosBinario();
        } catch (EmailInvalidoException | AtributoVacioException | UsuarioNoExistenteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Habilita la edición de los campos de texto para modificar los datos del usuario.
     *
     * @throws AtributoVacioException si algún atributo está vacío.
     */
    public void habilitarEdicion() throws AtributoVacioException {
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtCorreo.setEditable(true);
        txtUsername.setEditable(true);
        txtHidePassword.setEditable(true);
        txtShowPassword.setEditable(true);
        btnEditar.setVisible(false);
        btnConfirmar.setVisible(true);
    }

    /**
     * Evento que se activa al ocultar la contraseña en texto plano.
     *
     * @param keyevent Evento de tecla presionada.
     */
    @FXML
    public void HidePasswordKeyReleased(KeyEvent keyevent) {
        password = txtHidePassword.getText();
        txtShowPassword.setText(password);
    }

    /**
     * Evento que se activa al hacer clic en el icono de ojo abierto para mostrar la contraseña.
     *
     * @param mousevent Evento de clic del ratón.
     */
    @FXML
    public void Open_Eye_OnClickAction(MouseEvent mousevent) {
        txtShowPassword.setVisible(false);
        openEye.setVisible(false);
        closeEye.setVisible(true);
        txtHidePassword.setVisible(true);
    }

    /**
     * Evento que se activa al hacer clic en el icono de ojo cerrado para ocultar la contraseña.
     *
     * @param mousevent Evento de clic del ratón.
     */
    @FXML
    public void Close_Eye_OnClickAction(MouseEvent mousevent) {
        txtShowPassword.setVisible(true);
        openEye.setVisible(true);
        closeEye.setVisible(false);
        txtHidePassword.setVisible(false);
    }

    /**
     * Muestra la ventana de canciones favoritas del usuario.
     *
     * @param mouseEvent Evento de clic del ratón.
     */
    public void mostrarVentanaFavoritos(MouseEvent mouseEvent) {
        aplicacion.mostrarVentanaMisCanciones();
    }
}
