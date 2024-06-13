package co.edu.uniquindio.storify.model;

import co.edu.uniquindio.storify.estructurasDeDatos.listas.ListaEnlazadaSimpleCircular;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa un usuario en el sistema.
 * Implementa Serializable para permitir la serialización de objetos de esta clase.
 * Implementa Comparable para permitir la comparación entre usuarios basada en el nombre de usuario.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@SuppressWarnings("All")
public class Usuario implements Serializable, Comparable<Usuario> {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private String email;
    private Persona persona;

    /**
     * Compara este usuario con otro usuario basado en el nombre de usuario.
     *
     * @param o El usuario con el que se va a comparar.
     * @return Un valor negativo, cero o positivo si este usuario es menor, igual o mayor que el usuario especificado.
     */
    @Override
    public int compareTo(Usuario o) {
        return this.username.compareTo(o.getUsername());
    }

    /**
     * Verifica si este usuario es igual a otro objeto.
     * Dos usuarios son considerados iguales si tienen el mismo nombre de usuario.
     *
     * @param o El objeto con el que se va a comparar.
     * @return true si los objetos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    /**
     * Calcula el código hash para este usuario basado en el nombre de usuario.
     *
     * @return El código hash de este usuario.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }
}
