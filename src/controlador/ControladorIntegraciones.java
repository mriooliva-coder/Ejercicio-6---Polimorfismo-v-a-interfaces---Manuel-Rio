// Controlador: conecta las vistas con el modelo.
package controlador;

import java.util.ArrayList;
import java.util.List;

import modelo.*;

public class ControladorIntegraciones {

    private RegistroIntegraciones registro = new RegistroIntegraciones();
    private int contadorPedidos = 1;

    public ControladorIntegraciones() {
        registro.agregar(new PasarelaTarjeta("TC-01", "VisaNet Guatemala", "api.visanet.com.gt", 185, true, 3.5));
        registro.agregar(new PasarelaTarjeta("TC-02", "Stripe", "api.stripe.com", 240, true, 2.9));
        registro.agregar(new PasarelaTransferencia("TB-01", "BI Transferencias", "api.bi.com.gt", 420, true, 0.5));
        registro.agregar(new PasarelaTransferencia("TB-02", "BAM Pagos", "api.bam.com.gt", 395, false, 0.8));
        registro.agregar(new BilleteraDigital("BD-01", "PayPal", "api.paypal.com", 210, true, 4.4));
        registro.agregar(new BilleteraDigital("BD-02", "Tigo Money", "api.tigo.com.gt", 330, true, 1.5));
        registro.agregar(new ProveedorLogistica("LOG-01", "Cargo Expreso", "api.cargoexpreso.com", 510, true, 35, 8));
        registro.agregar(new ProveedorLogistica("LOG-02", "Forza Delivery", "api.forzadelivery.com", 465, false, 30, 7));
        registro.agregar(new ProveedorLogistica("LOG-03", "DHL Express", "api.dhl.com", 300, true, 120, 25));
        registro.agregar(new CertificadorFEL("FEL-01", "INFILE", "certificador.infile.com.gt", 150, true, "1234567-8"));
        registro.agregar(new CertificadorFEL("FEL-02", "Digifact", "felgtaws.digifact.com.gt", 275, true, "1234567-8"));
        // Nueva startup: solo se agrego esta linea, el checkout no se toco
        registro.agregar(new ProveedorUltimaMilla("UM-01", "RapiEntrega", "api.rapientrega.gt", 120, true, 20, 5));
    }

    public String listar() {
        String texto = String.format("%-7s %-20s %-22s %7s  %s%n", "ID", "NOMBRE", "TIPO", "LATENCIA", "ESTADO");
        for (IntegracionAPI api : registro.getIntegraciones()) {
            texto += api + "\n";
        }
        return texto;
    }

    public List<IntegracionAPI> getIntegraciones() {
        return registro.getIntegraciones();
    }

    public String buscar(String id) {
        IntegracionAPI api = registro.buscarPorId(id);
        if (api == null) return "No existe la integracion " + id;
        return api.getConfiguracion() + "\nContratos: " + getContratos(api);
    }

    public String getContratos(IntegracionAPI api) {
        String c = "";
        if (api instanceof Transaccional) c += "Transaccional ";
        if (api instanceof Documentable) c += "Documentable ";
        if (api instanceof Auditable) c += "Auditable ";
        if (api instanceof Cotizable) c += "Cotizable ";
        return c.trim();
    }

    public void ordenarPorLatencia(boolean descendente) {
        registro.ordenarPorLatencia(descendente);
    }

    // Ids de las integraciones que cumplen un contrato, ej. getIds(Transaccional.class)
    public String[] getIds(Class<?> contrato) {
        ArrayList<String> ids = new ArrayList<>();
        for (IntegracionAPI api : registro.getIntegraciones()) {
            if (contrato.isInstance(api)) ids.add(api.getId());
        }
        return ids.toArray(new String[0]);
    }

    // Si la API no es Transaccional la peticion se ignora, sin excepciones
    public String reintegro(String idApi, String idTransaccion, double monto) {
        IntegracionAPI api = registro.buscarPorId(idApi);
        if (api == null) return "No existe la integracion " + idApi;
        if (!(api instanceof Transaccional)) {
            return api.getNombre() + " no maneja dinero. Peticion de reintegro ignorada.";
        }
        return ((Transaccional) api).reintegrar(idTransaccion, monto);
    }

    public String verBitacoras() {
        String texto = "";
        for (IntegracionAPI api : registro.getIntegraciones()) {
            if (api instanceof Auditable) {
                String b = ((Auditable) api).getBitacora();
                texto += api.getNombre() + ":\n" + (b.isEmpty() ? "  (sin eventos)\n" : b);
            }
        }
        return texto;
    }

    public String checkout(String idPago, String idEnvio, String idFactura, double monto, double pesoKg, boolean express) {
        IntegracionAPI pago = registro.buscarPorId(idPago);
        IntegracionAPI envio = registro.buscarPorId(idEnvio);
        IntegracionAPI factura = registro.buscarPorId(idFactura);
        if (!(pago instanceof Transaccional) || !(envio instanceof Cotizable) || !(factura instanceof Documentable)) {
            return "ERROR: revise los ids, alguno no existe o no sirve para esa funcion.";
        }
        if (!envio.isConectada() || !factura.isConectada()) {
            return "ERROR: el proveedor de envio o el certificador esta desconectado.";
        }
        return procesarCompra((Transaccional) pago, (Cotizable) envio, (Documentable) factura, monto, pesoKg, express);
    }

    // Logica central: solo conoce interfaces, por eso una integracion nueva funciona sin cambiar nada aqui
    private String procesarCompra(Transaccional pago, Cotizable envio, Documentable facturador,
                                  double monto, double pesoKg, boolean express) {
        String pedido = "PED-" + contadorPedidos++;
        double total = monto + envio.cotizar(pesoKg, express);
        String cobro = pago.cobrar(total);
        if (cobro.startsWith("ERROR")) return cobro + "\nCompra cancelada.";

        String res = pedido + " | Total: GTQ " + String.format("%.2f", total) + "\n" + cobro
                + "\n" + facturador.generarDocumento(pedido);
        if (envio instanceof Documentable) {
            res += "\n" + ((Documentable) envio).generarDocumento(pedido);
        }
        return res;
    }
}
