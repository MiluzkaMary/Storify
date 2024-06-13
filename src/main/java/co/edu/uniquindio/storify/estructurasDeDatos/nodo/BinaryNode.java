package co.edu.uniquindio.storify.estructurasDeDatos.nodo;

import lombok.Data;

import java.io.Serializable;

@Data
@SuppressWarnings("All")
public class BinaryNode <T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T data;
    private BinaryNode<T> leftNode;
    private BinaryNode<T> rightNode;

    public BinaryNode(T dato) {
        this.data = dato;
        this.leftNode = null;
        this.rightNode = null;
    }

    public BinaryNode(T dato, BinaryNode<T> leftNode, BinaryNode<T> rightNode) {
        this.data = dato;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    @Override
    public String toString() {
        return "binaryNode{" +
                "data=" + data +
                '}';
    }
}
