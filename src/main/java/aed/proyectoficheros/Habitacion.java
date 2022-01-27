package aed.proyectoficheros;

import java.io.Serial;
import java.io.Serializable;

public class Habitacion implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String codigohotel;
    private String numerohabitacion;
    private int codHabitacion;
    private int capacidad;
    private int preciodia;
    private boolean activa;

    public Habitacion(){

    }
    public Habitacion(String codigohotel, String numerohabitacion, int capacidad, int preciodia, boolean activa) {
        this.codigohotel = codigohotel;
        this.numerohabitacion = numerohabitacion;
        this.capacidad = capacidad;
        this.preciodia = preciodia;
        this.activa = activa;
    }

    public String getCodigohotel() {
        return codigohotel;
    }

    public int setCodHabitacion(int codHabitacion) {
        return codHabitacion;
    }

    public String getNumerohabitacion() {
        return numerohabitacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getPreciodia() {
        return preciodia;
    }

    public boolean isActiva() {
        return activa;
    }

    @Override
    public String toString() {
        return codigohotel +
                numerohabitacion +
                capacidad +
                preciodia +
                activa;
    }

}
