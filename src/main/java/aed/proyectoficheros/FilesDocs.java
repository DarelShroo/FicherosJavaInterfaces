package aed.proyectoficheros;

import java.util.Objects;

public class FilesDocs {
    private String nombre;
    private String ruta;
    private boolean isFile;

    public FilesDocs(String nombre, String ruta, boolean isFile) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.isFile = isFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilesDocs filesDocs = (FilesDocs) o;
        return isFile == filesDocs.isFile && Objects.equals(nombre, filesDocs.nombre) && Objects.equals(ruta, filesDocs.ruta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, ruta, isFile);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
