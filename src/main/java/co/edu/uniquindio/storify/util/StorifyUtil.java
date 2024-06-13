package co.edu.uniquindio.storify.util;

import co.edu.uniquindio.storify.exceptions.UsuarioYaRegistradoException;
import co.edu.uniquindio.storify.model.*;

/**
 * Clase de utilidad para inicializar datos de prueba en la tienda de música.
 */
@SuppressWarnings("all")
public class StorifyUtil {

    /**
     * Inicializa datos de prueba en la tienda de música.
     *
     * @return La tienda de música con los datos de prueba inicializados.
     */
    public static TiendaMusica inicializarDatosPrueba() {
        TiendaMusica tiendaMusica = new TiendaMusica();

        // Datos de administrador
        Administrador administrador = new Administrador();
        Cliente persona1 = new Cliente("Mary", "Saire");

        Usuario usuario = new Usuario("admin", "$aDmiN", "admin@gmail", administrador);
        Usuario usuario1 = new Usuario("Mary", "1234", "mary@gmail.com", persona1);

        try {
            tiendaMusica.agregarUsuario(usuario);
            tiendaMusica.agregarUsuario(usuario1);
        } catch (UsuarioYaRegistradoException e) {
            throw new RuntimeException(e);
        }

        // Datos de artistas
        Artista artista1 = new Artista("ART001", "Michael Jackson", "Estados Unidos", TipoArtista.SOLISTA);
        Artista artista2 = new Artista("ART002", "The Beatles", "Reino Unido", TipoArtista.BANDA);
        Artista artista3 = new Artista("ART003", "Beyoncé", "Estados Unidos", TipoArtista.SOLISTA);
        Artista artista4 = new Artista("ART004", "Queen", "Reino Unido", TipoArtista.BANDA);
        Artista artista5 = new Artista("ART005", "The Weeknd", "Canada", TipoArtista.SOLISTA);
        Artista artista6 = new Artista("ART006", "Park Jae-sang (PSY)", "Surcorea", TipoArtista.SOLISTA);
        Artista artista7 = new Artista("ART007", "Coldplay", "Inglaterra", TipoArtista.BANDA);
        Artista artista8 = new Artista("ART008", "Mon Laferte", "Mexico", TipoArtista.SOLISTA);

        tiendaMusica.getArtistas().insert(artista1);
        tiendaMusica.getArtistas().insert(artista2);
        tiendaMusica.getArtistas().insert(artista3);
        tiendaMusica.getArtistas().insert(artista4);
        tiendaMusica.getArtistas().insert(artista5);
        tiendaMusica.getArtistas().insert(artista6);
        tiendaMusica.getArtistas().insert(artista7);
        tiendaMusica.getArtistas().insert(artista8);

        // Datos adicionales de artistas y canciones
        Cancion cancion1 = new Cancion("SONG001", "Die For You", "Starboy", "/imagenes/starboy.jpeg", 2016, "4:20", TipoGenero.OTRO, "https://www.youtube.com/watch?v=QLCpqdqeoII");
        Cancion cancion2 = new Cancion("SONG002", "I Feel It Coming", "Starboy", "/imagenes/starboy.jpeg", 2016, "4:29", TipoGenero.OTRO, "https://www.youtube.com/watch?v=iIWoYaJRryw");
        Cancion cancion3 = new Cancion("SONG003", "Gangnam Style", "Ssai Yukgap, Part 1", "/imagenes/gangnam.jpg", 2012, "4:13", TipoGenero.KPOP, "https://www.youtube.com/watch?v=9bZkp7q19f0");
        Cancion cancion4 = new Cancion("SONG004", "Paradise", "Mylo Xyloto", "/imagenes/paradise.jpeg", 2011, "4:20", TipoGenero.RB, "https://www.youtube.com/watch?v=J6ZWlDks0nQ");
        Cancion cancion5 = new Cancion("SONG005", "Tu Falta De Querer", "Mon Laferte Vol. 1", "/imagenes/MonLaferte.jpeg", 2015, "4:38", TipoGenero.POP, "https://www.youtube.com/watch?v=EbO74Yb0un4");
        Cancion cancion6 = new Cancion("SONG006", "Gentleman", "The Singles", "/imagenes/theSingles.jpg", 2013, "3:53", TipoGenero.KPOP, "https://www.youtube.com/watch?v=ASO_zypdnsQ");
        Cancion cancion7 = new Cancion("SONG007", "Blue Frog", "Ssai Yukgap, Part 1", "/imagenes/gangnam.jpg", 2012, "3:27", TipoGenero.KPOP, "https://www.youtube.com/watch?v=GjIrL7ivEyk");
        Cancion cancion8 = new Cancion("SONG008", "Amor Completo", "Mon Laferte Vol. 1", "/imagenes/MonLaferte.jpeg", 2015, "4:01", TipoGenero.POP, "https://www.youtube.com/watch?v=QaFtF7XLEBI");
        Cancion cancion9 = new Cancion("SONG009", "Mi Buen Amor", "La Trenza", "/imagenes/laTrenza.jpg", 2017, "3:49", TipoGenero.POP, "https://www.youtube.com/watch?v=bId_8k1gfjA");
        Cancion cancion10 = new Cancion("SONG010", "Si Tú Me Quisieras", "Mon Laferte Vol. 1", "/imagenes/MonLaferte.jpeg", 2015, "3:22", TipoGenero.POP, "https://www.youtube.com/watch?v=QBew-Lg1jvY");
        Cancion cancion11 = new Cancion("SONG011", "Billie Jean", "Thriller", "/imagenes/thriller.jpeg", 1983, "4:54", TipoGenero.RB, "https://www.youtube.com/watch?v=tfIuEwzoDME");
        Cancion cancion12 = new Cancion("SONG012", "Beat It", "Thriller", "/imagenes/thriller.jpeg", 1983, "4:58", TipoGenero.ROCK, "https://www.youtube.com/watch?v=WlTlUseVt7E");
        Cancion cancion13 = new Cancion("SONG013", "Thriller", "Thriller", "/imagenes/thriller.jpeg", 1983, "5:45", TipoGenero.ROCK, "https://www.youtube.com/watch?v=in5IYOatFb8");

        artista5.agregarCancion(cancion1);
        artista1.agregarCancion(cancion11);
        artista5.agregarCancion(cancion2);
        artista6.agregarCancion(cancion3);
        artista1.agregarCancion(cancion12);
        artista6.agregarCancion(cancion6);
        artista6.agregarCancion(cancion7);
        artista7.agregarCancion(cancion4);
        artista8.agregarCancion(cancion5);
        artista1.agregarCancion(cancion13);
        artista8.agregarCancion(cancion8);
        artista8.agregarCancion(cancion9);
        artista8.agregarCancion(cancion10);

        persona1.agregarCancionFavorita(cancion1);
        persona1.agregarCancionFavorita(cancion3);
        persona1.agregarCancionFavorita(cancion2);
        persona1.agregarCancionFavorita(cancion4);

        return tiendaMusica;
    }
}


