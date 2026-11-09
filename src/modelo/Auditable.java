// Interfaz para APIs que llevan bitacora de eventos.
package modelo;

public interface Auditable {
    void registrarEvento(String evento);
    String getBitacora();
}
