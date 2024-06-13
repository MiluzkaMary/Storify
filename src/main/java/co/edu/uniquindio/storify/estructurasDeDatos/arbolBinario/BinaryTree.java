package co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaDoble;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimple;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.BinaryNode;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import co.edu.uniquindio.storify.exceptions.EmptyNodeException;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.TiendaMusica;

import lombok.Data;

import java.io.Serializable;

@SuppressWarnings("all")
@Data
public class BinaryTree<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    private BinaryNode root;

    public BinaryTree() {
        this.root = null;
    }

    public BinaryTree(T data) {
        this.root = new BinaryNode<>(data);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        if (root == null) {
            return 0;
        }
        return 1 + size(root.getLeftNode()) + size(root.getRightNode());
    }

    private int size(BinaryNode<T> node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.getLeftNode()) + size(node.getRightNode());
    }

    public void insert(T data) {
        this.root = insert(this.root, data);
    }

    private BinaryNode<T> insert(BinaryNode<T> node, T data) {
        if (node == null) {
            return new BinaryNode<>(data);
        }
        if (data.compareTo(node.getData()) < 0) {
            node.setLeftNode(insert(node.getLeftNode(), data));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRightNode(insert(node.getRightNode(), data));
        }
        return node;
    }

    public T find(T data) throws InterruptedException {
        if (this.root == null) {
            return null;
        }

        if (root.getData().equals(data)) {
            return (T) root.getData();
        }

        BusquedaIzquierda<T> tareaIzquierda = new BusquedaIzquierda<>(root.getLeftNode(), data);
        BusquedaDerecha<T> tareaDerecha = new BusquedaDerecha<>(root.getRightNode(), data);

        tareaIzquierda.start();
        tareaDerecha.start();

        tareaIzquierda.join();
        tareaDerecha.join();

        T resultadoIzquierda = tareaIzquierda.getResultado();
        T resultadoDerecha = tareaDerecha.getResultado();

        if (resultadoIzquierda != null) {
            return resultadoIzquierda;
        } else {
            return resultadoDerecha;
        }

    }

    public ListaEnlazadaSimple<T> iterator() {
        ListaEnlazadaSimple<T> list = new ListaEnlazadaSimple<>();
        iterator(this.root, list);
        return list;
    }

    private void iterator(BinaryNode<T> node, ListaEnlazadaSimple<T> list) {
        if (node != null) {
            iterator(node.getLeftNode(), list); // Recorrer el subárbol izquierdo
            list.add(node.getData()); // Agregar el dato del nodo actual a la list
            iterator(node.getRightNode(), list); // Recorrer el subárbol derecho
        }
    }

    public void delete(T data) throws EmptyNodeException {
        if (this.root == null) {
            throw new EmptyNodeException("El árbol está vacío");
        }
        this.root = delete(this.root, data);
    }

    private BinaryNode<T> delete(BinaryNode<T> node, T data) {
        if (node == null) {
            return null;
        }
        if (data.compareTo(node.getData()) < 0) {
            node.setLeftNode(delete(node.getLeftNode(), data));
        } else if (data.compareTo(node.getData()) > 0) {
            node.setRightNode(delete(node.getRightNode(), data));
        } else { // Nodo encontrado
            if (node.getLeftNode() == null) {
                return node.getRightNode(); // Caso sin hijo izquierdo
            } else if (node.getRightNode() == null) {
                return node.getLeftNode(); // Caso sin hijo derecho
            } else {
                // Caso con ambos hijos
                T min = findMin(node.getRightNode());
                node.setData(min);
                node.setRightNode(delete(node.getRightNode(), min));
            }
        }
        return node;
    }

    private T findMin(BinaryNode<T> node) {
        while (node.getLeftNode() != null) {
            node = node.getLeftNode();
        }
        return node.getData();
    }

    // Método para recorrer el árbol en orden (inorden)
    public void inorder() {
        inorder(this.root);
    }

    // Método privado recursivo para recorrer el árbol en orden (inorden)
    private void inorder(BinaryNode<T> node) {
        if (node != null) {
            inorder(node.getLeftNode()); // Recorrer el subárbol izquierdo
            System.out.println(node.getData()); // Visitar el nodo actual
            inorder(node.getRightNode()); // Recorrer el subárbol derecho
        }
    }

    public ListaEnlazadaDoble<Cancion> obtenerBusquedaAlMenosUnFiltro(String genero, String anioLanzamiento, String duracion) throws InterruptedException {
        ListaEnlazadaDoble<Cancion> cancionesFiltradas = new ListaEnlazadaDoble<>();

        if (root != null) {
            Artista nodoRaiz= (Artista)root.getData();

            for (Cancion cancion : nodoRaiz.getCanciones()) {
                if (TiendaMusica.cumpleMinimoUnFiltro(cancion, genero, anioLanzamiento, duracion)) {
                    cancionesFiltradas.add(cancion);
                }
            }

            BusquedaIzquierdaFiltros<Artista> tareaIzquierda = new BusquedaIzquierdaFiltros<>(root.getLeftNode(), genero, anioLanzamiento, duracion);
            tareaIzquierda.start();


            BusquedaDerechaFiltros<Artista> tareaDerecha = new BusquedaDerechaFiltros<>(root.getRightNode(), genero, anioLanzamiento, duracion);
            tareaDerecha.start();

            tareaIzquierda.join();
            tareaDerecha.join();

            ListaEnlazadaDoble<Cancion> cancionesIzquierda = tareaIzquierda.getCancionesFiltradas1();
            ListaEnlazadaDoble<Cancion> cancionesDerecha = tareaDerecha.getCancionesFiltradas1();

            cancionesFiltradas.addAll(cancionesIzquierda);
            cancionesFiltradas.addAll(cancionesDerecha);
        }

        return cancionesFiltradas;
    }

    public ListaEnlazadaDoble<Cancion> obtenerBusquedaTodosFiltros(String genero, String anioLanzamiento, String duracion) throws InterruptedException {


        ListaEnlazadaDoble<Cancion> cancionesFiltradas = new ListaEnlazadaDoble<>();
        System.out.println("bla");
        if (root != null) {
            System.out.println("no es null");
            Artista nodoRaiz= (Artista)root.getData();

            System.out.println(nodoRaiz.getNombre()+ " es su nombre");

            for (Cancion cancion : nodoRaiz.getCanciones()) {
                System.out.println("la cancion: "+ cancion.getNombre());

                if (TiendaMusica.cumpleTodosLosFiltros(cancion, genero, anioLanzamiento, duracion)) {
                    cancionesFiltradas.add(cancion);
                    System.out.println("Si cumplio");
                }
                System.out.println("No cumplio");
            }

            BusquedaIzquierdaFiltros<Artista> tareaIzquierda = new BusquedaIzquierdaFiltros<>(root.getLeftNode(), genero, anioLanzamiento, duracion);
            tareaIzquierda.start();


            BusquedaDerechaFiltros<Artista> tareaDerecha = new BusquedaDerechaFiltros<>(root.getRightNode(), genero, anioLanzamiento, duracion);
            tareaDerecha.start();

            tareaIzquierda.join();
            tareaDerecha.join();

            ListaEnlazadaDoble<Cancion> cancionesIzquierda = tareaIzquierda.getCancionesFiltradas2();
            ListaEnlazadaDoble<Cancion> cancionesDerecha = tareaDerecha.getCancionesFiltradas2();

            cancionesFiltradas.addAll(cancionesIzquierda);
            cancionesFiltradas.addAll(cancionesDerecha);

            cancionesFiltradas.toString();
        }

        return cancionesFiltradas;
    }



    public void reemplazarValor(T value, T valueActualizado) {
        BinaryNode<T> nodo = buscarNodo(root, value);
        if (nodo != null) {
            nodo.setData(valueActualizado);
        }
    }

    private BinaryNode<T> buscarNodo(BinaryNode<T> node, T data) {
        if (node == null || node.getData().equals(data)) {
            return node;
        }
        if (data.compareTo(node.getData()) < 0) {
            return buscarNodo(node.getLeftNode(), data);
        } else {
            return buscarNodo(node.getRightNode(), data);
        }
    }



}

