package MODELO;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un acceso a datos de gasolineras.
 * Se encarga de cargar información desde un archivo CSV real del Ministerio
 * y generar una lista de objetos {@link Gasolinera}.
 * 
 * Esta versión utiliza la biblioteca OpenCSV para un manejo robusto del CSV.
 * 
 * @author Carlos
 */
public class GasolineraDAO {

    /** 
     * Nombre del archivo CSV con los datos. 
     * Se asume que está en la raíz del classpath (ej. src/main/resources si usas Maven, o la raíz del proyecto).
     */
    private static final String NOMBRE_ARCHIVO_CSV = "preciosEESS_es.csv";

    /**
     * Carga los datos del archivo CSV y devuelve una lista de objetos {@link Gasolinera}.
     *
     * @return Lista de gasolineras cargadas desde el archivo CSV.
     */
    public List<Gasolinera> cargarGasolineras() {
        List<Gasolinera> lista = new ArrayList<>();

        // Intentar cargar el archivo como un recurso desde el classpath
        // Esto es más robusto que depender de la ruta relativa al directorio de ejecución.
        InputStream is = GasolineraDAO.class.getClassLoader().getResourceAsStream(NOMBRE_ARCHIVO_CSV);

        if (is == null) {
            System.err.println("Error: No se pudo encontrar el archivo CSV en el classpath: " + NOMBRE_ARCHIVO_CSV);
            // Alternativamente, intentar cargarlo desde el sistema de archivos como antes,
            // si esa es la intención y el archivo está en la raíz del proyecto.
            // try (FileReader fileReader = new FileReader(NOMBRE_ARCHIVO_CSV)) { ... }
            // Pero es preferible que esté en el classpath.
            return lista; // Devuelve lista vacía si no se encuentra el archivo
        }

        try (InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8); // Especificar UTF-8 por si acaso
             CSVReader csvReader = new CSVReaderBuilder(isr)
                     .withSkipLines(1) // Saltar la cabecera del CSV
                     .withCSVParser(new CSVParserBuilder()
                             .withSeparator(',')    // Especificar el separador, aunque ',' es el default
                             .withIgnoreQuotations(false) // Procesar campos entrecomillados (default)
                             .withQuoteChar('"')      // Carácter de comilla (default)
                             .build())
                     .build()) {
            
            String[] datos;
            while ((datos = csvReader.readNext()) != null) {
                try {
                    // Asegúrate de que los índices de columna son correctos para tu archivo CSV
                    // Provincia;Municipio;Localidad;Código Postal;Dirección; ...
                    //  0         1          2         3             4
                    // ... Latitud; Longitud (WGS84); ... Precio Gasolina 95 E5; ... Precio Gasolina 98 E5
                    //       X         Y (+1)                   Z (+N)                     W (+M)
                    // Revisa los índices exactos en tu archivo. Los que usaste son:
                    // datos[0]: Provincia
                    // datos[1]: Municipio
                    // datos[3]: Código postal (usado como id)
                    // datos[4]: Dirección
                    // datos[6]: Longitud
                    // datos[7]: Latitud
                    // datos[9]: Precio Gasolina 95 E5
                    // datos[12]: Precio Gasolina 98 E5

                    // Es buena práctica verificar la longitud del array 'datos'
                    // para evitar ArrayIndexOutOfBoundsException si alguna línea es anómala.
                    if (datos.length < 13) { // Ajusta este número al índice más alto que uses + 1
                        System.err.println("Línea con datos insuficientes (saltada): " + String.join(";", datos));
                        continue;
                    }

                    String provincia = datos[0];
                    String municipio = datos[1];
                    String id = datos[3]; // Código postal
                    String direccion = datos[4];
                    // El rótulo no se está extrayendo del CSV, se mantiene "Desconocido".
                    // Si el CSV tiene una columna "Rótulo", deberías usarla. Por ejemplo, si está en datos[24]:
                    // String rotulo = datos[24];
                    String rotulo = "Desconocido"; 

                    // Precios: Asegúrate de que estos son los índices correctos para los precios deseados.
                    double precio95 = parsearPrecio(datos[9]);     // Gasolina 95 E5
                    double precio98 = parsearPrecio(datos[12]);    // Gasolina 98 E5
                    
                    // Si tienes columnas para Gasóleo A y Premium, usa sus índices correspondientes.
                    // Ejemplo: double precioA = parsearPrecio(datos[14]); // Suponiendo que es Gasóleo A
                    // Ejemplo: double precioPremium = parsearPrecio(datos[15]); // Suponiendo que es Gasóleo Premium
                    double precioA = -1; // Mantener si no se mapea
                    double precioPremium = -1; // Mantener si no se mapea

                    // Coordenadas: Asegúrate de que el índice 6 es Longitud y 7 es Latitud.
                    // El archivo del ministerio suele tener Latitud primero y luego Longitud.
                    // Ejemplo: Latitud en datos[6], Longitud en datos[7]
                    // Revisa tu archivo CSV. Tu código actual:
                    // double latitud = Double.parseDouble(datos[7].replace(",", "."));
                    // double longitud = Double.parseDouble(datos[6].replace(",", "."));
                    // Esto parece correcto si datos[7] es Latitud y datos[6] es Longitud.

                    double latitud = parsearCoordenada(datos[7]);
                    double longitud = parsearCoordenada(datos[6]);

                    Gasolinera g = new Gasolinera(id, rotulo, direccion, municipio, provincia,
                            precio95, precio98, precioA, precioPremium,
                            latitud, longitud);
                    lista.add(g);

                } catch (NumberFormatException e) {
                    System.err.println("Error de formato numérico procesando línea (saltada): " + String.join(";", datos) + " - Error: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Error de índice fuera de límites procesando línea (saltada): " + String.join(";", datos) + " - Error: " + e.getMessage());
                } catch (Exception e) { // Captura genérica para otros errores inesperados en una línea
                    System.err.println("Error inesperado procesando línea (saltada): " + String.join(";", datos) + " - Error: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            System.err.println("Error de E/S leyendo el archivo CSV: " + e.getMessage());
            e.printStackTrace(); // Útil para depuración
        } catch (CsvValidationException e) { // Aunque readNext() la declara, es menos común con lectura línea a línea simple.
            System.err.println("Error de validación del CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Convierte un campo de texto con formato europeo (coma como decimal) a número decimal.
     * Si el campo está vacío, nulo o no es un número válido, devuelve -1.0 (o podrías lanzar excepción).
     *
     * @param valor Cadena de texto con el precio del carburante.
     * @return Valor numérico del precio o -1.0 si está vacío o es inválido.
     */
    private double parsearPrecio(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return -1.0;
        }
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            // System.err.println("Valor de precio no válido: '" + valor + "'"); // Opcional: loguear el valor problemático
            return -1.0; // Valor por defecto si la conversión falla
        }
    }

    /**
     * Convierte un campo de texto de coordenada (con coma como decimal) a número decimal.
     * Si el campo está vacío, nulo o no es un número válido, devuelve 0.0 o un valor indicativo de error.
     *
     * @param valor Cadena de texto con la coordenada.
     * @return Valor numérico de la coordenada o 0.0 si es inválido (considera una mejor gestión de errores).
     */
    private double parsearCoordenada(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0.0; // O NaN, o lanzar excepción
        }
        try {
            return Double.parseDouble(valor.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            // System.err.println("Valor de coordenada no válido: '" + valor + "'");
            return 0.0; // O NaN
        }
    }
}