package co.edu.uniquindio.storify.estructurasDeDatos.pila;


import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;

@SuppressWarnings("all")
public class Pila<T> {
    private int tamanio;
    private Node<T> nodoCabeza;
    private Node<T> nodoUltimo;

    public Pila() {
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

    public T desapilar() {
        if (isEmpty()) {
            throw new RuntimeException("La pila está vacía");
        }

        T t = nodoUltimo.getData();

        if (nodoCabeza == nodoUltimo) { // Si solo hay un elemento en la pila
            nodoCabeza = null;
            nodoUltimo = null;
        } else {
            Node<T> nodoActual = nodoCabeza;
            while (nodoActual.getNextNode() != nodoUltimo) {
                nodoActual = nodoActual.getNextNode();
            }
            nodoUltimo = nodoActual;
            nodoUltimo.setNextNode(null);
        }
        tamanio--;
        return t;
    }


    public boolean isEmpty() {
        return nodoCabeza == null;
    }

    public T getPrimero() {
        return nodoCabeza.getData();
    }

    public boolean sonIguales(Pila<T> pila1, Pila<T> pila2) {
        if (pila1.getTamanio() != pila2.getTamanio()) {
            return false;
        }
        Pila<T> pilaAux1 = pila1.copy();
        Pila<T> pilaAux2 = pila2.copy();
        while (!pilaAux1.isEmpty()) {
            T elementoPila1 = pilaAux1.desapilar();
            T elementoPila2 = pilaAux2.desapilar();
            if (!elementoPila1.equals(elementoPila2)) {
                return false;
            }
        }
        return true;
    }

    public Pila<T> copy() {
        Pila<T> copia = new Pila<>();
        Pila<T> pilaTemporal = new Pila<>();
        Node<T> nodoActual = nodoCabeza;
        while (nodoActual != null) {
            T elemento = nodoActual.getData();
            copia.add(elemento);
            pilaTemporal.add(elemento);
            nodoActual = nodoActual.getNextNode();
        }
        while (!pilaTemporal.isEmpty()) {
            this.add(pilaTemporal.desapilar());
        }

        return copia;
    }

    public void add(T elemento, int i) {
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

    public void clear() {
        nodoCabeza = null;
        nodoUltimo = null;
        tamanio = 0;
    }
}