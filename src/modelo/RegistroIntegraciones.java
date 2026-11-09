// Guarda la unica lista polimorfica de integraciones.
// Cambio de diseno: la lista estaba en el controlador, se movio aqui para separar el modelo.
package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroIntegraciones {

    private ArrayList<IntegracionAPI> integraciones = new ArrayList<>();

    // No acepta ids repetidos (usa equals)
    public boolean agregar(IntegracionAPI api) {
        if (integraciones.contains(api)) return false;
        return integraciones.add(api);
    }

    public IntegracionAPI buscarPorId(String id) {
        for (IntegracionAPI api : integraciones) {
            if (api.getId().equalsIgnoreCase(id.trim())) return api;
        }
        return null;
    }

    public void ordenarPorLatencia() {
        Collections.sort(integraciones);
    }

    public void ordenarPorLatencia(boolean descendente) {
        if (descendente) integraciones.sort(Collections.reverseOrder());
        else ordenarPorLatencia();
    }

    public List<IntegracionAPI> getIntegraciones() {
        return Collections.unmodifiableList(integraciones);
    }
}
