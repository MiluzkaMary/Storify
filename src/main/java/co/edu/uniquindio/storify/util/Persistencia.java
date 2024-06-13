package co.edu.uniquindio.storify.util;

import co.edu.uniquindio.storify.model.TiendaMusica;

/**
 * Clase de utilidad para la persistencia de datos.
 */
public class Persistencia {

    // Ruta del archivo binario de la tienda
    private static final String RUTA_ARCHIVO_TIENDA_BINARIO = "src/main/resources/archivos/tienda.dat";

    /**
     * Guarda la tienda de música en un archivo binario.
     *
     * @param tiendaMusica La tienda de música a guardar.
     */
    public static void guardarRecursoBancoBinario(TiendaMusica tiendaMusica) {
        try {
            ArchivoUtil.salvarRecursoSerializado(RUTA_ARCHIVO_TIENDA_BINARIO, tiendaMusica);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Carga la tienda de música desde un archivo binario.
     *
     * @return La tienda de música cargada desde el archivo binario.
     */
    public static TiendaMusica cargarRecursoBancoBinario() {
        TiendaMusica tiendaMusica = null;

        try {
            tiendaMusica = (TiendaMusica) ArchivoUtil.cargarRecursoSerializado(RUTA_ARCHIVO_TIENDA_BINARIO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tiendaMusica;
    }
}
