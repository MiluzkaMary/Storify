package co.edu.uniquindio.storify.estructurasDeDatos.cola;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;

import java.io.Serializable;

@SuppressWarnings("all")
public class Cola<T>  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int tamanio;
    private Node<T> nodoCabeza;
    private Node<T> nodoUltimo;

    public Cola() {
        this.nodoCabeza = null;
        this.nodoUltimo = null;
        this.tamanio = 0;
    }

    public Node<T> getNodoUltimo() {
        return nodoUltimo;
    }

    public void setNodoUltimo(Node<T> nodoUltimo) {
        this.nodoUltimo = nodoUltimo;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public Node<T> getCabeza() {
        return nodoCabeza;
    }

    public void setCabeza(Node<T> cabeza) {
        this.nodoCabeza = cabeza;
    }

    public void add(T info) {
        Node<T> nodo = new Node<T>(info);
        if (nodoCabeza == null) {
            nodoCabeza = nodo;
            nodoUltimo = nodo;
        } else {
            nodoUltimo.setNextNode(nodo);
            nodoUltimo = nodo;
        }
        tamanio++;
    }


    public void imprimir() {
        Node<T> nodoActual = nodoCabeza;

        while (nodoActual != null) {
            System.out.print(nodoActual.getData() + "   ");
            nodoActual = nodoActual.getNextNode();
        }
        System.out.println();
    }

    public Node<T> getNodoCabeza() {
        return nodoCabeza;
    }

    public void setNodoCabeza(Node<T> nodoCabeza) {
        this.nodoCabeza = nodoCabeza;
    }

    public T desencolar() {
        if (nodoCabeza == null) {
            throw new RuntimeException("Lista Vacía");
        } else {
            T t = nodoCabeza.getData();
            tamanio--;
            nodoCabeza = nodoCabeza.getNextNode();
            return t;
        }
    }

    public T getObject(int indice){
        if(indice < 0){
            System.out.println("El indice no puede ser negativo");
        }

        if (indice == 0){
            return nodoCabeza.getData();
        }

        Node<T> nodoActual = nodoCabeza;
        int contador = 0;

        while (nodoActual != null && contador <= indice) {
            if(contador==indice){
                return nodoActual.getData();
            }
            nodoActual = nodoActual.getNextNode();
            contador++;
        }

        if (nodoActual == null) {
            throw new IndexOutOfBoundsException("Indice fuera de rango");
        }
        return null;
    }

    public boolean isEmpty(){
        return nodoCabeza == null;
    }

    public T getPrimero(){
        return nodoCabeza.getData();
    }

    public boolean sonIguales(Cola<T> cola1, Cola<T> cola2) {
        if (cola1.getTamanio() != cola2.getTamanio()) {
            return false;
        }
        Cola<T> colaAux1 = cola1.copy();
        Cola<T> colaAux2 = cola2.copy();
        while (!colaAux1.isEmpty()) {
            T elementoCola1 = colaAux1.desencolar();
            T elementoCola2 = colaAux2.desencolar();
            if (!elementoCola1.equals(elementoCola2)) {
                return false;
            }
        }
        return true;
    }

    public Cola<T> copy() {
        Cola<T> copia = new Cola<>();
        Cola<T> colaTemporal = new Cola<>();
        Node<T> nodoActual = nodoCabeza;
        while (nodoActual != null) {
            T elemento = nodoActual.getData();
            copia.add(elemento);
            colaTemporal.add(elemento);
            nodoActual = nodoActual.getNextNode();
        }
        while (!colaTemporal.isEmpty()) {
            this.add(colaTemporal.desencolar());
        }

        return copia;
    }

    public void add(T elemento, int i){
        if (i < 0 || i > tamanio) {
            throw new RuntimeException("Posición fuera de rango");
        }
        Node<T> nuevoNodo = new Node<>(elemento);
        if (i == 0) {
            nuevoNodo.setNextNode(nodoCabeza);
            nodoCabeza = nuevoNodo;
            if (nodoUltimo == null) {
                nodoUltimo = nodoCabeza;
            }
        } else {
            Node<T> nodoActual = nodoCabeza;
            for (int j = 0; j < i - 1; j++) {
                nodoActual = nodoActual.getNextNode();
            }
            nuevoNodo.setNextNode(nodoActual.getNextNode());
            nodoActual.setNextNode(nuevoNodo);
            if (nodoActual == nodoUltimo) {
                nodoUltimo = nuevoNodo;
            }
        }
        if (nuevoNodo.getNextNode() == null) {
            nodoUltimo = nuevoNodo;
        }
        tamanio++;
    }
}
