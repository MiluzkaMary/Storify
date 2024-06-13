package co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.BinaryNode;

@SuppressWarnings("all")
public class BusquedaDerecha<T extends Comparable<T>> extends Thread {
    private BinaryNode<T> nodo;
    private T data;
    private T resultado;

    public BusquedaDerecha(BinaryNode<T> nodo, T data) {
        this.nodo = nodo;
        this.data = data;
    }

    @Override
    public void run() {
        resultado = buscarRecursivo(nodo, data);
    }

    private T buscarRecursivo(BinaryNode<T> nodo, T data) {
        if (nodo == null) {
            return null;
        }
        if (data.compareTo(nodo.getData()) < 0) {
            return buscarRecursivo(nodo.getLeftNode(), data);
        } else if (data.compareTo(nodo.getData()) > 0) {
            return buscarRecursivo(nodo.getRightNode(), data);
        }
        return nodo.getData();
    }

    public T getResultado() {
        return resultado;
    }
}