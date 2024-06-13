package co.edu.uniquindio.storify.controllers.controladorFlujo;

import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.Cliente;

public class ComandoEliminarCancion implements Comando {
    private Cliente usuario;
    private Cancion cancion;

    public ComandoEliminarCancion(Cliente usuario, Cancion cancion) {
        this.usuario = usuario;
        this.cancion = cancion;
    }

    @Override
    public void ejecutar() {
        usuario.eliminarCancionFavorita(cancion);
    }

    @Override
    public void deshacer() {
        usuario.agregarCancionFavorita(cancion);
    }

    @Override
    public void rehacer() {
        ejecutar();
    }
}