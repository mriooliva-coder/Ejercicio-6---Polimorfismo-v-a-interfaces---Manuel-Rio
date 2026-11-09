// Clase base de las pasarelas de pago.
package modelo;

public abstract class PasarelaPago extends IntegracionAPI implements Transaccional {

    private double comision;
    private static int contador = 1000;

    public PasarelaPago(String id, String nombre, String endpoint, int latenciaMs, boolean conectada, double comision) {
        super(id, nombre, endpoint, latenciaMs, conectada);
        this.comision = comision;
    }

    @Override
    public String cobrar(double monto) {
        return cobrar(monto, "GTQ");
    }

    @Override
    public String cobrar(double monto, String moneda) {
        if (!isConectada()) return "ERROR: " + getNombre() + " esta desconectada.";
        String idTx = "TX-" + (++contador);
        if (this instanceof Auditable) {
            ((Auditable) this).registrarEvento("Cobro " + idTx + " por " + moneda + " " + monto);
        }
        return "Cobro aprobado " + idTx + " por " + moneda + " " + String.format("%.2f", monto);
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nComision: " + comision + "%";
    }

    public double getComision() { return comision; }
}
