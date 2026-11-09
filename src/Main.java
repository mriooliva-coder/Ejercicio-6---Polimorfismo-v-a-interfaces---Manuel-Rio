// Programa principal: crea el controlador y abre la consola o la interfaz grafica.

import java.util.Scanner;
import javax.swing.SwingUtilities;

import controlador.ControladorIntegraciones;
import vista.VistaConsola;
import vista.VistaGUI;

public class Main {
    public static void main(String[] args) {
        ControladorIntegraciones controlador = new ControladorIntegraciones();
        Scanner sc = new Scanner(System.in);
        System.out.print("1. Consola  2. Interfaz grafica: ");
        if (sc.nextLine().trim().equals("2")) {
            SwingUtilities.invokeLater(() -> new VistaGUI(controlador).setVisible(true));
        } else {
            new VistaConsola(controlador, sc).iniciar();
        }
    }
}
