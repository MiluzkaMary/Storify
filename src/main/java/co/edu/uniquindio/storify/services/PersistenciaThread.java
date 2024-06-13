package co.edu.uniquindio.storify.services;

import co.edu.uniquindio.storify.model.TiendaMusica;
import co.edu.uniquindio.storify.util.Persistencia;

/**
 * Clase que implementa Runnable para realizar la persistencia de una tienda de música en un hilo separado.
 * Esto permite guardar el estado de la tienda de manera asíncrona.
 */
public class PersistenciaThread implements Runnable {

     private TiendaMusica tienda;

     /**
      * Constructor de la clase PersistenciaThread.
      *
      * @param tienda La tienda de música que se desea persistir.
      */
     public PersistenciaThread(TiendaMusica tienda) {
          this.tienda = tienda;
     }

     /**
      * Método run que se ejecuta cuando el hilo se inicia.
      * Este método llama al método de persistencia para guardar el estado de la tienda de música.
      */
     @Override
     public void run() {
          Persistencia.guardarRecursoBancoBinario(tienda);
     }
}
