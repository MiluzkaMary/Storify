package co.edu.uniquindio.storify.model;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaDoble;
import lombok.*;

import java.io.Serializable;

/**
 * La clase Artista representa un artista en la plataforma Storify.
 * Implementa Serializable para permitir la serialización de sus instancias y Comparable para comparar artistas por su nombre.
 */
@SuppressWarnings("all")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Artista implements Serializable, Comparable<Artista> {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
    private String nacionalidad;
    private TipoArtista tipoArtista;
    private ListaEnlazadaDoble<Cancion> canciones;

    /**
     * Constructor de la clase Artista utilizando el patrón Builder de Lombok.
     * Inicializa la lista de canciones como una lista enlazada doble vacía.
     *
     * @param codigo       el código del artista.
     * @param nombre       el nombre del artista.
     * @param nacionalidad la nacionalidad del artista.
     * @param tipoArtista  el tipo de artista (solista, banda, etc.).
     */
    @Builder
    public Artista(String codigo, String nombre, String nacionalidad, TipoArtista tipoArtista) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.tipoArtista = tipoArtista;
        this.canciones = new ListaEnlazadaDoble<>();
    }

    /**
     * Obtiene el tipo de artista como una cadena de texto.
     *
     * @return una representación en cadena del tipo de artista.
     */
    public String obtenerTipoArtistaString() {
        return tipoArtista.toString();
    }

    /**
     * Obtiene la cantidad de canciones asociadas al artista.
     *
     * @return el número de canciones del artista.
     */
    public int obtenerCantidadCanciones() {
        return canciones.size();
    }

    /**
     * Agrega una canción a la lista de canciones del artista.
     *
     * @param cancion la canción a agregar.
     */
    public void agregarCancion(Cancion cancion) {
        canciones.add(cancion);
    }

    /**
     * Compara este artista con otro artista por nombre.
     *
     * @param o el artista con el cual comparar.
     * @return un valor negativo, cero o positivo según si este artista es menor, igual o mayor que el artista especificado.
     */
    @Override
    public int compareTo(Artista o) {
        return this.getNombre().compareTo(o.getNombre());
    }
}
