/**
 * La clase AdministradorComandos gestiona el historial de comandos ejecutados en una aplicación,
 * permitiendo deshacer y rehacer acciones.
 */
package co.edu.uniquindio.storify.controllers.controladorFlujo;

import co.edu.uniquindio.storify.estructurasDeDatos.pila.Pila;
import lombok.Data;

@Data
public class AdministradorComandos {
    // Pila que almacena los comandos deshechos
    private Pila<Comando> pilaDeshacer;
    // Pila que almacena los comandos rehacer
    private Pila<Comando> pilaRehacer;

    /**
     * Constructor de la clase. Inicializa las pilas de deshacer y rehacer.
     */
    public AdministradorComandos() {
        this.pilaDeshacer = new Pila<>();
        this.pilaRehacer = new Pila<>();
    }

    /**
     * Ejecuta un comando y lo agrega a la pila de deshacer.
     *
     * @param comando El comando a ejecutar.
     */
    public void ejecutarComando(Comando comando) {
        comando.ejecutar();
        pilaDeshacer.add(comando);
        pilaRehacer.clear(); // Limpia la pila de rehacer cuando se agrega un nuevo comando.
    }

    /**
     * Deshace la última acción ejecutada, si existe.
     */
    public void deshacer() {
        if (!pilaDeshacer.isEmpty()) {
            Comando comando = pilaDeshacer.desapilar();
            comando.deshacer();
            pilaRehacer.add(comando);
        }
    }

    /**
     * Rehace la última acción deshecha, si existe.
     */
    public void rehacer() {
        if (!pilaRehacer.isEmpty()) {
            Comando comando = pilaRehacer.desapilar();
            comando.rehacer();
            pilaDeshacer.add(comando);
        }
    }
}
