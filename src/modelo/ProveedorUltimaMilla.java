// Nueva startup de ultima milla: hereda de ProveedorLogistica, solo se instancia en la carga inicial.
package modelo;

public class ProveedorUltimaMilla extends ProveedorLogistica {

    public ProveedorUltimaMilla(String id, String nombre, String endpoint, int latenciaMs, boolean conectada,
                                double tarifaBase, double tarifaPorKg) {
        super(id, nombre, endpoint, latenciaMs, conectada, tarifaBase, tarifaPorKg);
    }

    @Override
    public String getTipo() { return "Ultima milla"; }

    // Para paquetes pesados (mas de 5 kg) cobra el doble
    @Override
    public double cotizar(double pesoKg, boolean express) {
        double costo = super.cotizar(pesoKg, express);
        return pesoKg > 5 ? costo * 2 : costo;
    }
}
