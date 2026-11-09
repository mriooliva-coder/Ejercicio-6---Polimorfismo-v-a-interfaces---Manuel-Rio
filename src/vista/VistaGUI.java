// Interfaz grafica con Swing.
package vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controlador.ControladorIntegraciones;
import modelo.*;

public class VistaGUI extends JFrame {

    private ControladorIntegraciones controlador;
    private DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Tipo", "Contratos", "Latencia (ms)", "Estado"}, 0);
    private JTable tabla = new JTable(modelo);
    private JTextArea salida = new JTextArea(8, 40);

    public VistaGUI(ControladorIntegraciones controlador) {
        super("Integraciones e-commerce");
        this.controlador = controlador;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 560);
        setLocationRelativeTo(null);

        JButton btnBuscar = new JButton("Ver configuracion");
        JButton btnAsc = new JButton("Latencia menor a mayor");
        JButton btnDesc = new JButton("Latencia mayor a menor");
        JButton btnCompra = new JButton("Simular compra");
        JButton btnReintegro = new JButton("Reintegro");
        JButton btnBitacora = new JButton("Bitacoras");
        JPanel botones = new JPanel();
        for (JButton b : new JButton[]{btnBuscar, btnAsc, btnDesc, btnCompra, btnReintegro, btnBitacora}) {
            botones.add(b);
        }

        salida.setEditable(false);
        salida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        tabla.setDefaultEditor(Object.class, null);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(new JScrollPane(salida), BorderLayout.SOUTH);
        add(botones, BorderLayout.NORTH);

        btnBuscar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            String id = fila >= 0 ? (String) modelo.getValueAt(fila, 0) : JOptionPane.showInputDialog(this, "ID:");
            if (id != null) salida.setText(controlador.buscar(id));
        });
        btnAsc.addActionListener(e -> { controlador.ordenarPorLatencia(false); actualizar(); });
        btnDesc.addActionListener(e -> { controlador.ordenarPorLatencia(true); actualizar(); });
        btnCompra.addActionListener(e -> compra());
        btnReintegro.addActionListener(e -> reintegro());
        btnBitacora.addActionListener(e -> salida.setText(controlador.verBitacoras()));
        actualizar();
    }

    private void actualizar() {
        modelo.setRowCount(0);
        for (IntegracionAPI api : controlador.getIntegraciones()) {
            modelo.addRow(new Object[]{api.getId(), api.getNombre(), api.getTipo(),
                    controlador.getContratos(api), api.getLatenciaMs(), api.getEstado()});
        }
    }

    private void compra() {
        JComboBox<String> pago = new JComboBox<>(controlador.getIds(Transaccional.class));
        JComboBox<String> envio = new JComboBox<>(controlador.getIds(Cotizable.class));
        JComboBox<String> factura = new JComboBox<>(controlador.getIds(CertificadorFEL.class));
        JTextField monto = new JTextField("350");
        JTextField peso = new JTextField("2");
        JCheckBox express = new JCheckBox("Express");
        Object[] campos = {"Pago:", pago, "Envio:", envio, "Certificador FEL:", factura,
                "Monto (GTQ):", monto, "Peso (kg):", peso, express};
        if (JOptionPane.showConfirmDialog(this, campos, "Simular compra", JOptionPane.OK_CANCEL_OPTION) != 0) return;
        try {
            salida.setText(controlador.checkout((String) pago.getSelectedItem(), (String) envio.getSelectedItem(),
                    (String) factura.getSelectedItem(), Double.parseDouble(monto.getText()),
                    Double.parseDouble(peso.getText()), express.isSelected()));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Monto y peso deben ser numeros.");
        }
    }

    // Se muestran todas las APIs para poder probar que en las de logistica se ignora
    private void reintegro() {
        JComboBox<String> api = new JComboBox<>(controlador.getIds(IntegracionAPI.class));
        JTextField tx = new JTextField("TX-1001");
        JTextField monto = new JTextField("100");
        Object[] campos = {"API:", api, "ID transaccion:", tx, "Monto (GTQ):", monto};
        if (JOptionPane.showConfirmDialog(this, campos, "Reintegro", JOptionPane.OK_CANCEL_OPTION) != 0) return;
        try {
            salida.setText(controlador.reintegro((String) api.getSelectedItem(), tx.getText(),
                    Double.parseDouble(monto.getText())));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El monto debe ser un numero.");
        }
    }
}
