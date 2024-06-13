package co.edu.uniquindio.storify.estructurasDeDatos.listas;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import lombok.Data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("All")
@Data
public class ListaEnlazadaSimpleCircular<T> implements IListasEnlazadas<T>, Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;
    private Node<T> headNode;
    private Node<T> lastNode;
    private int size;

    public ListaEnlazadaSimpleCircular() {
        this.headNode = null;
        this.lastNode = null;
        this.size = 0;
    }

    @Override
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (headNode == null) {
            headNode = newNode;
            headNode.setNextNode(headNode);
            lastNode = headNode;
        } else {
            newNode.setNextNode(headNode);
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
            lastNode = headNode;
        } else {
            newNode.setNextNode(headNode);
            lastNode.setNextNode(newNode);
            lastNode = newNode;
        }
        size++;
    }

    @Override
    public void addIndex(Integer index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
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
            current.setNextNode(newNode);
            size++;
        }
    }

    @Override
    public void removeData(T data) {
        if (headNode == null) {
            return; // La lista está vacía
        }

        if (headNode.getData().equals(data)) {
            if (headNode.getNextNode() == headNode) {
                headNode = lastNode = null;
            } else {
                lastNode.setNextNode(headNode.getNextNode());
                headNode = headNode.getNextNode();
            }
            size--;
            return;
        }

        Node<T> current = headNode;
        while (current.getNextNode() != headNode) {
            if (current.getNextNode().getData().equals(data)) {
                if (current.getNextNode() == lastNode) {
                    lastNode = current;
                }
                current.setNextNode(current.getNextNode().getNextNode());
                size--;
                return;
            }
            current = current.getNextNode();
        }
    }

    @Override
    public void removeIndex(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        if (index == 0) {
            if (headNode.getNextNode() == headNode) {
                headNode = lastNode = null;
            } else {
                lastNode.setNextNode(headNode.getNextNode());
                headNode = headNode.getNextNode();
            }
        } else {
            Node<T> current = headNode;
            for (int i = 0; i < index - 1; i++) {
                current = current.getNextNode();
            }
            if (current.getNextNode() == lastNode) {
                lastNode = current;
            }
            current.setNextNode(current.getNextNode().getNextNode());
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
            throw new IndexOutOfBoundsException("Index out of bounds");
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

        if (headNode == null){
            return;
        }

        while (currentNode.getNextNode() != headNode) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getNextNode();
        }
        System.out.println(currentNode.getData());
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

    public void addAll(ListaEnlazadaSimpleCircular<T> listaEnlazadaSimpleCircular){
        for (T data : listaEnlazadaSimpleCircular) {
            add(data);
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ListaEnlazadaSimpleCircular{");
        Node<T> currentNode = headNode;
        while (currentNode != null) {
            sb.append(currentNode.getData());
            if (currentNode.getNextNode() != headNode) {
                sb.append(" -> ");
            }
            currentNode = currentNode.getNextNode();
            if (currentNode == headNode) {
                break;
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean contains(T cancion) {
        if (headNode == null) {
            return false;
        }

        Node<T> current = headNode;
        while (current.getNextNode() != headNode) {
            if (current.getData().equals(cancion)) {
                return true;
            }
            current = current.getNextNode();
        }
        return current.getData().equals(cancion);
    }



    @Override
    public Iterator<T> iterator() {
        return new ListaEnlazadaDobleIterator<T>(headNode);
    }

    private class ListaEnlazadaDobleIterator<T> implements Iterator<T>{
        private Node<T> currentNode;

        public ListaEnlazadaDobleIterator(Node<T> currentNode) {
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
