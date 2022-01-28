package aed.proyectoficheros;

import java.util.Objects;

public class Estancias {
    String fechaInicio;
    String fechaFin;
    String nombre;

    public Estancias(String fechaInicio, String fechaFin, String nombre) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombre = nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estancias estancias = (Estancias) o;
        return Objects.equals(fechaInicio, estancias.fechaInicio) && Objects.equals(fechaFin, estancias.fechaFin) && Objects.equals(nombre, estancias.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaInicio, fechaFin, nombre);
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
