// Interfaz para APIs que cobran y reintegran dinero.
package modelo;

public interface Transaccional {
    String cobrar(double monto);                  // en quetzales
    String cobrar(double monto, String moneda);
    String reintegrar(String idTransaccion, double monto);
}
