// Certificador de facturas electronicas FEL.
package modelo;

public class CertificadorFEL extends IntegracionAPI implements Documentable, Auditable {

    private String nitEmisor;
    private String bitacora = "";

    public CertificadorFEL(String id, String nombre, String endpoint, int latenciaMs, boolean conectada, String nitEmisor) {
        super(id, nombre, endpoint, latenciaMs, conectada);
        this.nitEmisor = nitEmisor;
    }

    @Override
    public String getTipo() { return "Certificador FEL"; }

    @Override
    public String generarDocumento(String referencia) {
        return generarDocumento(referencia, "XML");
    }

    @Override
    public String generarDocumento(String referencia, String formato) {
        registrarEvento("Factura certificada para " + referencia);
        return "Factura FEL de " + referencia + " (" + formato + ") NIT " + nitEmisor + " por " + getNombre();
    }

    @Override
    public void registrarEvento(String evento) { bitacora += "[" + getId() + "] " + evento + "\n"; }

    @Override
    public String getBitacora() { return bitacora; }

    @Override
    public String getConfiguracion() {
        return super.getConfiguracion() + "\nNIT emisor: " + nitEmisor;
    }

    public String getNitEmisor() { return nitEmisor; }
}
