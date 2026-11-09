// Interfaz para APIs que generan documentos (facturas, guias).
package modelo;

public interface Documentable {
    String generarDocumento(String referencia);
    String generarDocumento(String referencia, String formato);
}
