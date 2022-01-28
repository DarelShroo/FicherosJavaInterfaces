package aed.proyectoficheros;

import aed.proyectoficheros.avisos.Avisos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFicherosRaf implements Initializable {
    //ficherosRaf.fxml
    File miDir = new File(".");

    @FXML
    private Button btnAniadir;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnModPreciodia;

    @FXML
    private Button btnVisualizarRaf;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<?, ?> colActiva;

    @FXML
    private TableColumn<?, ?> colCapacidad;

    @FXML
    private TableColumn<?, ?> colCodHotel;

    @FXML
    private TableColumn<?, ?> colNumHabitacion;

    @FXML
    private TableColumn<?, ?> colPreciodia;

    @FXML
    private TextField lblCodHotel;

    @FXML
    private TextField lblNumCapacidad;

    @FXML
    private TextField lblNumHabitacion;

    @FXML
    private TextField lblPreciodiaMod;

    @FXML
    private TextField lblPreciodia;

    @FXML
    private TableView<Habitacion> tblHabitaciones;

    @FXML
    private TextArea lblIdPreciodia;

    @FXML
    private TextField lblRuta;

    @FXML
    private TextField lblIdHabitacion;

    @FXML
    private GridPane viewRaf;

    @FXML
    private RadioButton rdBtnTrue;

    @FXML
    private RadioButton rdBtnFalse;

    @FXML
    private Button btnCrearNuevoDoc;

    private ObservableList<Habitacion> habitaciones;

    private String select = "true";

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void onActioInsertarDatosTable(ActionEvent event) {
        String codigoHotel = this.lblCodHotel.getText();
        String numeroHabitacion = this.lblNumHabitacion.getText();
        int capacidad = Integer.parseInt(this.lblNumCapacidad.getText());
        int preciodia = Integer.parseInt(this.lblPreciodia.getText());
        boolean activa = Boolean.parseBoolean(this.select);
        if (codigoHotel.length() < 4 ) {
            Avisos.warningDialog("Aviso", "longitud menor", "la longitud de codigoHotel debe ser de 4 caracteres");
        } else if (numeroHabitacion.length() < 6) {
            Avisos.warningDialog("Aviso", "longitud menor", "la longitud de numeroHabitacion debe ser de 6 caracteres");
        } else {
            Habitacion nuevaHabitacion = new Habitacion();
            try {

                nuevaHabitacion = new Habitacion(codigoHotel, numeroHabitacion, capacidad, preciodia, activa);

                if (habitaciones.contains(nuevaHabitacion)) {
                    Avisos.warningDialog("Aviso", "Habitacion ya existente", "Esta habitación ya existe en la lista");
                } else {
                    habitaciones.add(nuevaHabitacion);
                    this.tblHabitaciones.setItems(habitaciones);

                    this.lblCodHotel.setText("");
                    this.lblNumHabitacion.setText("");
                    this.lblNumCapacidad.setText("");
                    this.lblPreciodia.setText("");
                    this.rdBtnFalse.setSelected(true);
                }
            } catch (NumberFormatException e) {
                Avisos.warningDialog("Aviso", "Numero no válido", "A ocurrido un error, hay letras donde deberían haber números");
            } catch (RuntimeException e) {
               // Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }
        }
    }

    @FXML
    void onActionGuardar(ActionEvent event) {
        int posSeek = ((ultimoIndice()) * 33);
        System.out.println(posSeek);
        int indice = ultimoIndice() + 1;

        if(new File(this.lblRuta.getText()).exists() && new File(this.lblRuta.getText()).isFile()){
            try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {
                for (Habitacion habitacion : habitaciones) {
                    raf.seek(posSeek);
                    //4
                    raf.writeInt(indice);

                    //12
                    StringBuffer sb = new StringBuffer(habitacion.getCodigohotel());
                    sb.setLength(6);
                    raf.writeChars(sb.toString());

                    //8
                    StringBuffer sb2 = new StringBuffer(habitacion.getNumerohabitacion());
                    sb2.setLength(4);
                    raf.writeChars(sb2.toString());

                    //4*2 = 8
                    raf.writeInt(habitacion.getCapacidad());
                    raf.writeInt(habitacion.getPreciodia());

                    //1
                    raf.writeBoolean(habitacion.isActiva());

                    //int = 4
                    //String 2 x Caracter
                    //Double 8
                    //Boolean 1
                    //char 2

                    //1+20+4
                    indice++;
                }

                this.habitaciones.clear();

                this.onActionVisualizar(event);
            } catch (IOException e) {
               // Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución, al intentar guardar el documento. Comprueba la ruta");
            }
        }else {
          // Avisos.errorDialog("Error", "Documento inexistente", "El documento no existe, debes crearlo. O quizás sea la ruta hacia una carpeta y no un fichero.");
        }
    }

    @FXML
    void onActionModPreciodia(ActionEvent event) throws NumberFormatException {
        try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {

            int posicionPreciodia = posicionPreciodia(Integer.parseInt(this.lblIdHabitacion.getText()));
            int nuevoPreciodia = Integer.parseInt(this.lblPreciodiaMod.getText());

            raf.seek(posicionPreciodia);
            raf.writeInt(nuevoPreciodia);

            raf.seek(posicionPreciodia);
            if (raf.readInt() == Integer.parseInt(this.lblPreciodiaMod.getText())) {
                Avisos.informationDialog("Confirmación", "Precio modificado", "Se han modificado los datos, precio actualizado");
            } else {
                Avisos.warningDialog("Aviso", "No modificado", "El archivo que trata de modificar no se a actualizado con los nuevos datos");
            }

            onActionVisualizar(new ActionEvent());

        } catch (EOFException | NumberFormatException | FileNotFoundException ignored) {
            if (ignored.getLocalizedMessage().equals("NumberFormatException")) {
                Avisos.errorDialog("Error", "Error numero no válido", "Hay letras donde deberían haber números");
            } else if (ignored.getLocalizedMessage().equals("FileNotFoundException")) {
                Avisos.warningDialog("Aviso", "No se encuentra", "El archivo que trata de modificar no se encuentra");
            } else if (ignored.getLocalizedMessage().equals("EOFException")) {
                Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }
        } catch (IOException e) {
            //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
        }
    }

    private int posicionPreciodia(int indice) {
        int indicePosicionesSeeker = 0;
        int aux = 1;
        int miIndiceFinal = 0;
        int posicionPreciodia = 0;

        if(indice > 1) {
            try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {
                //12+8+2+2+1
                raf.seek(indicePosicionesSeeker);
                while (indice != miIndiceFinal) {
                    indice = raf.readInt();
                    if (indice == (aux++)) {
                        miIndiceFinal++;
                    }
                    indicePosicionesSeeker += 33;
                }

            } catch (EOFException | NumberFormatException e) {
                Avisos.errorDialog("Error", "Error numero no válido", "Este numero de indice no es válido");
            } catch (IOException e) {
                //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }

            posicionPreciodia = (33 * miIndiceFinal) + 24;
        }else {
            posicionPreciodia = 24;
        }
        //Encontrar posición preciodia a modificar.
        return posicionPreciodia;
    }

    @FXML
    void onActionVisualizar(ActionEvent event) {
        int indicePosicionesSeeker = 0;
        int indice = 1;
        int aux = 1;
        int miIndice = 1;
        String mostrar = "";
        String codHotel = "";
        String numHabitacion = "";
        try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {
            //12+8+2+2+1
            while (true) {
                raf.seek(indicePosicionesSeeker);
                indice = raf.readInt();
                if (indice == (aux++)) {
                    mostrar += miIndice + "\n,\n";
                    for (int i = 0; i < 6; i++) {
                        codHotel += raf.readChar();
                    }
                    mostrar += codHotel + "\n,\n";

                    for (int i = 0; i < 4; i++) {
                        numHabitacion += raf.readChar();
                    }
                    mostrar += numHabitacion + "\n,\n";
                    mostrar += raf.readInt() + "\n,\n";
                    mostrar += raf.readInt() + "\n,\n";
                    mostrar += raf.readBoolean() + "\n,\n";
                    miIndice++;
                }
                codHotel = "";
                numHabitacion = "";
                indicePosicionesSeeker += 33;
            }


        } catch (EOFException e) {
            try {
                this.lblIdPreciodia.setText(mostrar.substring(0, mostrar.length() - 2));
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        } catch (IOException e) {
            //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución comprueba la ruta.");
        }
    }

    @FXML
    void switchToScenePrincipal(ActionEvent event) throws IOException {
        Controller.volver(btnVolver);
    }

    @FXML
    void onActionCrearNuevoDoc(ActionEvent event) throws IOException {
        int indice = 1;

        try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {
            for (Habitacion habitacion : habitaciones) {
                //4
                raf.writeInt(habitacion.setCodHabitacion(indice));

                //12
                StringBuffer sb = new StringBuffer(habitacion.getCodigohotel());
                sb.setLength(6);
                raf.writeChars(sb.toString());

                //8
                StringBuffer sb2 = new StringBuffer(habitacion.getNumerohabitacion());
                sb2.setLength(4);
                raf.writeChars(sb2.toString());

                //4*2 = 8
                raf.writeInt(habitacion.getCapacidad());
                raf.writeInt(habitacion.getPreciodia());

                //1
                raf.writeBoolean(habitacion.isActiva());

                //int = 4
                //String 2 x Caracter
                //Double 8
                //Boolean 1
                //char 2

                //1+20+4
                indice++;
            }

            this.habitaciones.clear();

            this.onActionVisualizar(event);
        } catch (IOException e) {
            //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución comprueba la ruta.");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Tabla de datos
        this.habitaciones = FXCollections.observableArrayList();
        this.colCodHotel.setCellValueFactory(new PropertyValueFactory("codigohotel"));
        this.colNumHabitacion.setCellValueFactory(new PropertyValueFactory("numerohabitacion"));
        this.colCapacidad.setCellValueFactory(new PropertyValueFactory("capacidad"));
        this.colPreciodia.setCellValueFactory(new PropertyValueFactory("preciodia"));
        this.colActiva.setCellValueFactory(new PropertyValueFactory("activa"));

        //Agregado al mismo grupo los radio buttons
        ToggleGroup tg = new ToggleGroup();
        this.rdBtnFalse.setToggleGroup(tg);
        this.rdBtnFalse.setSelected(true);
        this.rdBtnTrue.setToggleGroup(tg);

        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton r = (RadioButton) t1;
                select = r.getText();
            }
        });

        try {
            //Set de la ruta por defecto en label
            lblRuta.setText(miDir.getCanonicalPath() + "\\habitacion.dat");
        } catch (IOException e) {
            //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución comprueba la ruta.");
        }
    }

    private int ultimoIndice() {
        int indicePosicionesSeeker = 0;
        int indice = 1;
        int aux = 1;
        int miIndiceFinal = 1;
        try (RandomAccessFile raf = new RandomAccessFile(this.lblRuta.getText(), "rw")) {
            //12+8+2+2+1
            while (true) {
                raf.seek(indicePosicionesSeeker);
                indice = raf.readInt();
                if (indice == (aux++)) {
                    miIndiceFinal++;
                    System.out.println(miIndiceFinal);
                }
                indicePosicionesSeeker += 33;
            }
        } catch (FileNotFoundException e) {
            Avisos.errorDialog("Warning", "Archivo no encontrado", "A ocurrido un error al intentar leer el documento, comprueba la ruta.");
        } catch (IOException e) {
            //Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución comprueba la ruta.");
        }
        return aux;
    }
}
