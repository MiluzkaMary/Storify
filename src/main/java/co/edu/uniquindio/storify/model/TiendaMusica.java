package co.edu.uniquindio.storify.model;

import co.edu.uniquindio.storify.estructurasDeDatos.arbolBinario.BinaryTree;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaDoble;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimpleCircular;
import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimple;
import co.edu.uniquindio.storify.estructurasDeDatos.nodo.Node;
import co.edu.uniquindio.storify.exceptions.*;
import co.edu.uniquindio.storify.util.ArchivoUtil;
import co.edu.uniquindio.storify.util.YouTubeHelper;
import lombok.Data;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * La clase TiendaMusica representa la tienda de música digital Storify, encargada de administrar
 * los artistas, canciones y usuarios registrados en la plataforma.
 * <p>
 * Esta clase proporciona métodos para agregar, editar y buscar artistas, canciones y usuarios,
 * así como para crear nuevos objetos de tipo Artista, Cancion y Usuario.
 * </p>
 *
 * @see Usuario
 * @see Artista
 * @see Cancion
 * @see Administrador
 * @see ArtistasYaEnTiendaException
 * @see AtributoVacioException
 * @see CancionYaRegistradaException
 * @see UsuarioYaRegistradoException
 * @see UsuarioNoExistenteException
 * @see EmailInvalidoException
 *
 * @version 1.0.0
 */
@Data
@ToString
@SuppressWarnings("All")
public class TiendaMusica implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * El nombre de la tienda de música.
     */
    private final String nombre = "Storify";

    /**
     * La versión de la tienda de música.
     */
    private final String version = "1.0.0";

    /**
     * Mapa que almacena a los usuarios registrados en la tienda de música, utilizando
     * como clave el nombre de usuario.
     */
    private HashMap<String, Usuario> usuarios;

    /**
     * Árbol binario que contiene a los artistas registrados en la tienda de música.
     */
    private BinaryTree<Artista> artistas;

    /**
     * El administrador de la tienda de música.
     */
    private Administrador administrador;

    /**
     * Constructor de la clase TiendaMusica.
     * Inicializa las estructuras de datos para almacenar usuarios y artistas,
     * y crea al administrador por defecto.
     */
    public TiendaMusica() {
        this.usuarios = new HashMap<>();
        this.artistas = new BinaryTree<>();
        this.administrador = crearAdministrador();
    }

    /**
     * Método privado para crear un administrador por defecto.
     *
     * @return El administrador creado.
     */
    private Administrador crearAdministrador() {
        Administrador administrador = new Administrador();
        return administrador;
    }

    /**
     * Agrega un nuevo artista a la tienda de música.
     *
     * @param artista El artista a agregar.
     * @throws ArtistasYaEnTiendaException Si el artista ya está registrado en la tienda.
     * @throws InterruptedException        Si se interrumpe el hilo mientras espera.
     */
    public void agregarArtista(Artista artista) throws ArtistasYaEnTiendaException, InterruptedException {
        if (artistas.find(artista) != null) {
            throw new ArtistasYaEnTiendaException("El artista ya existe en la tienda.");
        }
        artistas.insert(artista);
    }

    /**
     * Crea un nuevo objeto de tipo Artista con los atributos proporcionados.
     *
     * @param nombre      El nombre del artista.
     * @param codigo      El código del artista.
     * @param nacionalidad La nacionalidad del artista.
     * @param tipoArtista El tipo de artista.
     * @return El objeto Artista creado.
     * @throws AtributoVacioException Si algún atributo requerido está vacío.
     */
    public Artista crearArtista(String nombre, String codigo, String nacionalidad, String tipoArtista) throws AtributoVacioException {
        if (nombre == null || nombre.isBlank()) {
            throw new AtributoVacioException("El nombre es obligatorio");
        }
        if (codigo == null || codigo.isBlank()) {
            throw new AtributoVacioException("El código es obligatorio");
        }
        if (nacionalidad == null || nacionalidad.isBlank()) {
            throw new AtributoVacioException("La nacionalidad es obligatoria");
        }
        if (tipoArtista == null || tipoArtista.isBlank()) {
            throw new AtributoVacioException("El tipo de artista es obligatorio");
        }
        Artista artistaNuevo = Artista.builder()
                .codigo(codigo)
                .nombre(nombre)
                .nacionalidad(nacionalidad)
                .tipoArtista(TipoArtista.valueOf(tipoArtista.toUpperCase()))
                .build();
        return artistaNuevo;
    }

    /**
     * Edita un artista existente en la tienda de música.
     *
     * @param artistaAntiguo      El artista a editar.
     * @param nombre              El nuevo nombre del artista.
     * @param codigo              El nuevo código del artista.
     * @param nacionalidad        La nueva nacionalidad del artista.
     * @param tipoArtista         El nuevo tipo de artista.
     * @param listaNuevaCanciones La nueva lista de canciones del artista.
     * @return El objeto Artista editado.
     * @throws AtributoVacioException     Si algún atributo requerido está vacío.
     * @throws ArtistaNoEncontradoException Si el artista no se encuentra en la tienda.
     */
    public Artista editarArtista(Artista artistaAntiguo, String nombre, String codigo, String nacionalidad, String tipoArtista, ListaEnlazadaDoble<Cancion> listaNuevaCanciones) throws AtributoVacioException, ArtistaNoEncontradoException {
        Artista artistaNuevo = crearArtista(nombre, codigo, nacionalidad, tipoArtista);
        artistaNuevo.setCanciones(listaNuevaCanciones);
        artistas.reemplazarValor(artistaAntiguo, artistaNuevo);

        return artistaNuevo;
    }

    /**
     * Edita una canción existente en la tienda de música.
     *
     * @param cancionAntigua El objeto Cancion a editar.
     * @param nombre         El nuevo nombre de la canción.
     * @param nombreAlbum    El nuevo nombre del álbum.
     * @param caratula       La nueva carátula.
     * @param anio           El nuevo año de lanzamiento.
     * @param duracion       La nueva duración.
     * @param genero         El nuevo género.
     * @param urlYoutube     La nueva URL de YouTube.
     * @return El objeto Cancion editado.
     * @throws AtributoVacioException     Si algún atributo requerido está vacío.
     * @throws ArtistaNoEncontradoException Si el artista de la canción no se encuentra en la tienda.
     */
    public Cancion editarCancion(Cancion cancionAntigua, String nombre, String nombreAlbum, String caratula, String anio, String duracion, String genero, String urlYoutube) throws AtributoVacioException, ArtistaNoEncontradoException {
        Cancion cancionNueva = crearCancion(nombre, nombreAlbum, caratula, anio, duracion, genero, urlYoutube);
        Artista artista = buscarArtistaCancion(cancionAntigua);
        ListaEnlazadaDoble<Cancion> cancionesArtista = artista.getCanciones();
        Node<Cancion> current = cancionesArtista.getHeadNode();
        while (current != null) {
            if (current.getData().equals(cancionAntigua)) {
                current.setData(cancionNueva);
                actualizarCancionUsuarioFav(cancionNueva, cancionAntigua);
                break;
            }
            current = current.getNextNode();
        }
        return cancionNueva;
    }

    /**
     * Edita un usuario existente en la tienda de música.
     *
     * @param usuarioAntiguo El usuario a editar.
     * @param nuevoUsername  El nuevo nombre de usuario.
     * @param nuevoPassword  La nueva contraseña.
     * @param nuevoEmail     El nuevo email.
     * @param nuevoNombre    El nuevo nombre.
     * @param nuevoApellido  El nuevo apellido.
     * @return El objeto Usuario editado.
     * @throws AtributoVacioException     Si algún atributo requerido está vacío.
     * @throws EmailInvalidoException     Si el formato del email es incorrecto.
     * @throws UsuarioNoExistenteException Si el usuario no se encuentra en la tienda.
     */
    public Usuario editarUsuario(Usuario usuarioAntiguo, String nuevoUsername, String nuevoPassword, String nuevoEmail, String nuevoNombre, String nuevoApellido) throws AtributoVacioException, EmailInvalidoException, UsuarioNoExistenteException {
        if (nuevoUsername.isEmpty() || nuevoPassword.isEmpty() || nuevoEmail.isEmpty() || nuevoNombre.isEmpty() || nuevoApellido.isEmpty()) {
            throw new AtributoVacioException("Todos los campos son obligatorios");
        }
        if (!isValidEmail(nuevoEmail)) {
            throw new EmailInvalidoException("El formato del email es incorrecto");
        }
        for (Usuario usuario : usuarios.values()) {
            if (usuario.equals(usuarioAntiguo)) {
                Cliente nuevaPersona = (Cliente) usuarioAntiguo.getPersona();
                nuevaPersona.setNombre(nuevoNombre);
                nuevaPersona.setApellido(nuevoApellido);

                usuario.setEmail(nuevoEmail);
                usuario.setPassword(nuevoPassword);
                usuario.setUsername(nuevoUsername);
                usuario.setPersona(nuevaPersona);

                return usuario;
            }
        }
        throw new UsuarioNoExistenteException("Usuario no encontrado para edición");
    }

    /**
     * Método para validar el formato de un email.
     *
     * @param email El email a validar.
     * @return true si el email es válido, false en caso contrario.
     */
    private boolean isValidEmail(String email) {
        return email.endsWith("@gmail.com") || email.endsWith("@uqvirtual.edu.co");
    }

    /**
     * Actualiza las canciones favoritas de los usuarios si una canción es editada.
     *
     * @param cancionNueva  La nueva canción.
     * @param cancionAntigua La canción antigua.
     */
    public void actualizarCancionUsuarioFav(Cancion cancionNueva, Cancion cancionAntigua) {
        for (Usuario usuario : usuarios.values()) {
            Persona persona = usuario.getPersona();
            if (persona instanceof Cliente) {
                Cliente cliente = (Cliente) usuario.getPersona();
                ListaEnlazadaSimpleCircular<Cancion> cancionesFavoritas = cliente.getCancionesFavoritas();
                if (cancionesFavoritas.getHeadNode() != null) {
                    Node<Cancion> currentFavorita = cancionesFavoritas.getHeadNode();
                    do {
                        if (currentFavorita.getData().equals(cancionAntigua)) {
                            currentFavorita.setData(cancionNueva);
                            break;
                        }
                        currentFavorita = currentFavorita.getNextNode();
                    } while (currentFavorita != cancionesFavoritas.getHeadNode());
                }
            }
        }
    }

    /**
     * Crea una nueva canción con los atributos proporcionados.
     *
     * @param nombre      El nombre de la canción.
     * @param nombreAlbum El nombre del álbum.
     * @param caratula    La carátula de la canción.
     * @param anio        El año de lanzamiento.
     * @param duracion    La duración de la canción.
     * @param genero      El género de la canción.
     * @param urlYoutube  La URL de YouTube.
     * @return El objeto Cancion creado.
     * @throws AtributoVacioException Si algún atributo requerido está vacío.
     */
    public Cancion crearCancion(String nombre, String nombreAlbum, String caratula, String anio, String duracion, String genero, String urlYoutube) throws AtributoVacioException {
        if (nombre == null || nombre.isBlank()) {
            throw new AtributoVacioException("El nombre es obligatorio");
        }
        if (nombreAlbum == null || nombreAlbum.isBlank()) {
            throw new AtributoVacioException("El nombre del álbum es obligatorio");
        }
        if (caratula == null || caratula.isBlank()) {
            throw new AtributoVacioException("La carátula es obligatoria");
        }
        if (anio == null || anio.isBlank()) {
            throw new AtributoVacioException("El año es obligatorio");
        }
        if (duracion == null || duracion.isBlank()) {
            throw new AtributoVacioException("La duración de la canción es obligatoria");
        }
        if (genero == null || genero.isBlank()) {
            throw new AtributoVacioException("El género de la canción es obligatorio");
        }
        if (urlYoutube == null || urlYoutube.isBlank()) {
            throw new AtributoVacioException("La URL de YouTube es obligatoria");
        }

        Cancion cancionNueva = Cancion.builder()
                .codigo(generarCodigoAleatorio())
                .nombre(nombre)
                .album(nombreAlbum)
                .caratula(caratula)
                .anioLanzamiento(Integer.parseInt(anio))
                .duracion(duracion)
                .genero(TipoGenero.valueOf(genero.toUpperCase()))
                .urlYoutube(urlYoutube)
                .build();

        return cancionNueva;
    }

    /**
     * Genera un código aleatorio para las canciones.
     *
     * @return El código generado.
     */
    public static String generarCodigoAleatorio() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char letra = (char) (random.nextInt(26) + 'a');
            codigo.append(letra);
        }
        for (int i = 0; i < 3; i++) {
            int digito = random.nextInt(10);
            codigo.append(digito);
        }
        return codigo.toString();
    }

    /**
     * Agrega una nueva canción a la lista de canciones de un artista.
     *
     * @param cancion La canción a agregar.
     * @param artista El artista al que pertenece la canción.
     * @return true si la canción fue agregada exitosamente, false en caso contrario.
     * @throws CancionYaRegistradaException Si la canción ya está registrada en la lista del artista.
     */
    public boolean agregarCancion(Cancion cancion, Artista artista) throws CancionYaRegistradaException {
        ListaEnlazadaDoble<Cancion> canciones = artista.getCanciones();
        if (canciones.contains(cancion)) {
            throw new CancionYaRegistradaException("La canción ya está en las canciones del artista");
        }
        canciones.add(cancion);
        return true;
    }

    /**
     * Crea un nuevo cliente con los atributos proporcionados.
     *
     * @param nombre   El nombre del cliente.
     * @param apellido El apellido del cliente.
     * @return El objeto Cliente creado.
     * @throws AtributoVacioException Si algún atributo requerido está vacío.
     */
    public Cliente crearCliente(String nombre, String apellido) throws AtributoVacioException {
        if (nombre == null || nombre.isBlank()) {
            throw new AtributoVacioException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new AtributoVacioException("El apellido es obligatorio");
        }

        Cliente clienteNuevo = Cliente.builder()
                .nombre(nombre)
                .apellido(apellido)
                .build();

        return clienteNuevo;
    }

    /**
     * Crea un nuevo usuario con los atributos proporcionados.
     *
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @param email    El email del usuario.
     * @param persona  El objeto Persona asociado al usuario.
     * @return El objeto Usuario creado.
     * @throws AtributoVacioException Si algún atributo requerido está vacío.
     */
    public Usuario crearUsuario(String username, String password, String email, Persona persona) throws AtributoVacioException {
        if (username == null || username.isBlank()) {
            throw new AtributoVacioException("El username es obligatorio");
        }
        if (password == null || password.isBlank()) {
            throw new AtributoVacioException("La contraseña es obligatoria");
        }
        if (email == null || email.isBlank()) {
            throw new AtributoVacioException("El email es obligatorio");
        }

        Usuario usuarioNuevo = Usuario.builder()
                .username(username)
                .password(password)
                .email(email)
                .persona(persona)
                .build();

        return usuarioNuevo;
    }

    /**
     * Agrega un nuevo usuario a la tienda de música.
     *
     * @param usuario El usuario a agregar.
     * @return true si el usuario fue agregado exitosamente, false en caso contrario.
     * @throws UsuarioYaRegistradoException Si el usuario ya está registrado en la tienda.
     */
    public boolean agregarUsuario(Usuario usuario) throws UsuarioYaRegistradoException {
        String usernameCliente = usuario.getUsername();
        Usuario usuarioExistente = getUsuarios().putIfAbsent(usernameCliente, usuario);
        if (usuarioExistente != null) {
            throw new UsuarioYaRegistradoException("El usuario ya existe en la base de datos");
        }
        return true;
    }

    /**
     * Busca un usuario en la tienda de música por su nombre de usuario y contraseña.
     *
     * @param username   El nombre de usuario.
     * @param contrasenia La contraseña del usuario.
     * @return El objeto Usuario encontrado.
     * @throws AtributoVacioException     Si algún atributo requerido está vacío.
     * @throws UsuarioNoExistenteException Si el usuario no se encuentra en la tienda.
     */
    public Usuario buscarUsuario(String username, String contrasenia) throws AtributoVacioException, UsuarioNoExistenteException {
        if (username == null || username.isBlank()) {
            throw new AtributoVacioException("El username es obligatorio");
        }
        if (contrasenia == null || contrasenia.isBlank()) {
            throw new AtributoVacioException("La contraseña es obligatorio");
        }

        for (Usuario usuario : usuarios.values()) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(contrasenia)) {
                return usuario;
            }
        }
        throw new UsuarioNoExistenteException("Usuario no encontrado");
    }

    /**
     * Obtiene el tipo de usuario (Cliente o Administrador) basado en el nombre de usuario y la contraseña.
     *
     * @param username   El nombre de usuario.
     * @param contrasenia La contraseña del usuario.
     * @return El tipo de usuario (Cliente o Administrador).
     * @throws AtributoVacioException     Si algún atributo requerido está vacío.
     * @throws UsuarioNoExistenteException Si el usuario no se encuentra en la tienda.
     */
    public String obtenerTipoUsuario(String username, String contrasenia) throws AtributoVacioException, UsuarioNoExistenteException {
        Usuario usuario = buscarUsuario(username, contrasenia);
        Persona persona = usuario.getPersona();
        if (persona instanceof Cliente) {
            return "Cliente";
        } else if (persona instanceof Administrador) {
            return "Administrador";
        }

        throw new UsuarioNoExistenteException("Usuario no encontrado");
    }

    /**
     * Busca canciones por el nombre del artista.
     *
     * @param nombreArtista El nombre del artista.
     * @return Una lista de canciones del artista.
     * @throws ArtistaNoEncontradoException Si el artista no se encuentra en la tienda.
     * @throws InterruptedException Si el hilo es interrumpido.
     */
    public ListaEnlazadaDoble<Cancion> buscarCancionesPorArtista(String nombreArtista) throws ArtistaNoEncontradoException, InterruptedException {
        ListaEnlazadaDoble<Cancion> cancionesArtista = new ListaEnlazadaDoble<>();
        Artista artista = new Artista(null, nombreArtista, null, null);

        Artista artista1 = artistas.find(artista);

        throw new ArtistaNoEncontradoException("El artista seleccionado no existe");
    }

    /**
     * Busca el artista de una canción dada.
     *
     * @param cancion La canción cuya artista se desea buscar.
     * @return El artista de la canción.
     * @throws ArtistaNoEncontradoException Si no se encuentra ningún artista que coincida con la canción especificada.
     */
    public Artista buscarArtistaCancion(Cancion cancion) throws ArtistaNoEncontradoException {
        for (Artista artista : artistas.iterator()) {
            ListaEnlazadaDoble<Cancion> cancionesArtista = artista.getCanciones();
            for (Cancion cancionArtista : cancionesArtista) {
                if (cancionArtista.equals(cancion)) {
                    return artista;
                }
            }
        }
        throw new ArtistaNoEncontradoException("Ningún artista coincide con la canción especificada");
    }

    public Artista buscarArtistaPorNombre(String nombre) throws ArtistaNoEncontradoException {
        for (Artista artista : artistas.iterator()) {
            // Comprobar si el nombre completo o el primer nombre coincide
            if (artista.getNombre().equalsIgnoreCase(nombre) || artista.getNombre().split(" ")[0].equalsIgnoreCase(nombre)) {
                return artista;
            }
        }
        throw new ArtistaNoEncontradoException("Ningún artista coincide con el nombre especificado");
    }



    /**
     * Obtiene todas las canciones en el sistema.
     *
     * @return Una lista circular de todas las canciones en el sistema.
     */
    public ListaEnlazadaSimpleCircular<Cancion> obtenerCancionesGenerales() {
        ListaEnlazadaSimpleCircular<Cancion> cancionesGenerales = new ListaEnlazadaSimpleCircular<>();
        ListaEnlazadaSimple<Artista> artistas = getArtistas().iterator();

        for (Artista artista : artistas) {
            for (Cancion cancion : artista.getCanciones()) {
                cancionesGenerales.add(cancion);
            }
        }

        return cancionesGenerales;
    }

    /**
     * Carga artistas desde un archivo.
     *
     * @param ruta La ruta del archivo desde donde se cargarán los artistas.
     * @throws IOException Si ocurre un error al leer el archivo.
     * @throws ArtistasYaEnTiendaException Si al menos un artista ya está en la tienda.
     * @throws InterruptedException Si el hilo es interrumpido.
     */
    public void cargarArtistasDesdeArchivo(String ruta) throws IOException, ArtistasYaEnTiendaException, InterruptedException {
        ListaEnlazadaSimple<Artista> artistas = ArchivoUtil.cargarArtistasDesdeArchivo(ruta);
        BinaryTree<Artista> artistasExistentes = getArtistas();
        ListaEnlazadaSimple<Artista> artistasYaEnTienda = new ListaEnlazadaSimple<>();

        for (Artista artista : artistas) {
            if (!(artistasExistentes.find(artista) != null)) {
                artistasExistentes.insert(artista);
            } else {
                artistasYaEnTienda.add(artista);
            }
        }

        if (!artistasYaEnTienda.isEmpty()) {
            throw new ArtistasYaEnTiendaException("Al menos un artista ya está en la tienda");
        }
    }

    /**
     * Cuenta el número de canciones por género.
     *
     * @return Un mapa con el género como clave y el número de canciones como valor.
     */
    public Map<String, Integer> contarCancionesPorGenero() {
        Map<String, Integer> contadorGeneros = new HashMap<>();
        ListaEnlazadaSimple<Artista> artistas = getArtistas().iterator();

        for (Artista artista : artistas) {
            for (Cancion cancion : artista.getCanciones()) {
                String genero = cancion.getGenero().toString();
                contadorGeneros.put(genero, contadorGeneros.getOrDefault(genero, 0) + 1);
            }
        }

        return contadorGeneros;
    }

    /**
     * Obtiene el género con más canciones.
     *
     * @return El nombre del género con más canciones.
     */
    public String obtenerGeneroConMasCanciones() {
        HashMap<TipoGenero, Integer> contadorGeneros = new HashMap<>();
        ListaEnlazadaSimple<Artista> artistas = getArtistas().iterator();

        for (Artista artista : artistas) {
            for (Cancion cancion : artista.getCanciones()) {
                TipoGenero genero = cancion.getGenero();
                contadorGeneros.put(genero, contadorGeneros.getOrDefault(genero, 0) + 1);
            }
        }

        String generoConMasCanciones = null;
        int maxCanciones = 0;
        for (Map.Entry<TipoGenero, Integer> entry : contadorGeneros.entrySet()) {
            if (entry.getValue() > maxCanciones) {
                generoConMasCanciones = entry.getKey().toString();
                maxCanciones = entry.getValue();
            }
        }

        return generoConMasCanciones;
    }

    /**
     * Cuenta las vistas por artista simulando la obtención de vistas de YouTube.
     *
     * @return Un mapa con el nombre del artista como clave y el total de vistas como valor.
     * @throws IOException Si ocurre un error al leer los datos de YouTube.
     * @throws GeneralSecurityException Si ocurre un error de seguridad al acceder a los datos de YouTube.
     */
    public Map<String, Double> contarVistasPorArtista() throws IOException, GeneralSecurityException {
        Map<String, Double> vistasPorArtista = new HashMap<>();

        for (Artista artista : getArtistas().iterator()) {
            double totalVistas = 0.0;

            for (Cancion cancion : artista.getCanciones()) {
                long reproducciones = YouTubeHelper.obtenerVistasVideo(cancion.getUrlYoutube());
                totalVistas += reproducciones;
            }

            vistasPorArtista.put(artista.getNombre(), totalVistas);
        }

        return vistasPorArtista;
    }

    /**
     * Convierte un árbol binario a una lista.
     *
     * @param <T> El tipo de elementos en el árbol.
     * @param arbol El árbol a convertir.
     * @return Una lista con los elementos del árbol.
     */
    public <T extends Comparable<T>> List<T> convertirArbolALista(BinaryTree<T> arbol) {
        List<T> lista = new ArrayList<>();
        ListaEnlazadaSimple<T> iterador = arbol.iterator();

        for (T elemento : iterador) {
            lista.add(elemento);
        }

        return lista;
    }

    /**
     * Convierte una lista enlazada doble a un ArrayList.
     *
     * @param <T> El tipo de elementos en la lista.
     * @param listaEnlazadaDoble La lista enlazada doble a convertir.
     * @return Un ArrayList con los elementos de la lista enlazada doble.
     */
    public <T extends Comparable<T>> ArrayList<T> convertirAArrayList(ListaEnlazadaDoble<T> listaEnlazadaDoble) {
        ArrayList<T> arrayList = new ArrayList<>();
        for (T elemento : listaEnlazadaDoble) {
            arrayList.add(elemento);
        }
        return arrayList;
    }

    /**
     * Obtiene una lista de nacionalidades sin repetir.
     *
     * @return Una lista de nacionalidades sin duplicados.
     */
    public List<String> obtenerNacionalidadesSinRepetir() {
        List<String> nacionalidades = new ArrayList<>();
        Set<String> nacionalidadesSet = new HashSet<>();

        for (Artista artista : this.artistas.iterator()) {
            String nacionalidad = artista.getNacionalidad();
            nacionalidadesSet.add(nacionalidad);
        }

        nacionalidades.addAll(nacionalidadesSet);
        return nacionalidades;
    }

    /**
     * Obtiene una lista de años de lanzamiento sin repetir.
     *
     * @return Una lista de años de lanzamiento sin duplicados.
     */
    public List<String> obtenerAniosLanzamientoSinRepetir() {
        List<String> lanzamiento = new ArrayList<>();
        Set<String> lanzamientoSet = new HashSet<>();

        for (Artista artista : this.artistas.iterator()) {
            for (Cancion cancion : artista.getCanciones()) {
                String cancionLanzamiento = String.valueOf(cancion.getAnioLanzamiento());
                lanzamientoSet.add(cancionLanzamiento);
            }
        }
        lanzamientoSet.add("Todos");

        lanzamiento.addAll(lanzamientoSet);
        return lanzamiento;
    }

    /**
     * Obtiene una lista de tipos de género de canciones sin repetir.
     *
     * @return Una lista de tipos de género de canciones sin duplicados.
     */
    public List<String> obtenerTipoGeneroCancionesSinRepetir() {
        List<String> genero = new ArrayList<>();
        Set<String> generoSet = new HashSet<>();

        for (Artista artista : this.artistas.iterator()) {
            for (Cancion cancion : artista.getCanciones()) {
                String cancionGenero = cancion.obtenerGeneroComoString();
                generoSet.add(cancionGenero);
            }
        }
        generoSet.add("Todos");

        genero.addAll(generoSet);
        return genero;
    }

    /**
     * Obtiene una lista de duraciones de canciones sin repetir.
     *
     * @return Una lista de duraciones de canciones sin duplicados.
     */
    public List<String> obtenerDuracionCancionesSinRepetir() {
        List<String> duracion = new ArrayList<>();
        Set<String> duracionSet = new HashSet<>(); // Usamos un Set para evitar duplicados

        // Recorremos el árbol de artistas utilizando el iterador
        for (Artista artista : this.artistas.iterator()) {
            for (Cancion cancion : artista.getCanciones()) {
                String cancionDuracion = cancion.getDuracion();
                duracionSet.add(cancionDuracion);
            }
        }

        duracionSet.add("Todos");

        duracion.addAll(duracionSet);
        return duracion;
    }

    /**
     * Obtiene una lista de artistas que cumplen al menos uno de los filtros de nacionalidad o tipo de artista.
     *
     * @param minimoNacionalidad La nacionalidad mínima a filtrar.
     * @param minimoTipo El tipo de artista mínimo a filtrar.
     * @return Un árbol binario con los artistas que cumplen al menos uno de los criterios.
     */
    public BinaryTree<Artista> obtenerMinimoFiltroArtistas(String minimoNacionalidad, String minimoTipo) {
        BinaryTree<Artista> artistasFiltrados = new BinaryTree<>();

        // Iterar sobre el árbol de artistas
        for (Artista artista : this.artistas.iterator()) {
            // Verificar si el artista cumple con los criterios de filtro
            if (artista.getNacionalidad().equals(minimoNacionalidad) || artista.obtenerTipoArtistaString().equals(minimoTipo)) {
                artistasFiltrados.insert(artista); // Agregar el artista al nuevo árbol
            }
        }

        return artistasFiltrados;
    }

    /**
     * Obtiene una lista de artistas que cumplen ambos filtros de nacionalidad y tipo de artista.
     *
     * @param minimoNacionalidad La nacionalidad mínima a filtrar.
     * @param minimoTipo El tipo de artista mínimo a filtrar.
     * @return Un árbol binario con los artistas que cumplen ambos criterios.
     */
    public BinaryTree<Artista> obtenerMaximoFiltroArtistas(String minimoNacionalidad, String minimoTipo) {
        BinaryTree<Artista> artistasFiltrados = new BinaryTree<>();

        // Iterar sobre el árbol de artistas
        for (Artista artista : this.artistas.iterator()) {
            // Verificar si el artista cumple con los criterios de filtro
            if (artista.getNacionalidad().equals(minimoNacionalidad) && artista.obtenerTipoArtistaString().equals(minimoTipo)) {
                artistasFiltrados.insert(artista); // Agregar el artista al nuevo árbol
            }
        }

        return artistasFiltrados;
    }

    /**
     * Este metodo toma en parametro la lista favorita de un cliente, y verifica los filtros tambien especificados por parametro
     * Nota: no se uso directamente en esta parte la busqueda por hilos ya que la lista de canciones favoritas
     * de un cliente se obtiene directamente del cliente, si se trata de usar un albor binario se abarcaran
     * otras canciones NO favoritas del mismo artista
     * @param listaFavs
     * @param genero
     * @param anio
     * @param duracion
     * @return
     */
    public ListaEnlazadaSimpleCircular<Cancion> obtenerListaMinimoFiltroDeFavoritos(ListaEnlazadaSimpleCircular<Cancion> listaFavs, String genero, String anio, String duracion){
        ListaEnlazadaSimpleCircular<Cancion> nuevaLista = new ListaEnlazadaSimpleCircular<>();

        // Utilizar un iterador para recorrer la lista
        Node<Cancion> currentNode = listaFavs.getHeadNode();
        if (currentNode != null) {
            do {
                Cancion cancion = currentNode.getData();
                if (cumpleMinimoUnFiltro(cancion, genero, anio, duracion)) {
                    nuevaLista.add(cancion);
                }
                currentNode = currentNode.getNextNode();
            } while (currentNode != null && currentNode != listaFavs.getHeadNode());
        }

        return nuevaLista;
    }

    /**
     * Este metodo toma en parametro la lista favorita de un cliente, y verifica los filtros tambien especificados por parametro
     * Nota: no se uso directamente en esta parte la busqueda por hilos ya que la lista de canciones favoritas
     * de un cliente se obtiene directamente del cliente, si se trata de usar un albor binario se abarcaran
     * otras canciones NO favoritas del mismo artista
     * @param listaFavs
     * @param genero
     * @param anio
     * @param duracion
     * @return
     */
    public ListaEnlazadaSimpleCircular<Cancion> obtenerListaMaximoFiltroDeFavoritos(ListaEnlazadaSimpleCircular<Cancion> listaFavs, String genero, String anio, String duracion){
        ListaEnlazadaSimpleCircular<Cancion> nuevaLista = new ListaEnlazadaSimpleCircular<>();

        // Utilizar un iterador para recorrer la lista
        Node<Cancion> currentNode = listaFavs.getHeadNode();
        if (currentNode != null) {
            do {
                Cancion cancion = currentNode.getData();
                if (cumpleTodosLosFiltros(cancion, genero, anio, duracion)) {
                    nuevaLista.add(cancion);
                }
                currentNode = currentNode.getNextNode();
            } while (currentNode != null && currentNode != listaFavs.getHeadNode());
        }

        return nuevaLista;
    }
    /**
     * Verifica si una canción cumple con al menos uno de los filtros de género, año de lanzamiento o duración.
     *
     * @param cancion La canción a verificar.
     * @param genero El género a filtrar.
     * @param anioLanzamiento El año de lanzamiento a filtrar.
     * @param duracion La duración a filtrar.
     * @return true si la canción cumple con al menos uno de los filtros, false en caso contrario.
     */
    public static boolean cumpleMinimoUnFiltro(Cancion cancion, String genero, String anioLanzamiento, String duracion) {
        boolean cumpleGenero = genero.equals("Todos") || cancion.obtenerGeneroComoString().equals(genero);
        boolean cumpleAnioLanzamiento = anioLanzamiento.equals("Todos") || String.valueOf(cancion.getAnioLanzamiento()).equals(anioLanzamiento);
        boolean cumpleDuracion = duracion.equals("Todos") || cancion.getDuracion().equals(duracion);

        return cumpleGenero || cumpleAnioLanzamiento || cumpleDuracion;
    }

    /**
     * Verifica si una canción cumple con todos los filtros de género, año de lanzamiento y duración.
     *
     * @param cancion La canción a verificar.
     * @param genero El género a filtrar.
     * @param anioLanzamiento El año de lanzamiento a filtrar.
     * @param duracion La duración a filtrar.
     * @return true si la canción cumple con todos los filtros, false en caso contrario.
     */
    public static boolean cumpleTodosLosFiltros(Cancion cancion, String genero, String anioLanzamiento, String duracion) {
        boolean cumpleGenero = genero.equals("Todos") || cancion.obtenerGeneroComoString().equals(genero) || genero.equals("Vacio");
        boolean cumpleAnioLanzamiento = anioLanzamiento.equals("Todos") || String.valueOf(cancion.getAnioLanzamiento()).equals(anioLanzamiento) || anioLanzamiento.equals("Vacio");
        boolean cumpleDuracion = duracion.equals("Todos") || cancion.getDuracion().equals(duracion) || duracion.equals("Vacio");

        return cumpleGenero && cumpleAnioLanzamiento && cumpleDuracion;
    }

    /**
     * Obtiene un árbol binario que contiene un único artista buscado.
     *
     * @param artistaPorBuscar El artista a buscar.
     * @return Un árbol binario con el artista encontrado.
     */
    public BinaryTree<Artista> obtenerArbolPorArtista(Artista artistaPorBuscar) {
        BinaryTree<Artista> resultadoArbol = new BinaryTree<>();
        resultadoArbol.insert(artistaPorBuscar);
        return resultadoArbol;
    }

    /**
     * Obtiene una lista de nombres de países.
     *
     * @return Una lista de nombres de países.
     */
    public ArrayList<String> obtenerNombresPaises() {
        ArrayList<String> countries = new ArrayList<>();
        // Agregar nombres de países sin tildes
        countries.add("Estados Unidos");
        countries.add("Reino Unido");
        countries.add("Canada");
        countries.add("Corea del Sur");
        countries.add("Inglaterra");
        countries.add("Mexico");
        countries.add("Australia");
        countries.add("Brasil");
        countries.add("Argentina");
        countries.add("China");
        countries.add("Japon");
        countries.add("India");
        countries.add("Rusia");
        countries.add("Alemania");
        countries.add("Francia");
        countries.add("Italia");
        countries.add("España");
        countries.add("Portugal");
        countries.add("Suecia");
        countries.add("Noruega");
        countries.add("Dinamarca");
        countries.add("Finlandia");
        countries.add("Holanda");
        countries.add("Belgica");
        countries.add("Suiza");
        countries.add("Austria");
        countries.add("Grecia");
        countries.add("Irlanda");
        countries.add("Turquia");
        countries.add("Vietnam");
        countries.add("Tailandia");
        countries.add("Indonesia");
        countries.add("Malasia");
        countries.add("Filipinas");
        return countries;
    }

    /**
     * Elimina una canción de la lista de canciones de un artista y de las listas de canciones favoritas de los usuarios.
     *
     * @param cancionElegida La canción a eliminar.
     */
    public void eliminarCancion(Cancion cancionElegida) {
        ListaEnlazadaSimple<Artista> artistas = getArtistas().iterator();

        for (Artista artista : artistas) {
            if (artista.getCanciones().contains(cancionElegida)) {
                artista.getCanciones().removeData(cancionElegida);
                eliminarCancionUsuario(cancionElegida);
                return;
            }
        }
    }

    /**
     * Elimina una canción de las listas de canciones favoritas de los usuarios.
     *
     * @param cancionElegida La canción a eliminar.
     */
    public void eliminarCancionUsuario(Cancion cancionElegida) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getPersona() instanceof Cliente) {
                Cliente cliente = (Cliente) usuario.getPersona();
                if (cliente.getCancionesFavoritas().contains(cancionElegida)) {
                    cliente.eliminarCancionFavorita(cancionElegida);
                }
            }
        }
    }


    /**
     * Elimina un artista del árbol de artistas y también elimina las canciones del
     * artista elegido de las listas de canciones favoritas de los usuarios.
     *
     * @param artistaElegido El artista que se desea eliminar.
     * @throws EmptyNodeException Si el árbol de artistas está vacío.
     */
    public void eliminarArtistaArbol(Artista artistaElegido) throws EmptyNodeException {
        //buscar entre las canciones favs de clientes y eliminar las canciones del artista
        //elegido
        eliminarCancionDeArtistaEnUsuario(artistaElegido);
        artistas.delete(artistaElegido);
    }

    /**
     * Elimina una canción de las listas de canciones favoritas de los usuarios.
     *
     * @param cancionElegida La canción a eliminar.
     */
    public void eliminarCancionDeArtistaEnUsuario(Artista artista) {
        for (Usuario usuario : usuarios.values()) {
            if (usuario.getPersona() instanceof Cliente) {
                Cliente cliente = (Cliente) usuario.getPersona();
                List<Cancion> cancionesAEliminar = new ArrayList<>();

                // Recopilar canciones a eliminar
                ListaEnlazadaDoble<Cancion> canciones = artista.getCanciones();
                for (Cancion cancion : canciones) {
                    if (artista.getCanciones().contains(cancion)) {
                        cancionesAEliminar.add(cancion);
                    }
                }

                // Eliminar canciones recopiladas
                for (Cancion cancion : cancionesAEliminar) {
                    cliente.eliminarCancionFavorita(cancion);
                }
            }
        }
    }

    /**
     * Ordena una lista de canciones por fecha de lanzamiento, de más reciente a más antigua.
     *
     * @param listaCanciones La lista enlazada circular de canciones que se desea ordenar.
     * @return Una nueva lista enlazada circular con las canciones ordenadas por fecha de lanzamiento.
     */
    public ListaEnlazadaSimpleCircular<Cancion> ordenarCancionesPorFechaMasReciente(ListaEnlazadaSimpleCircular<Cancion> listaCanciones) {
        // Paso 1: Convertir la lista circular a una lista normal
        List<Cancion> listaNormal = new ArrayList<>();
        if (listaCanciones.getHeadNode() != null) {
            Node<Cancion> currentNode = listaCanciones.getHeadNode();
            do {
                listaNormal.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            } while (currentNode != listaCanciones.getHeadNode());
        }

        // Paso 2: Ordenar la lista normal con Collections.sort() y un comparador
        Collections.sort(listaNormal, (c1, c2) -> Integer.compare(c2.getAnioLanzamiento(), c1.getAnioLanzamiento()));

        // Paso 3: Reconstruir la lista circular con el ordenado
        ListaEnlazadaSimpleCircular<Cancion> listaOrdenada = new ListaEnlazadaSimpleCircular<>();
        for (Cancion cancion : listaNormal) {
            listaOrdenada.add(cancion);
        }

        return listaOrdenada;
    }


    /**
     * Ordena una lista de canciones por fecha de lanzamiento, de más antigua a más reciente.
     *
     * @param listaCanciones La lista enlazada circular de canciones que se desea ordenar.
     * @return Una nueva lista enlazada circular con las canciones ordenadas por fecha de lanzamiento.
     */
    public ListaEnlazadaSimpleCircular<Cancion> ordenarCancionesPorFechaMasAntigua(ListaEnlazadaSimpleCircular<Cancion> listaCanciones) {
        // Paso 1: Convertir la lista circular a una lista normal
        List<Cancion> listaNormal = new ArrayList<>();
        if (listaCanciones.getHeadNode() != null) {
            Node<Cancion> currentNode = listaCanciones.getHeadNode();
            do {
                listaNormal.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            } while (currentNode != listaCanciones.getHeadNode());
        }

        // Paso 2: Ordenar la lista normal con Collections.sort() y un comparador
        Collections.sort(listaNormal, (c1, c2) -> Integer.compare(c1.getAnioLanzamiento(), c2.getAnioLanzamiento()));

        // Paso 3: Reconstruir la lista circular con el ordenado
        ListaEnlazadaSimpleCircular<Cancion> listaOrdenada = new ListaEnlazadaSimpleCircular<>();
        for (Cancion cancion : listaNormal) {
            listaOrdenada.add(cancion);
        }

        return listaOrdenada;
    }


    /**
     * Convierte una duración de formato "mm:ss" a segundos.
     *
     * @param duracion La duración en formato "mm:ss".
     * @return La duración en segundos.
     * @throws NumberFormatException Si la cadena de duración no está en el formato esperado.
     */
    private int convertirDuracionASegundos(String duracion) {
        String[] partes = duracion.split(":");
        int minutos = Integer.parseInt(partes[0]);
        int segundos = Integer.parseInt(partes[1]);
        return minutos * 60 + segundos;
    }

    /**
     * Ordena una lista de canciones por duración, de más larga a más corta.
     *
     * @param listaCanciones La lista enlazada circular de canciones que se desea ordenar.
     * @return Una nueva lista enlazada circular con las canciones ordenadas por duración de más larga a más corta.
     * @throws NumberFormatException Si la duración de alguna canción no está en el formato esperado "mm:ss".
     */
    public ListaEnlazadaSimpleCircular<Cancion> ordenarCancionesPorDuracionMasLarga(ListaEnlazadaSimpleCircular<Cancion> listaCanciones) {
        // Paso 1: Convertir la lista circular a una lista normal
        List<Cancion> listaNormal = new ArrayList<>();
        if (listaCanciones.getHeadNode() != null) {
            Node<Cancion> currentNode = listaCanciones.getHeadNode();
            do {
                listaNormal.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            } while (currentNode != listaCanciones.getHeadNode());
        }

        // Paso 2: Ordenar la lista normal con Collections.sort() y un comparador
        Collections.sort(listaNormal, (c1, c2) -> {
            int duracion1 = convertirDuracionASegundos(c1.getDuracion());
            int duracion2 = convertirDuracionASegundos(c2.getDuracion());
            return Integer.compare(duracion2, duracion1); // De la más larga a la más corta
        });

        // Paso 3: Reconstruir la lista circular con el ordenado
        ListaEnlazadaSimpleCircular<Cancion> listaOrdenada = new ListaEnlazadaSimpleCircular<>();
        for (Cancion cancion : listaNormal) {
            listaOrdenada.add(cancion);
        }

        return listaOrdenada;
    }





    /**
     * Ordena una lista de canciones por duración, de más corta a más larga.
     *
     * @param listaCanciones La lista enlazada circular de canciones que se desea ordenar.
     * @return Una nueva lista enlazada circular con las canciones ordenadas por duración de más corta a más larga.
     * @throws NumberFormatException Si la duración de alguna canción no está en el formato esperado "mm:ss".
     */
    public ListaEnlazadaSimpleCircular<Cancion> ordenarCancionesPorDuracionMasCorta(ListaEnlazadaSimpleCircular<Cancion> listaCanciones) {
        // Paso 1: Convertir la lista circular a una lista normal
        List<Cancion> listaNormal = new ArrayList<>();
        if (listaCanciones.getHeadNode() != null) {
            Node<Cancion> currentNode = listaCanciones.getHeadNode();
            do {
                listaNormal.add(currentNode.getData());
                currentNode = currentNode.getNextNode();
            } while (currentNode != listaCanciones.getHeadNode());
        }

        // Paso 2: Ordenar la lista normal con Collections.sort() y un comparador
        Collections.sort(listaNormal, (c1, c2) -> {
            int duracion1 = convertirDuracionASegundos(c1.getDuracion());
            int duracion2 = convertirDuracionASegundos(c2.getDuracion());
            return Integer.compare(duracion1, duracion2); // De la más corta a la más larga
        });

        // Paso 3: Reconstruir la lista circular con el ordenado
        ListaEnlazadaSimpleCircular<Cancion> listaOrdenada = new ListaEnlazadaSimpleCircular<>();
        for (Cancion cancion : listaNormal) {
            listaOrdenada.add(cancion);
        }

        return listaOrdenada;
    }




    /**
     * Obtiene una lista de los tipos de ordenamientos disponibles.
     *
     * @return Una lista de cadenas que representan los tipos de ordenamientos disponibles.
     */
    public List<String>obtenerOrdenamientos(){
        List<String> orden = new ArrayList<>();
        orden.add("Mas Recientes");
        orden.add("Mas antiguas");
        orden.add("Mayor duracion");
        orden.add("Menor duracion");
        return orden;
    }

}