// Vista de consola con el menu.
package vista;

import java.util.Scanner;

import controlador.ControladorIntegraciones;
import modelo.*;

public class VistaConsola {

    private ControladorIntegraciones controlador;
    private Scanner sc;

    // Cambio de diseno: recibe el Scanner del Main para no abrir dos sobre System.in
    public VistaConsola(ControladorIntegraciones controlador, Scanner sc) {
        this.controlador = controlador;
        this.sc = sc;
    }

    public void iniciar() {
        int op;
        do {
            System.out.println("\n1. Listar integraciones\n2. Buscar por ID\n3. Ordenar por latencia"
                    + "\n4. Simular compra\n5. Reintegro\n6. Ver bitacoras\n0. Salir");
            op = (int) pedirNumero("Opcion: ");
            switch (op) {
                case 1: System.out.println(controlador.listar()); break;
                case 2: System.out.println(controlador.buscar(pedir("ID: "))); break;
                case 3:
                    controlador.ordenarPorLatencia(pedir("De mayor a menor? (s/n): ").equalsIgnoreCase("s"));
                    System.out.println(controlador.listar());
                    break;
                case 4: compra(); break;
                case 5:
                    System.out.println(controlador.reintegro(pedir("ID de la API: "),
                            pedir("ID transaccion: "), pedirNumero("Monto: ")));
                    break;
                case 6: System.out.println(controlador.verBitacoras()); break;
                case 0: System.out.println("Adios"); break;
                default: System.out.println("Opcion no valida");
            }
        } while (op != 0);
    }

    private void compra() {
        System.out.println("Pagos: " + String.join(", ", controlador.getIds(Transaccional.class)));
        String pago = pedir("ID pago: ");
        System.out.println("Envios: " + String.join(", ", controlador.getIds(Cotizable.class)));
        String envio = pedir("ID envio: ");
        System.out.println("Facturas: " + String.join(", ", controlador.getIds(CertificadorFEL.class)));
        String factura = pedir("ID certificador: ");
        double monto = pedirNumero("Monto (GTQ): ");
        double peso = pedirNumero("Peso (kg): ");
        boolean express = pedir("Express? (s/n): ").equalsIgnoreCase("s");
        System.out.println(controlador.checkout(pago, envio, factura, monto, peso, express));
    }

    private String pedir(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private double pedirNumero(String msg) {
        while (true) {
            try {
                return Double.parseDouble(pedir(msg));
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero.");
            }
        }
    }
}
