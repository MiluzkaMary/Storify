package co.edu.uniquindio.storify.util;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimple;
import co.edu.uniquindio.storify.model.Artista;
import co.edu.uniquindio.storify.model.Cancion;
import co.edu.uniquindio.storify.model.TipoArtista;
import co.edu.uniquindio.storify.model.TipoGenero;
import co.edu.uniquindio.storify.model.TiendaMusica;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de utilidad para operaciones de archivo.
 */
public class ArchivoUtil {

    /**
     * Carga los artistas desde un archivo especificado en una lista enlazada simple.
     *
     * @param ruta La ruta del archivo.
     * @return Una lista enlazada simple de artistas.
     * @throws IOException Si ocurre un error de lectura del archivo.
     */
    public static ListaEnlazadaSimple<Artista> cargarArtistasDesdeArchivo(String ruta) throws IOException {
        ListaEnlazadaSimple<Artista> artistas = new ListaEnlazadaSimple<>();
        List<String> contenido = leerArchivo(ruta);

        Artista artistaActual = null;
        boolean esEncabezadoArtistas = false;
        boolean esEncabezadoCanciones = false;

        for (String linea : contenido) {
            if (linea.startsWith("#Artistas")) {
                esEncabezadoArtistas = true;
                esEncabezadoCanciones = false;
                continue;
            } else if (linea.startsWith("#Canciones")) {
                esEncabezadoArtistas = false;
                esEncabezadoCanciones = true;
                continue;
            }

            String[] partes = linea.split(";");

            if (esEncabezadoArtistas && partes.length == 4) {
                artistaActual = new Artista(partes[0], partes[1], partes[2], TipoArtista.valueOf(partes[3]));
                artistas.add(artistaActual);
            } else if (esEncabezadoCanciones && partes.length == 8) {
                Cancion cancion = new Cancion(TiendaMusica.generarCodigoAleatorio(), partes[1], partes[2], partes[3], Integer.parseInt(partes[4]), (partes[5]), TipoGenero.valueOf(partes[6]), (partes[7]));

                // Buscar al artista correspondiente en la lista enlazada
                for (Artista artista : artistas) {
                    if (artista.getNombre().equals(partes[0])) {
                        artista.getCanciones().add(cancion);
                        break; // Una vez encontramos al artista, no es necesario seguir buscando
                    }
                }
            }
        }
        return artistas;
    }

    /**
     * Lee el contenido de un archivo y lo devuelve como una lista de cadenas de texto.
     *
     * @param ruta La ruta del archivo.
     * @return Una lista de cadenas de texto que representan las líneas del archivo.
     * @throws IOException Si ocurre un error de lectura del archivo.
     */
    private static List<String> leerArchivo(String ruta) throws IOException {
        List<String> contenido = new ArrayList<>();
        FileReader fr = new FileReader(ruta);
        BufferedReader bfr = new BufferedReader(fr);
        String linea = "";
        while ((linea = bfr.readLine()) != null) {
            contenido.add(linea);
        }
        bfr.close();
        fr.close();
        return contenido;
    }

    /**
     * Carga un recurso serializado desde un archivo.
     *
     * @param rutaArchivo La ruta del archivo.
     * @return El objeto serializado.
     * @throws Exception Si ocurre un error durante la carga del recurso.
     */
    @SuppressWarnings("unchecked")
    public static Object cargarRecursoSerializado(String rutaArchivo) throws Exception {
        Object aux = null;
        ObjectInputStream ois = null;
        try {
            // Se crea un ObjectInputStream
            ois = new ObjectInputStream(new FileInputStream(rutaArchivo));
            aux = ois.readObject();
        } catch (Exception e2) {
            throw e2;
        } finally {
            if (ois != null)
                ois.close();
        }
        return aux;
    }

    /**
     * Guarda un objeto serializado en un archivo.
     *
     * @param rutaArchivo La ruta del archivo.
     * @param object      El objeto a serializar.
     * @throws Exception Si ocurre un error durante la escritura del objeto en el archivo.
     */
    public static void salvarRecursoSerializado(String rutaArchivo, Object object) throws Exception {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(rutaArchivo));
            oos.writeObject(object);
        } catch (Exception e) {
            throw e;
        } finally {
            if (oos != null)
                oos.close();
        }
    }
}
