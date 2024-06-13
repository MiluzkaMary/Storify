package co.edu.uniquindio.storify.estructurasDeDatos.listas;

import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

@Data
@EqualsAndHashCode
@SuppressWarnings("All")
public class ListaEnlazadaDoble<T> implements IListasEnlazadas<T>, Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private Node<T> headNode;
    private Node<T> lastNode;
    private int size;

    public ListaEnlazadaDoble() {
        this.headNode = null;
        this.lastNode = null;
        this.size = 0;
    }

    public ListaEnlazadaDoble(ListaEnlazadaDoble<T> lista) {
         for (T data : lista) {
             add(data);
         }
    }

    @Override
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);

        if (headNode == null) {
            headNode = lastNode = newNode;
            size++;
            return;
        }

        newNode.setNextNode(headNode);
        headNode.setPrevNode(newNode);
        headNode = newNode;
        size++;
    }

    @Override
    public void add(T data) {
        if (headNode == null) {
            headNode = lastNode = new Node<>(data);
            size++;
            return;
        }

        Node<T> newNode = new Node<>(data);

        newNode.setPrevNode(lastNode);
        lastNode.setNextNode(newNode);
        lastNode = newNode;
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
            Node<T> currentNode = getNodeAtIndex(index);

            newNode.setNextNode(currentNode);
            newNode.setPrevNode(currentNode.getPrevNode());
            currentNode.getPrevNode().setNextNode(newNode);
            currentNode.setPrevNode(newNode);
            size++;
        }
    }

    @Override
    public void removeData(T data) {
        Node<T> current = headNode;

        while (current != null) {
            if (current.getData().equals(data)) {
                if (current == headNode) {
                    headNode = current.getNextNode();
                    if (headNode != null) {
                        headNode.setPrevNode(null);
                    }
                } else if (current == lastNode) {
                    lastNode = current.getPrevNode();
                    if (lastNode != null) {
                        lastNode.setNextNode(null);
                    }
                } else {
                    current.getPrevNode().setNextNode(current.getNextNode());
                    current.getNextNode().setPrevNode(current.getPrevNode());
                }
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
            headNode = headNode.getNextNode();
            if (headNode != null) {
                headNode.setPrevNode(null);
            }
        } else if (index == size - 1) {
            lastNode = lastNode.getPrevNode();
            if (lastNode != null) {
                lastNode.setNextNode(null);
            }
        } else {
            Node<T> currentNode = getNodeAtIndex(index);
            currentNode.getPrevNode().setNextNode(currentNode.getNextNode());
            currentNode.getNextNode().setPrevNode(currentNode.getPrevNode());
        }
        size--;
    }

    @Override
    public int search(T data) {
        int index = 0;
        Node<T> current = headNode;

        while (current != null) {
            if (current.getData().equals(data)) {
                return index;
            }
            current = current.getNextNode();
            index++;
        }

        return -1;
    }

    public boolean contains(T data) {
        int index = 0;
        Node<T> current = headNode;

        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNextNode();
            index++;
        }

        return false;
    }

    @Override
    public T locate(Integer index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index fuera de rango");
        }

        Node<T> node = getNodeAtIndex(index);
        return node.getData();
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

        if(headNode == null){
            return;
        }

        Node<T> currentNode = headNode;


        while (currentNode != null) {
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getNextNode();
        }
    }

    public void reversedPrintln() {
        if(lastNode == null){
            return;
        }

        Node<T> currentNode = lastNode;

        while(currentNode != null){
            System.out.print(currentNode.getData() + " ");
            currentNode = currentNode.getPrevNode();
        }

    }

    @Override
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

    public void addAll(ListaEnlazadaDoble<T> list){
        for (T data : list) {
            add(data);
        }
    }

    public ListaEnlazadaSimpleCircular<T> convertirListaDobleASimpleCircular() {
        ListaEnlazadaSimpleCircular<T> listaSimpleCircular = new ListaEnlazadaSimpleCircular<>();

        if (headNode == null) {
            return listaSimpleCircular; // Si la lista doble está vacía, la lista simple circular también estará vacía
        }

        // Agregar cada elemento de la lista doble al final de la lista simple circular
        Node<T> currentNode = headNode;
        do {
            listaSimpleCircular.add(currentNode.getData());
            currentNode = currentNode.getNextNode();
        } while (currentNode != null);

        return listaSimpleCircular;
    }



    private Node<T> getNodeAtIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index fuera de rango");
        }

        Node<T> current = headNode;
        for (int i = 0; i < index; i++) {
            current = current.getNextNode();
        }
        return current;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ListaEnlazadaDoble{");
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

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("ListaEnlazadaDoble{");
            Node<T> currentNode = (Node<T>) headNode;
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

    }
}
