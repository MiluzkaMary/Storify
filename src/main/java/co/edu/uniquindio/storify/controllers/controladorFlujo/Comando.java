package co.edu.uniquindio.storify.controllers.controladorFlujo;

public interface Comando {
    void ejecutar();
    void deshacer();
    void rehacer();
}