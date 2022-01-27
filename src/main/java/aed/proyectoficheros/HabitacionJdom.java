package aed.proyectoficheros;

import java.io.Serial;
import java.io.Serializable;

public class HabitacionJdom implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String numerohabitacion;
    private String codigohotel;
    private String fechaInicio;
    private String fechaFin;
    private String nombreCliente;

    public HabitacionJdom(){

    }
    public HabitacionJdom(String numerohabitacion, String codigohotel,String fechaInicio, String fechaFin,String nombreCliente) {
        this.numerohabitacion = numerohabitacion;
        this.codigohotel = codigohotel;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombreCliente = nombreCliente;
    }

    public String getNumerohabitacion() {
        return numerohabitacion;
    }

    public void setNumerohabitacion(String numerohabitacion) {
        this.numerohabitacion = numerohabitacion;
    }

    public String getCodigohotel() {
        return codigohotel;
    }

    public void setCodigohotel(String codigohotel) {
        this.codigohotel = codigohotel;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
