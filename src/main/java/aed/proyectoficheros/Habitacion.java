package aed.proyectoficheros;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habitacion that = (Habitacion) o;
        return codHabitacion == that.codHabitacion && capacidad == that.capacidad && preciodia == that.preciodia && activa == that.activa && Objects.equals(codigohotel, that.codigohotel) && Objects.equals(numerohabitacion, that.numerohabitacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigohotel, numerohabitacion, codHabitacion, capacidad, preciodia, activa);
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
