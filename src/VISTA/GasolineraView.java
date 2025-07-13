package VISTA;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Vista de la aplicación que muestra una tabla con las gasolineras
 * y un botón para cargar o actualizar los datos desde el modelo.
 * 
 * Esta clase forma parte de la capa de presentación (Vista) en el patrón MVC.
 * 
 * @author Carlos
 */
public class GasolineraView extends JFrame {

    private JTable tablaGasolineras;
    private DefaultTableModel modeloTabla;
    private JButton botonActualizar;

    /**
     * Constructor. Inicializa y configura los componentes de la interfaz gráfica.
     */
    public GasolineraView() {
        setTitle("Gasolineras - Precios de Carburantes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        // Configurar tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Rótulo", "Dirección", "Municipio", "Provincia",
            "Gasolina 95", "Gasolina 98", "Gasóleo A", "Gasóleo Premium"
        });

        tablaGasolineras = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaGasolineras);

        // Botón de actualización
        botonActualizar = new JButton("Cargar datos");

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(botonActualizar, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    /**
     * Devuelve el botón de actualizar para poder asignarle un controlador.
     * @return JButton de actualización
     */
    public JButton getBotonActualizar() {
        return botonActualizar;
    }

    /**
     * Limpia la tabla y añade nuevas filas con los datos proporcionados.
     * 
     * @param filas Array de objetos (una fila por cada gasolinera).
     */
    public void actualizarTabla(Object[][] filas) {
        modeloTabla.setRowCount(0); // Limpiar
        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }
    }
}
