package APP;

import MODELO.GasolineraDAO;
import VISTA.GasolineraView;
import CONTROLADOR.GasolineraController;

import javax.swing.SwingUtilities;

/**
 * Clase principal de la aplicación.
 * Inicializa los componentes del patrón MVC.
 * 
 * Esta clase representa el punto de entrada de la aplicación Java.
 * 
 * @author Carlos
 */
public class Main {

    /**
     * Método principal que lanza la interfaz gráfica de la aplicación.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        // Aseguramos que la interfaz gráfica se cargue en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            GasolineraDAO modelo = new GasolineraDAO();
            GasolineraView vista = new GasolineraView();
            new GasolineraController(modelo, vista);
        });
    }
}
