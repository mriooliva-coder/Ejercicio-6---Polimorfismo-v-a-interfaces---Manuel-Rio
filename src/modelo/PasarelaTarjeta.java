// Pasarela de pagos con tarjeta.
package modelo;

public class PasarelaTarjeta extends PasarelaPago implements Auditable {

    private String bitacora = "";

    public PasarelaTarjeta(String id, String nombre, String endpoint, int latenciaMs, boolean conectada, double comision) {
        super(id, nombre, endpoint, latenciaMs, conectada, comision);
    }

    @Override
    public String getTipo() { return "Pago con tarjeta"; }

    @Override
    public String reintegrar(String idTransaccion, double monto) {
        registrarEvento("Reintegro de " + idTransaccion + " por GTQ " + monto);
        return "Reverso a la tarjeta de " + idTransaccion + " (5 a 10 dias habiles).";
    }

    @Override
    public void registrarEvento(String evento) { bitacora += "[" + getId() + "] " + evento + "\n"; }

    @Override
    public String getBitacora() { return bitacora; }
}
