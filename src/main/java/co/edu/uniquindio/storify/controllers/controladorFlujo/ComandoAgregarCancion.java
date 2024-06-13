package co.edu.uniquindio.storify.controllers.controladorFlujo;

import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Cliente;


public class ComandoAgregarCancion implements Comando{
    private Cliente usuario;
    private Cancion cancion;

    public ComandoAgregarCancion(Cliente usuario, Cancion cancion) {
        this.usuario = usuario;
        this.cancion = cancion;
    }

    @Override
    public void ejecutar() {
        usuario.agregarCancionFavorita(cancion);
    }

    @Override
    public void deshacer() {
        usuario.eliminarCancionFavorita(cancion);
    }

    @Override
    public void rehacer() {
        ejecutar();
    }
}