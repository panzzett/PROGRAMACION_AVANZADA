package CONTROLADOR;

import MODELO.Gasolinera;
import MODELO.GasolineraDAO;
import VISTA.GasolineraView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador principal de la aplicación. 
 * Gestiona la interacción entre la vista y el modelo.
 * 
 * Se encarga de cargar los datos desde el modelo y pasarlos a la vista cuando el usuario lo solicita.
 * 
 * Forma parte de la capa de control dentro del patrón MVC.
 * 
 * @author Carlos
 */
public class GasolineraController {

    private GasolineraDAO modelo;
    private GasolineraView vista;

    /**
     * Constructor del controlador.
     * 
     * @param modelo Objeto DAO para acceder a los datos de gasolineras.
     * @param vista Vista de la aplicación basada en Swing.
     */
    public GasolineraController(GasolineraDAO modelo, GasolineraView vista) {
        this.modelo = modelo;
        this.vista = vista;

        // Asignar acción al botón "Cargar datos"
        this.vista.getBotonActualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarDatos();
            }
        });
    }

    /**
     * Método que solicita al modelo los datos y los envía a la vista.
     */
    private void cargarDatos() {
        List<Gasolinera> gasolineras = modelo.cargarGasolineras();
        Object[][] filas = new Object[gasolineras.size()][9];

        for (int i = 0; i < gasolineras.size(); i++) {
            Gasolinera g = gasolineras.get(i);
            filas[i][0] = g.getId();
            filas[i][1] = g.getRotulo();
            filas[i][2] = g.getDireccion();
            filas[i][3] = g.getMunicipio();
            filas[i][4] = g.getProvincia();
            filas[i][5] = g.getPrecioGasolina95();
            filas[i][6] = g.getPrecioGasolina98();
            filas[i][7] = g.getPrecioGasoleoA();
            filas[i][8] = g.getPrecioGasoleoPremium();
        }

        vista.actualizarTabla(filas);
    }
}
