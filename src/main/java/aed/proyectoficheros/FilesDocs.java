package aed.proyectoficheros;

public class FilesDocs {
    private String nombre;
    private String ruta;
    private boolean isFile;

    public FilesDocs(String nombre, String ruta, boolean isFile) {
        this.nombre = nombre;
        this.ruta = ruta;
        this.isFile = isFile;
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
