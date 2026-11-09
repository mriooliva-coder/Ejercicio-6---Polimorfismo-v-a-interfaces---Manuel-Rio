// Clase base de todas las integraciones con APIs externas.
package modelo;

public abstract class IntegracionAPI implements Comparable<IntegracionAPI> {

    private String id;
    private String nombre;
    private String endpoint;
    private int latenciaMs;
    private boolean conectada;

    public IntegracionAPI(String id, String nombre, String endpoint, int latenciaMs, boolean conectada) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
        this.latenciaMs = latenciaMs;
        this.conectada = conectada;
    }

    public abstract String getTipo();

    // Las subclases le agregan sus propios parametros
    public String getConfiguracion() {
        return "ID: " + id + "\nNombre: " + nombre + "\nTipo: " + getTipo()
                + "\nEndpoint: " + endpoint + "\nLatencia: " + latenciaMs + " ms"
                + "\nEstado: " + getEstado();
    }

    public String getEstado() {
        return conectada ? "CONECTADA" : "DESCONECTADA";
    }

    // Ordena de menor a mayor latencia
    @Override
    public int compareTo(IntegracionAPI otra) {
        return Integer.compare(latenciaMs, otra.latenciaMs);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IntegracionAPI)) return false;
        return id.equalsIgnoreCase(((IntegracionAPI) obj).id);
    }

    @Override
    public int hashCode() {
        return id.toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%-7s %-20s %-22s %4d ms  %s", id, nombre, getTipo(), latenciaMs, getEstado());
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEndpoint() { return endpoint; }
    public int getLatenciaMs() { return latenciaMs; }
    public void setLatenciaMs(int latenciaMs) { this.latenciaMs = latenciaMs; }
    public boolean isConectada() { return conectada; }
    public void setConectada(boolean conectada) { this.conectada = conectada; }
}
