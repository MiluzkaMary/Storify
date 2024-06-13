package co.edu.uniquindio.storify.controllers;

import co.edu.uniquindio.storify.app.Aplicacion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.AdministradorComandos;
import co.edu.uniquindio.storify.exceptions.ArtistasYaEnTiendaException;
import co.edu.uniquindio.storify.model.*;
import co.edu.uniquindio.storify.services.PersistenciaThread;
import co.edu.uniquindio.storify.util.Persistencia;
import co.edu.uniquindio.storify.util.StorifyUtil;
import javafx.stage.Stage;

import java.io.IOException;


@SuppressWarnings("all")
public class ModelFactoryController {
    private TiendaMusica tiendaMusica=null;
    private Aplicacion aplicacion=null;
    private Stage ventana;
    private AdministradorComandos administradorComandos = new AdministradorComandos();
    private PersistenciaThread persistenciaThread = null;

    public ModelFactoryController()  {
        cargarDatosBinario();

        if(tiendaMusica == null){
            cargarDatosPrueba();
        }
        persistenciaThread = new PersistenciaThread(tiendaMusica);
        //guardarDatosBinario();
    }



    private void cargarDatosBinario() {
        this.tiendaMusica = Persistencia.cargarRecursoBancoBinario();
    }

    public void guardarDatosBinario() {
        persistenciaThread.run();
    }

    private void cargarDatosPrueba() {
        tiendaMusica = StorifyUtil.inicializarDatosPrueba();
    }


    /**
     * Metodo que obtiene la TiendaMusica
     * @return TiendaMusica
     */
    public TiendaMusica getTiendaMusica() {
        return tiendaMusica;
    }

    /**
     * Metodo que cambia la tiendaMusica
     * @param tiendaMusica Nueva tiendaMusica
     */
    public void setConcesionario(TiendaMusica tiendaMusica) {
        this.tiendaMusica = tiendaMusica;
    }

    /**
     * Obtiene la ventana
     * @return ventana
     */
    public Stage getVentana() {
        return ventana;
    }

    /**
     * Establece la ventana
     * @param ventana La ventana
     */
    public void setVentana(Stage ventana) {
        this.ventana = ventana;
    }

    /**
     * Obtiene la direccion de la aplicacion principal
     * @return Aplicacion principal
     */
    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    /**
     * Establece la direccion de la aplicacion principal
     * @param aplicacion Nueva aplicacion principal
     */
    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public AdministradorComandos getAdministradorComandos() {
        return administradorComandos;
    }


     /*
    -----------------------------------------------------------------------------------------------------------
    --------------------------------------GET INSTANCE---------------------------------------------------------
    */
    /**
     * Obtiene una instancia única de la clase ModelFactoryController.
     * @return Una instancia única de ModelFactoryController.
     */
    public static ModelFactoryController getInstance() {
        return SingletonHolder.INSTANCE;
    }



    /**
     * Clase interna que contiene la instancia única de ModelFactoryController.
     */
    private static class SingletonHolder {
        private static final ModelFactoryController INSTANCE = new ModelFactoryController();
    }
}