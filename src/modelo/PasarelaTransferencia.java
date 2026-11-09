// Pasarela de pagos por transferencia bancaria.
package modelo;

public class PasarelaTransferencia extends PasarelaPago implements Auditable {

    private String bitacora = "";

    public PasarelaTransferencia(String id, String nombre, String endpoint, int latenciaMs, boolean conectada, double comision) {
        super(id, nombre, endpoint, latenciaMs, conectada, comision);
    }

    @Override
    public String getTipo() { return "Transferencia bancaria"; }

    @Override
    public String reintegrar(String idTransaccion, double monto) {
        registrarEvento("Devolucion de " + idTransaccion + " por GTQ " + monto);
        return "Devolucion por transferencia de " + idTransaccion + " (24 a 48 horas).";
    }

    @Override
    public void registrarEvento(String evento) { bitacora += "[" + getId() + "] " + evento + "\n"; }

    @Override
    public String getBitacora() { return bitacora; }
}
