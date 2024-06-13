package co.edu.uniquindio.storify.util;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Utilidad para interactuar con la API de YouTube.
 * Esta clase proporciona métodos para obtener estadísticas de videos de YouTube.
 */
public class YouTubeHelper {

     private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
     private static final String API_KEY = "AIzaSyAJsq8canZkjSv_6SvKVSbHcXhoVk5PJUM"; // Reemplaza con tu clave de API

     /**
      * Obtiene el número de vistas de un video de YouTube dado su URL.
      *
      * @param url La URL del video de YouTube.
      * @return El número de vistas del video.
      * @throws GeneralSecurityException Si ocurre un problema de seguridad.
      * @throws IOException Si ocurre un problema de I/O.
      */
     public static long obtenerVistasVideo(String url) throws GeneralSecurityException, IOException {
          String videoId = obtenerVideoId(url); // El ID del video que deseas analizar
          return getYouTubeVideoViews(videoId);
     }

     /**
      * Extrae el ID del video de YouTube a partir de su URL.
      *
      * @param url La URL del video de YouTube.
      * @return El ID del video de YouTube.
      */
     private static String obtenerVideoId(String url) {
          return url.substring(url.lastIndexOf("=") + 1);
     }

     /**
      * Obtiene el número de vistas de un video de YouTube dado su ID.
      *
      * @param videoId El ID del video de YouTube.
      * @return El número de vistas del video.
      * @throws IOException Si ocurre un problema de I/O.
      * @throws GeneralSecurityException Si ocurre un problema de seguridad.
      */
     private static long getYouTubeVideoViews(String videoId) throws IOException, GeneralSecurityException {
          final HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
          YouTube youtubeService = new YouTube.Builder(httpTransport, JSON_FACTORY, getRequestInitializer())
                  .setApplicationName("YouTube-Video-Stats")
                  .build();

          YouTube.Videos.List request = youtubeService.videos().list(Collections.singletonList("statistics"));
          VideoListResponse response = request.setKey(API_KEY).setId(Collections.singletonList(videoId)).execute();
          List<Video> videos = response.getItems();

          if (videos != null && !videos.isEmpty()) {
               Video video = videos.get(0);
               return Long.parseLong(String.valueOf(video.getStatistics().getViewCount()));
          } else {
               System.out.println("No se encontró información para el video con ID: " + videoId);
               return -1;
          }
     }

     /**
      * Inicializador de solicitud HTTP.
      *
      * @return Un HttpRequestInitializer.
      */
     private static HttpRequestInitializer getRequestInitializer() {
          return httpRequest -> {
               // No hay inicializaciones especiales necesarias
          };
     }
}
