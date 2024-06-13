package co.edu.uniquindio.storify.estructurasDeDatos.listas;

public interface IListasEnlazadas <T> {
    public void addFirst(T data);

    public void add(T data);

    public void addIndex(Integer index, T data);

    public void removeData(T data);

    public void removeIndex(Integer index);

    public int search(T data);

    public T locate(Integer index);

    public T get(Integer index);

    public void clear();

    public void println();

    public int size();

    public int countAppearances(T data);

}
