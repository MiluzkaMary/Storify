package co.edu.uniquindio.storify.estructurasDeDatos.listas;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import lombok.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("All")
@Data
public class ListaEnlazadaSimple<T> implements IListasEnlazadas<T>, Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private Node<T> headNode;
    private Node<T> lastNode;
    private int size;

    /**
     * Constructor de la clase ListaEnlazadaSimple.
     * Inicializa los nodos de la cabeza y el último nodo como nulos y el tamaño como 0.
     */
    public ListaEnlazadaSimple() {
        this.headNode = null;
        this.lastNode = null;
        this.size = 0;
    }

    /**
     * Agrega un nuevo nodo al principio de la lista.
     * @param data El dato a agregar al nodo.
     */
    public void addFirst(T data){
        Node<T> nodo = new Node<T>(data);
        if (headNode == null){
            headNode = nodo;
            lastNode = nodo;
        }else{
            nodo.setNextNode(headNode);
            headNode = nodo;
        }
        size++;
    }

    /**
     * Agrega un nuevo nodo al final de la lista.
     * @param info El dato a agregar al nodo.
     */
    @Override
    public void add(T info) {
        if(headNode == null){
            addFirst(info);
            return;
        }

        Node<T> nodo = new Node<>(info);
        lastNode.setNextNode(nodo);
        lastNode = nodo;
        size++;
    }

    /**
     * Agrega un nuevo nodo en la posición especificada por el índice.
     * @param index El índice donde se agregará el nuevo nodo.
     * @param data El dato a agregar al nuevo nodo.
     */
    public void addIndex(Integer index, T data){
        if(index < 0){
            System.out.println("El índice no puede ser menor a 1");
        }

        if (index == 0){
            addFirst(data);
        }

        Node<T> newNode = new Node<T>(data);
        Node<T> actualNode = headNode;
        int counter = 0;

        while (actualNode != null && counter < index - 1) {
            actualNode = actualNode.getNextNode();
            counter++;
        }

        if (actualNode == null) {
            System.out.println("El índice está fuera de rango.");
            return;
        }

        Node<T> nextNode = actualNode.getNextNode();
        actualNode.setNextNode(newNode);
        newNode.setNextNode(nextNode);
        size++;
    }

    /**
     * Elimina un nodo que contiene el dato especificado.
     * @param data El dato a eliminar.
     */
    @Override
    public void removeData(T data) {
        Node<T> actualNode = headNode;

        if(size == 0){
            System.out.println("La lista está vacía");
            return;
        }

        if (actualNode.getData().equals(data)){
            headNode = actualNode.getNextNode();
            size--;
            return;
        }

        while (actualNode.getNextNode() != null && !actualNode.getNextNode().getData().equals(data)){
            actualNode = actualNode.getNextNode();
        }

        if(actualNode.getNextNode() == null){
            System.out.println("El elemento no se encuentra dentro de la lista");
            return;
        }

        if (actualNode.getNextNode().getData().equals(data)){
            actualNode.setNextNode(actualNode.getNextNode().getNextNode());
            size--;
        }
    }

    /**
     * Elimina un nodo en la posición especificada por el índice.
     * @param index El índice del nodo a eliminar.
     */
    @Override
    public void removeIndex(Integer index) {
        if (index < 0 || index >= size) {
            System.out.println("El índice está fuera de rango");
            return;
        }

        if (index == 0) {
            headNode = headNode.getNextNode();
            size--;
            return;
        }

        Node<T> prevNode = null;
        Node<T> currentNode = headNode;
        int currentIndex = 0;

        while (currentNode != null && currentIndex < index) {
            prevNode = currentNode;
            currentNode = currentNode.getNextNode();
            currentIndex++;
        }

        if (currentNode == null) {
            System.out.println("El índice está fuera de rango");
            return;
        }

        // Eliminar el nodo actual
        prevNode.setNextNode(currentNode.getNextNode());
        size--;
    }

    /**
     * Busca el primer nodo que contiene el dato especificado y devuelve su índice.
     * @param data El dato a buscar.
     * @return El índice del nodo que contiene el dato, o -1 si no se encuentra.
     */
    @Override
    public int search(T data) {
        Node<T> currentNode = headNode;
        int index = 0;

        while (currentNode != null) {
            if (currentNode.getData().equals(data)) {
                return index; // Se encontró el elemento, devuelve el índice
            }
            currentNode = currentNode.getNextNode();
            index++;
        }

        return -1; // El elemento no se encontró en la lista
    }

    /**
     * Devuelve el dato del nodo en la posición especificada por el índice.
     * @param index El índice del nodo.
     * @return El dato del nodo en la posición especificada.
     */
    @Override
    public T locate(Integer index) {
        if (index < 0 || index >= size) {
            System.out.println("El índice está fuera de rango.");
            return null;
        }

        Node<T> currentNode = headNode;
        int currentIndex = 0;

        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.getNextNode();
            currentIndex++;
        }

        if (currentNode == null) {
            System.out.println("El índice está fuera de rango.");
            return null;
        }

        return currentNode.getData();
    }

    @Override
    public T get(Integer index) {
        return locate(index);
    }

    /**
     * Limpia la lista, eliminando todos los nodos.
     */
    @Override
    public void clear() {
        headNode = null;
        size = 0;
    }

    /**
     * Imprime los datos de los nodos en la lista.
     */
    @Override
    public void println() {
        Node<T> currentNode = headNode;

        if(headNode == null){
            return;
        }

        while (currentNode != null) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getNextNode();
        }
    }

    /**
     * Invierte la lista enlazada.
     */
    public void reversed(){
        reversedLinkedList(headNode);
    }

    private void reversedLinkedList(Node<T> currentNode) {
        if (currentNode.getNextNode() == null){
            headNode = lastNode = currentNode;
            return;
        }

        reversedLinkedList(currentNode.getNextNode());
        currentNode.setNextNode(null);
        lastNode.setNextNode(currentNode);
        lastNode = currentNode;
    }

    /**
     * Devuelve el tamaño de la lista.
     * @return El tamaño de la lista.
     */
    public int size() {
        return size;
    }

    @Override
    public int countAppearances(T data) {
        int count = 0;
        Node<T> current = headNode;

        while (current != null) {
            if (current.getData().equals(data)) {
                count++;
            }
            current = current.getNextNode();
        }

        return count;
    }

    public void addAll(ListaEnlazadaSimple<T> list) {
        for (T data : list) {
            add(data);
        }
    }

    public void addList(int index, ListaEnlazadaSimple<T> list){
        if(index > size || index < 0){
            throw new IndexOutOfBoundsException("El indice esta fuera de rango");
        }

        if(index == 0){
            list.getLastNode().setNextNode(headNode);
            headNode = list.getHeadNode();
            return;
        }

        Node<T> currentNode = headNode;
        int count = 0;
        while(count < index-1){
            currentNode = currentNode.getNextNode();
            count++;
        }
        Node<T> nextNode = currentNode.getNextNode();
        currentNode.setNextNode(list.getHeadNode());
        list.getLastNode().setNextNode(nextNode);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ListaEnlazadaSimple{");
        Node<T> currentNode = headNode;
        while (currentNode != null) {
            sb.append(currentNode.getData());
            if (currentNode.getNextNode() != null) {
                sb.append(" -> ");
            }
            currentNode = currentNode.getNextNode();
        }
        sb.append("}");
        return sb.toString();
    }



    @Override
    public Iterator<T> iterator() {
        return new ListaEnlazadaSimpleIterator<T>(headNode);
    }

    public boolean isEmpty() {
        return headNode == null;
    }

    private class ListaEnlazadaSimpleIterator<T> implements Iterator<T>{
        private Node<T> currentNode;

        public ListaEnlazadaSimpleIterator(Node<T> currentNode) {
            this.currentNode = currentNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la lista");
            }
            T data = currentNode.getData();
            currentNode = currentNode.getNextNode();
            return data;
        }
    }
}
