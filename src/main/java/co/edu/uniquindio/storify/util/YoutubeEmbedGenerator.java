package co.edu.uniquindio.storify.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeEmbedGenerator {

    /**
     * Obtiene el código de inserción de un video de YouTube a partir de su URL.
     * @param urlYoutube La URL del video de YouTube.
     * @return El código de inserción del video si la URL es válida, de lo contrario retorna null.
     */
    public static String obtenerEmbedCode(String urlYoutube) {

        // Expresión regular para reconocer URLs de YouTube
        Pattern youtubePattern = Pattern.compile("(?:(?:https?:)?\\/\\/)?(?:www\\.)?(?:youtube\\.com\\/watch\\?v=|youtu\\.be\\/)([\\w\\-]+)(?:[?&].*)?$");

        // Matcher para hacer coincidir la URL con la expresión regular
        Matcher youtubeMatcher = youtubePattern.matcher(urlYoutube);

        // Si se encuentra una coincidencia, obtener el ID del video y construir el código de inserción
        if (youtubeMatcher.find()) {
            String videoId = youtubeMatcher.group(1);
            return String.format("https://www.youtube.com/embed/%s", videoId);
        }

        // Si no se encuentra ninguna coincidencia, retornar null
        return null;
    }
}
