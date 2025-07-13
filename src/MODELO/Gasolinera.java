package MODELO;

/**
 * Clase que representa una estación de servicio (gasolinera) con su ubicación,
 * marca y precios de distintos tipos de carburantes.
 * 
 * Esta clase forma parte del modelo de la aplicación.
 * 
 * @author Carlos Galvez
 */
public class Gasolinera {

    private String id;
    private String rotulo;
    private String direccion;
    private String municipio;
    private String provincia;
    private double precioGasolina95;
    private double precioGasolina98;
    private double precioGasoleoA;
    private double precioGasoleoPremium;
    private double latitud;
    private double longitud;

    /**
     * Constructor completo para crear un objeto Gasolinera.
     * 
     * @param id Identificador de la estación.
     * @param rotulo Nombre comercial (marca).
     * @param direccion Dirección completa.
     * @param municipio Municipio donde se ubica.
     * @param provincia Provincia.
     * @param precioGasolina95 Precio del carburante Gasolina 95.
     * @param precioGasolina98 Precio del carburante Gasolina 98.
     * @param precioGasoleoA Precio del gasóleo A.
     * @param precioGasoleoPremium Precio del gasóleo premium.
     * @param latitud Latitud geográfica.
     * @param longitud Longitud geográfica.
     */
    public Gasolinera(String id, String rotulo, String direccion, String municipio, String provincia,
                      double precioGasolina95, double precioGasolina98,
                      double precioGasoleoA, double precioGasoleoPremium,
                      double latitud, double longitud) {
        this.id = id;
        this.rotulo = rotulo;
        this.direccion = direccion;
        this.municipio = municipio;
        this.provincia = provincia;
        this.precioGasolina95 = precioGasolina95;
        this.precioGasolina98 = precioGasolina98;
        this.precioGasoleoA = precioGasoleoA;
        this.precioGasoleoPremium = precioGasoleoPremium;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Métodos getters

    public String getId() { return id; }
    public String getRotulo() { return rotulo; }
    public String getDireccion() { return direccion; }
    public String getMunicipio() { return municipio; }
    public String getProvincia() { return provincia; }
    public double getPrecioGasolina95() { return precioGasolina95; }
    public double getPrecioGasolina98() { return precioGasolina98; }
    public double getPrecioGasoleoA() { return precioGasoleoA; }
    public double getPrecioGasoleoPremium() { return precioGasoleoPremium; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }

    /**
     * Representación en texto del objeto Gasolinera.
     * 
     * @return Información resumida con marca, dirección, municipio y provincia.
     */
    @Override
    public String toString() {
        return rotulo + " - " + direccion + " (" + municipio + ", " + provincia + ")";
    }
}
