package co.edu.uniquindio.storify.estructurasDeDatos.listas;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import lombok.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("All")
@Data
public class ListaEnlazadaDobleCircular<T> implements IListasEnlazadas<T>, Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private Node<T> headNode;
    private Node<T> lastNode;
    private int size;

    public ListaEnlazadaDobleCircular() {
        this.headNode = null;
        this.lastNode = null;
        this.size = 0;
    }

    @Override
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (headNode == null) {
            headNode = newNode;
            lastNode = newNode;
            newNode.setNextNode(newNode);
            newNode.setPrevNode(newNode);
        } else {
            newNode.setNextNode(headNode);
            headNode.setPrevNode(newNode);
            newNode.setPrevNode(lastNode);
            lastNode.setNextNode(newNode);
            headNode = newNode;
        }
        size++;
    }

    @Override
    public void add(T data) {
        Node<T> newNode = new Node<>(data);

        if (headNode == null) {
            headNode = newNode;
            lastNode = newNode;
            newNode.setNextNode(newNode);
            newNode.setPrevNode(newNode);
        } else {
            newNode.setNextNode(headNode);
            headNode.setPrevNode(newNode);
            newNode.setPrevNode(lastNode);
            lastNode.setNextNode(newNode);
            lastNode = newNode;
        }
        size++;
    }

    @Override
    public void addIndex(Integer index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index fuera del rango");
        }

        if (index == 0) {
            addFirst(data);
        } else if (index == size) {
            add(data);
        } else {
            Node<T> newNode = new Node<>(data);
            Node<T> current = headNode;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNextNode();
            }
            newNode.setNextNode(current.getNextNode());
            current.getNextNode().setPrevNode(newNode);
            newNode.setPrevNode(current);
            current.setNextNode(newNode);
            size++;
        }
    }

    @Override
    public void removeData(T data) {
        if (headNode == null) {
            return; // La lista está vacía
        }

        Node<T> current = headNode;
        do {
            if (current.getData().equals(data)) {
                if (headNode == lastNode) {
                    headNode = lastNode = null;
                } else if (current == headNode) {
                    headNode = headNode.getNextNode();
                    headNode.setPrevNode(lastNode);
                    lastNode.setNextNode(headNode);
                } else if (current == lastNode) {
                    lastNode = lastNode.getPrevNode();
                    lastNode.setNextNode(headNode);
                    headNode.setPrevNode(lastNode);
                } else {
                    current.getPrevNode().setNextNode(current.getNextNode());
                    current.getNextNode().setPrevNode(current.getPrevNode());
                }
                size--;
                return;
            }
            current = current.getNextNode();
        } while (current != headNode);
    }

    @Override
    public void removeIndex(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index fuera del rango");
        }

        if (index == 0) {
            if (headNode == lastNode) {
                headNode = lastNode = null;
            } else {
                headNode = headNode.getNextNode();
                headNode.setPrevNode(lastNode);
                lastNode.setNextNode(headNode);
            }
        } else {
            Node<T> current = headNode;
            for (int i = 0; i < index; i++) {
                current = current.getNextNode();
            }
            current.getPrevNode().setNextNode(current.getNextNode());
            current.getNextNode().setPrevNode(current.getPrevNode());
            if (current == lastNode) {
                lastNode = current.getPrevNode();
            }
        }
        size--;
    }

    @Override
    public int search(T data) {
        if (headNode == null) {
            return -1;
        }

        Node<T> current = headNode;
        int index = 0;
        do {
            if (current.getData().equals(data)) {
                return index;
            }
            current = current.getNextNode();
            index++;
        } while (current != headNode);
        return -1;
    }

    @Override
    public T locate(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index fuera del rango");
        }

        Node<T> current = headNode;
        for (int i = 0; i < index; i++) {
            current = current.getNextNode();
        }
        return current.getData();
    }

    @Override
    public T get(Integer index) {
        return locate(index);
    }

    @Override
    public void clear() {
        headNode = lastNode = null;
        size = 0;
    }

    @Override
    public void println() {
        Node<T> currentNode = headNode;

        if(headNode == null){
            return;
        }

        while (currentNode.getNextNode() != headNode) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getNextNode();
        }
        System.out.print(currentNode.getData());
    }

    public void reversedPrintln() {

        if(lastNode == null){
            return;
        }

        Node<T> currentNode = lastNode;


        while (currentNode.getPrevNode() != lastNode) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getPrevNode();
        }
        System.out.print(currentNode.getData());
    }

    public int size() {
        return size;
    }

    @Override
    public int countAppearances(T data) {
        int count = 0;
        Node<T> current = headNode;

        while (current.getNextNode() != headNode) {
            if (current.getData().equals(data)) {
                count++;
            }
            current = current.getNextNode();
        }

        if (current.getData().equals(data)) {
            count++;
        }

        return count;
    }

    public void addAll(ListaEnlazadaSimpleCircular<T> list){
        for (T data : list) {
            add(data);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ListaEnlazadaDobleCircular{");
        if (headNode != null) {
            Node<T> currentNode = headNode;
            while (currentNode != lastNode) {
                sb.append(currentNode.getData()).append(" -> ");
                currentNode = currentNode.getNextNode();
            }
            sb.append(lastNode.getData());
        }
        sb.append("}");
        return sb.toString();
    }


    @Override
    public Iterator<T> iterator() {
        return new ListaEnlazadaDobleCircularIterator<>(headNode, lastNode);
    }

    private class ListaEnlazadaDobleCircularIterator<T> implements Iterator<T>{
        private Node<T> currentNode;
        private Node<T> lastNode;

        public ListaEnlazadaDobleCircularIterator(Node<T> headNode, Node<T> lastNode) {
            this.currentNode = headNode;
            this.lastNode = lastNode;
        }

        @Override
        public boolean hasNext() {
            return currentNode != lastNode;
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
