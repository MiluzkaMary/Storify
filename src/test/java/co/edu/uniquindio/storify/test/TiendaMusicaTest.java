package co.edu.uniquindio.storify.test;

import co.edu.uniquindio.storify.controllers.controladorFlujo.Comando;
import co.edu.uniquindio.storify.controllers.controladorFlujo.ComandoAgregarCancion;
import co.edu.uniquindio.storify.controllers.controladorFlujo.ComandoEliminarCancion;
import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.exceptions.*;
import co.edu.uniquindio.storify.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de prueba para la tienda de música.
 */
class TiendaMusicaTest {

    private TiendaMusica tienda;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        tienda = new TiendaMusica();
    }

    /**
     * Prueba para agregar un artista a la tienda.
     *
     * @throws ArtistasYaEnTiendaException si el artista ya está en la tienda.
     */
    @Test
    void agregarArtista() throws ArtistasYaEnTiendaException {
        Artista artista = new Artista("123", "Cristian", "Colombia", TipoArtista.SOLISTA);
        assertDoesNotThrow(() -> tienda.agregarArtista(artista));
        assertThrows(ArtistasYaEnTiendaException.class, () -> tienda.agregarArtista(artista));
    }

    /**
     * Prueba para crear un artista en la tienda.
     *
     * @throws AtributoVacioException si algún atributo está vacío.
     */
    @Test
    void crearArtista() throws AtributoVacioException {
        assertDoesNotThrow(() -> tienda.crearArtista("Cristian", "123", "Colombia", "SOLISTA"));
        assertThrows(AtributoVacioException.class, () -> tienda.crearArtista("Cristian", "123", "Colombia", ""));
    }

    /**
     * Prueba para crear una canción en la tienda.
     */
    @Test
    void crearCancion() {
        assertDoesNotThrow(() -> tienda.crearCancion("Reminisencias", "Exitos",
                "caratula", "1980", "5.30", "otro","www.youtube.com"));
        assertThrows(AtributoVacioException.class, () -> tienda.crearCancion("Reminisencias", "Exitos",
                "caratula", "1980", "5:30", "","www.youtube.com"));
    }

    /**
     * Prueba para generar un código aleatorio.
     */
    @Test
    void generarCodigoAleatorio() {
        assertNotNull(tienda.generarCodigoAleatorio());
    }

    /**
     * Prueba para agregar una canción a la tienda.
     *
     * @throws CancionYaRegistradaException si la canción ya está registrada.
     */
    @Test
    void agregarCancion() throws CancionYaRegistradaException {
        Artista artista = new Artista("123", "Cristian", "Colombia", TipoArtista.SOLISTA);
        Cancion cancion = new Cancion("1234","Reminisencias", "Exitos", "caratula", 1980, "5.30", TipoGenero.POP, "www.youtube.com");
        assertTrue(tienda.agregarCancion(cancion, artista));
        assertThrows(CancionYaRegistradaException.class, () -> tienda.agregarCancion(cancion, artista));
    }

    /**
     * Prueba para crear un cliente en la tienda.
     */
    @Test
    void crearCliente() {
        assertDoesNotThrow(() -> tienda.crearCliente("Maria", "Velez"));
        assertThrows(AtributoVacioException.class, () -> tienda.crearCliente("Maria", ""));
    }

    /**
     * Prueba para crear un usuario en la tienda.
     */
    @Test
    void crearUsuario() {
        Persona persona = new Cliente("Maria", "Velez");
        assertDoesNotThrow(() -> tienda.crearUsuario("MariaVelez", "123", "mariaVelez@gmail.com", persona));
        assertThrows(AtributoVacioException.class, () -> tienda.crearUsuario("MariaVelez", "123", "", persona));
    }

    /**
     * Prueba para agregar un usuario a la tienda.
     */
    @Test
    void agregarUsuario() {
        Usuario usuario = new Usuario("MariaVelez", "123", "mariaVelez@gmail.com", new Persona("Maria", "Velez"));
        assertDoesNotThrow(() -> tienda.agregarUsuario(usuario));
        assertThrows(UsuarioYaRegistradoException.class, () -> tienda.agregarUsuario(new Usuario("MariaVelez", "123", "mariaVelez@gmail.com", new Persona("Maria", "Velez"))));
    }

    /**
     * Prueba para buscar un usuario en la tienda.
     *
     * @throws UsuarioYaRegistradoException si el usuario ya está registrado.
     */
    @Test
    void buscarUsuario() throws UsuarioYaRegistradoException {
        Usuario usuario = new Usuario("MariaVelez", "123", "mariaVelez@gmail.com", new Persona("Maria", "Velez"));
        tienda.agregarUsuario(usuario);
        assertDoesNotThrow(() -> tienda.buscarUsuario("MariaVelez", "123"));
        assertThrows(AtributoVacioException.class, () -> tienda.buscarUsuario("", "123"));
        assertThrows(UsuarioNoExistenteException.class, () -> tienda.buscarUsuario("MariaVeles", "1234"));
    }

    /**
     * Prueba para obtener el tipo de usuario en la tienda.
     *
     * @throws UsuarioYaRegistradoException si el usuario ya está registrado.
     */
    @Test
    void obtenerTipoUsuario() throws UsuarioYaRegistradoException {
        Usuario usuario = new Usuario("MariaVelez", "123", "mariaVelez@gmail.com", new Cliente("Maria", "Velez"));
        tienda.agregarUsuario(usuario);

        assertDoesNotThrow(() -> tienda.obtenerTipoUsuario("MariaVelez", "123"));
        assertThrows(AtributoVacioException.class, () -> tienda.obtenerTipoUsuario("", "123"));
        assertThrows(UsuarioNoExistenteException.class, () -> tienda.obtenerTipoUsuario("MariaVeles", "1234"));
    }

    /**
     * Prueba para buscar canciones por artista en la tienda.
     *
     * @throws ArtistaNoEncontradoException si el artista no es encontrado.
     * @throws ArtistasYaEnTiendaException si el artista ya está en la tienda.
     * @throws CancionYaRegistradaException si la canción ya está registrada.
     * @throws InterruptedException si la operación es interrumpida.
     */
    @Test
    void buscarCancionesPorArtista() throws ArtistaNoEncontradoException, ArtistasYaEnTiendaException, CancionYaRegistradaException, InterruptedException {
        Artista artista = new Artista("Juan", "Codigo", "Nacionalidad", TipoArtista.SOLISTA);
        Cancion cancion1 = new Cancion("123","Cancion1", "Album1", "Caratula1", 2022, "4.5", TipoGenero.ROCK, "url1");
        Cancion cancion2 = new Cancion("1233","Cancion2", "Album2", "Caratula2", 2023, "3.5", TipoGenero.POP, "url2");
        tienda.agregarArtista(artista);
        tienda.agregarCancion(cancion1,artista);
        tienda.agregarCancion(cancion2,artista);

        assertEquals(2, tienda.buscarCancionesPorArtista("Codigo").size());
        assertThrows(ArtistaNoEncontradoException.class, () -> tienda.buscarCancionesPorArtista("ArtistaInexistente"));

    }

    /**
     * Prueba para cargar artistas desde un archivo.
     */
    @Test
    void cargarArtistasDesdeArchivo() {
        // Implementar la prueba cuando esté disponible
    }

    /**
     * Prueba para obtener el género con más canciones en la tienda.
     *
     * @throws ArtistasYaEnTiendaException si el artista ya está en la tienda.
     * @throws InterruptedException si la operación es interrumpida.
     */
    @Test
    void obtenerGeneroConMasCanciones() throws ArtistasYaEnTiendaException, InterruptedException {
        Artista artista = new Artista("1234", "Codigo", "Nacionalidad", TipoArtista.SOLISTA);
        tienda.agregarArtista(artista);
        assertDoesNotThrow(() -> tienda.obtenerGeneroConMasCanciones());
    }


    /**
     * Prueba para el comando de agregar canción.
     */
    @Test
    public void testComandoAgregarCancion() {
        Cliente usuario = new Cliente("usuarioPrueba","Velez");
        Cancion cancion = new Cancion("SONG001", "Die For You", "Starboy", "/imagenes/starboy.jpeg", 2016, "4:20", TipoGenero.OTRO, "https://www.youtube.com/watch?v=mTLQhPFx2nM&list=PLWGXKDxW301QZrzSl7hLzdYakFdayHC4l&index=17&ab_channel=TheWeeknd-Topic");
        Cancion cancion1 = new Cancion("SONG002", "Die For You34", "Starbo3434y", "/imagenes/starboy.jpeg", 2016, "4:20", TipoGenero.OTRO, "https://www.youtube.com/watch?v=mTLQhPFx2nM&list=PLWGXKDxW301QZrzSl7hLzdYakFdayHC4l&index=17&ab_channel=TheWeeknd-Topic");
        usuario.agregarCancionFavorita(cancion1);
        Comando agregarCancion = new ComandoAgregarCancion(usuario, cancion);

        agregarCancion.ejecutar();
        assertTrue(usuario.getCancionesFavoritas().contains(cancion));

        agregarCancion.deshacer();
        assertFalse(usuario.getCancionesFavoritas().contains(cancion));

        agregarCancion.rehacer();
        assertTrue(usuario.getCancionesFavoritas().contains(cancion));
    }

    /**
     * Prueba para el comando de eliminar canción.
     */
    @Test
    public void testComandoEliminarCancion() {
        Cliente usuario = new Cliente("usuarioPrueba","Velez");
        Cancion cancion = new Cancion("SONG001", "Die For You", "Starboy", "/imagenes/starboy.jpeg", 2016, "4:20", TipoGenero.OTRO, "https://www.youtube.com/watch?v=mTLQhPFx2nM&list=PLWGXKDxW301QZrzSl7hLzdYakFdayHC4l&index=17&ab_channel=TheWeeknd-Topic");
        Cancion cancion1 = new Cancion("SONG002", "Die For You", "Starboy", "/imagenes/starboy.jpeg", 2016, "4:20", TipoGenero.OTRO, "https://www.youtube.com/watch?v=mTLQhPFx2nM&list=PLWGXKDxW301QZrzSl7hLzdYakFdayHC4l&index=17&ab_channel=TheWeeknd-Topic");
        usuario.agregarCancionFavorita(cancion);
        usuario.agregarCancionFavorita(cancion1);
        Comando eliminarCancion = new ComandoEliminarCancion(usuario, cancion);

        eliminarCancion.ejecutar();
        assertFalse(usuario.getCancionesFavoritas().contains(cancion));

        eliminarCancion.deshacer();
        assertTrue(usuario.getCancionesFavoritas().contains(cancion));

        eliminarCancion.rehacer();
        assertFalse(usuario.getCancionesFavoritas().contains(cancion));
    }

    /**
     * Prueba para buscar un nodo en el árbol binario.
     */
    @Test
    void testBuscar() {
        BinaryTree<String> arbol = new BinaryTree<>();
        arbol.insert("Beatles");
        arbol.insert("Queen");
        arbol.insert("Led Zeppelin");
        arbol.insert("Pink Floyd");

        try {
            String resultado = arbol.find("Queen");
            assertNotNull(resultado);
            assertEquals("Queen", resultado);
        } catch (InterruptedException e) {
            fail("La búsqueda fue interrumpida: " + e.getMessage());
        }
    }

    /**
     * Prueba para buscar un nodo no existente en el árbol binario.
     */
    @Test
    void testBuscarNoEncontrado() {
        BinaryTree<String> arbol = new BinaryTree<>();
        arbol.insert("Beatles");
        arbol.insert("Queen");
        arbol.insert("Led Zeppelin");
        arbol.insert("Pink Floyd");

        try {
            String resultado = arbol.find("Nirvana");
            assertNull(resultado);
        } catch (InterruptedException e) {
            fail("La búsqueda fue interrumpida: " + e.getMessage());
        }
    }

    /**
     * Prueba para buscar la raíz en el árbol binario.
     */
    @Test
    void testBuscarRaiz() {
        BinaryTree<String> arbol = new BinaryTree<>();
        arbol.insert("Beatles");
        arbol.insert("Queen");
        arbol.insert("Led Zeppelin");
        arbol.insert("Pink Floyd");

        try {
            String resultado = arbol.find("Beatles");
            assertNotNull(resultado);
            assertEquals("Beatles", resultado);
        } catch (InterruptedException e) {
            fail("La búsqueda fue interrumpida: " + e.getMessage());
        }
    }
}
