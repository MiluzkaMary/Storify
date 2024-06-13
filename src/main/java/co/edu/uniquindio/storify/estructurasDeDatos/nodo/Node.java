package co.edu.uniquindio.storify.estructurasDeDatos.nodo;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@SuppressWarnings("all")
@Data
@EqualsAndHashCode

public class Node<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private T data;
    private Node<T> nextNode;
    private Node<T> prevNode;

    public Node(T data) {
        this.data = data;
        this.nextNode = null;
        this.prevNode = null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }

}
