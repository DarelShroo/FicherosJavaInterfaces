package aed.proyectoficheros;

public class Estancias {
    String fechaInicio;
    String fechaFin;
    String nombre;

    public Estancias(String fechaInicio, String fechaFin, String nombre) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nombre = nombre;
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
