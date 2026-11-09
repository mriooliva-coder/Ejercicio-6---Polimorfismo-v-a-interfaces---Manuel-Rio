// Pasarela de billeteras digitales (PayPal, Tigo Money).
package modelo;

public class BilleteraDigital extends PasarelaPago {

    public BilleteraDigital(String id, String nombre, String endpoint, int latenciaMs, boolean conectada, double comision) {
        super(id, nombre, endpoint, latenciaMs, conectada, comision);
    }

    @Override
    public String getTipo() { return "Billetera digital"; }

    @Override
    public String reintegrar(String idTransaccion, double monto) {
        return "Saldo de " + idTransaccion + " devuelto a la billetera al instante.";
    }
}
