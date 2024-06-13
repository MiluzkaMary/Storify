package co.edu.uniquindio.storify.model;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimpleCircular;
import lombok.*;

import java.io.Serializable;

/**
 * La clase Cliente representa un cliente en la plataforma Storify.
 * Hereda de la clase Persona e implementa Serializable para permitir la serialización de sus instancias y Comparable para comparar clientes por su nombre.
 */
@SuppressWarnings("all")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Cliente extends Persona implements Serializable, Comparable<Cliente> {
    private static final long serialVersionUID = 1L;
    private ListaEnlazadaSimpleCircular<Cancion> cancionesFavoritas;

    /**
     * Constructor de la clase Cliente.
     *
     * @param nombre  el nombre del cliente.
     * @param apellido el apellido del cliente.
     */
    @Builder
    public Cliente(String nombre, String apellido) {
        super(nombre, apellido);
        this.cancionesFavoritas = new ListaEnlazadaSimpleCircular<>();
    }

    /**
     * Agrega una canción a la lista de canciones favoritas del cliente.
     *
     * @param cancion la canción a agregar.
     */
    public void agregarCancionFavorita(Cancion cancion) {
        cancionesFavoritas.add(cancion);
    }

    /**
     * Elimina una canción de la lista de canciones favoritas del cliente.
     *
     * @param cancion la canción a eliminar.
     */
    public void eliminarCancionFavorita(Cancion cancion) {
        cancionesFavoritas.removeData(cancion);
    }

    /**
     * Compara este cliente con otro cliente por nombre.
     *
     * @param o el cliente con el cual comparar.
     * @return un valor negativo, cero o positivo según si este cliente es menor, igual o mayor que el cliente especificado.
     */
    @Override
    public int compareTo(Cliente o) {
        return this.getNombre().compareTo(o.getNombre());
    }
}
