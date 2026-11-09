// Interfaz para APIs que dan cotizaciones de envio.
// Cambio de diseno: antes cotizar estaba en ProveedorLogistica, se paso aqui para no hacer cast en el checkout.
package modelo;

public interface Cotizable {
    double cotizar(double pesoKg);
    double cotizar(double pesoKg, boolean express);
}
