package co.edu.uniquindio.storify.model;

import lombok.*;

import java.io.Serializable;

/**
 * La clase Cancion representa una canción en la plataforma Storify.
 * Implementa Serializable para permitir la serialización de sus instancias y Comparable para comparar canciones por su nombre.
 */
@SuppressWarnings("All")
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
public class Cancion implements Serializable, Comparable<Cancion> {
    private static final long serialVersionUID = 1L;
    private String codigo;
    private String nombre;
    private String album;
    private String caratula;
    private int anioLanzamiento;
    private String duracion;
    private TipoGenero genero;
    private String urlYoutube;

    /**
     * Constructor completo de la clase Cancion.
     *
     * @param codigo         el código de la canción.
     * @param nombre         el nombre de la canción.
     * @param album          el álbum al que pertenece la canción.
     * @param caratula       la carátula del álbum.
     * @param anioLanzamiento el año de lanzamiento de la canción.
     * @param duracion       la duración de la canción.
     * @param genero         el género musical de la canción.
     * @param urlYoutube     la URL del video de YouTube de la canción.
     */
    public Cancion(String codigo, String nombre, String album, String caratula, int anioLanzamiento, String duracion, TipoGenero genero, String urlYoutube) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.album = album;
        this.caratula = caratula;
        this.anioLanzamiento = anioLanzamiento;
        this.duracion = duracion;
        this.genero = genero;
        this.urlYoutube = urlYoutube;
    }

    /**
     * Compara esta canción con otra canción por nombre.
     *
     * @param o la canción con la cual comparar.
     * @return un valor negativo, cero o positivo según si esta canción es menor, igual o mayor que la canción especificada.
     */
    @Override
    public int compareTo(Cancion o) {
        return this.getNombre().compareTo(o.getNombre());
    }

    /**
     * Obtiene el género musical como una cadena de texto.
     *
     * @return una representación en cadena del género musical.
     */
    public String obtenerGeneroComoString() {
        switch (genero) {
            case ROCK:
                return "Rock";
            case POP:
                return "Pop";
            case SALSA:
                return "Salsa";
            case BACHATA:
                return "Bachata";
            case PUNK:
                return "Punk";
            case REGGAETON:
                return "Reggaeton";
            case ELECTRONICA:
                return "Electronica";
            case RB:
                return "R&B";
            case KPOP:
                return "K-POP";
            case OTRO:
                return "Otro";
            default:
                return "Desconocido";
        }
    }
}
