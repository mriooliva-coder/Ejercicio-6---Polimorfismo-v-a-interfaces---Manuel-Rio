// Proveedor de envios: cotiza y genera guias. No maneja dinero.
package modelo;

public class ProveedorLogistica extends IntegracionAPI implements Cotizable, Documentable {

    private double tarifaBase;
    private double tarifaPorKg;

    public ProveedorLogistica(String id, String nombre, String endpoint, int latenciaMs, boolean conectada,
                              double tarifaBase, double tarifaPorKg) {
        super(id, nombre, endpoint, latenciaMs, conectada);
        this.tarifaBase = tarifaBase;
        this.tarifaPorKg = tarifaPorKg;
    }

    @Override
    public String getTipo() { return "Logistica"; }

    @Override
    public double cotizar(double pesoKg) {
        return cotizar(pesoKg, false);
    }

    @Override
    public double cotizar(double pesoKg, boolean express) {
        double costo = tarifaBase + pesoKg * tarifaPorKg;
        return express ? costo * 1.5 : costo;
    }

    @Override
    public String generarDocumento(String referencia) {
        return generarDocumento(referencia, "PDF");
    }

    @Override
    public String generarDocumento(String referencia, String formato) {
        return "Guia GUIA-" + referencia + " (" + formato + ") generada por " + getNombre();
    }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nTarifa base: GTQ " + tarifaBase + "\nPor kg: GTQ " + tarifaPorKg;
    }

    public double getTarifaBase() { return tarifaBase; }
    public double getTarifaPorKg() { return tarifaPorKg; }
}
