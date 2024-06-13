package co.edu.uniquindio.storify.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * La clase Administrador representa un administrador en la plataforma Storify.
 * Extiende la clase Persona e implementa Serializable para permitir la serialización de sus instancias.
 * Utiliza Lombok para generar métodos comunes como getters, setters y equals/hashCode.
 */
@SuppressWarnings("all")
@Data
@EqualsAndHashCode(callSuper = true)
public class Administrador extends Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    private static Administrador administrador;

    /**
     * Constructor de la clase Administrador.
     * Utiliza el patrón Builder de Lombok y llama al constructor de la clase base Persona con valores predeterminados.
     */
    @Builder
    public Administrador() {
        super("Jose Juan", "Mesa");
    }

    /**
     * Obtiene la instancia única de Administrador.
     * Si no existe una instancia, la crea.
     * @return la instancia única de Administrador.
     */
    public static Administrador getAdministrador() {
        if (administrador == null) {
            administrador = new Administrador();
        }
        return administrador;
    }
}
