package co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaDoble;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.BinaryNode;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.TiendaMusica;
import lombok.Data;

@Data

public class BusquedaIzquierdaFiltros<T extends Comparable<T>> extends Thread {
    private BinaryNode<Artista> nodo;
    private String genero;
    private String anioLanzamiento;
    private String duracion;

    private ListaEnlazadaDoble<Cancion> cancionesFiltradas1;
    private ListaEnlazadaDoble<Cancion> cancionesFiltradas2;

    public BusquedaIzquierdaFiltros(BinaryNode<Artista> nodo, String genero, String anioLanzamiento, String duracion) {
        this.nodo = nodo;
        this.genero = genero;
        this.anioLanzamiento = anioLanzamiento;
        this.duracion = duracion;
        this.cancionesFiltradas1 = new ListaEnlazadaDoble<>();
        this.cancionesFiltradas2 = new ListaEnlazadaDoble<>();
    }

    @Override
    public void run() {
        buscarCancionesUnaCondicion(nodo);
        buscarCancionesTodasCondiciones(nodo);
    }

    private void buscarCancionesUnaCondicion(BinaryNode<Artista> nodo) {
        if (nodo != null) {
            Artista artista = nodo.getData();
            for (Cancion cancion : artista.getCanciones()) {
                if (TiendaMusica.cumpleMinimoUnFiltro(cancion, genero, anioLanzamiento, duracion)) {
                    cancionesFiltradas1.add(cancion);
                }
            }
            buscarCancionesUnaCondicion(nodo.getLeftNode());
            buscarCancionesUnaCondicion(nodo.getRightNode()); // Agregar búsqueda a la derecha
        }
    }

    private void buscarCancionesTodasCondiciones(BinaryNode<Artista> nodo) {
        if (nodo != null) {
            Artista artista = nodo.getData();
            for (Cancion cancion : artista.getCanciones()) {
                if (TiendaMusica.cumpleTodosLosFiltros(cancion, genero, anioLanzamiento, duracion)) {
                    cancionesFiltradas2.add(cancion);
                }
            }
            buscarCancionesTodasCondiciones(nodo.getLeftNode());
            buscarCancionesTodasCondiciones(nodo.getRightNode()); // Agregar búsqueda a la derecha
        }
    }



}