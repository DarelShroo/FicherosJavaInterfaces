package aed.proyectoficheros;

import aed.proyectoficheros.avisos.Avisos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.JDOMParseException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class ControllerFicherosJdom implements Initializable {
    private File miDir = new File(".");

    @FXML
    private Button btnAniadirEstanciaProvisional;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardarEstancia;

    @FXML
    private Button btnModPreciodia;

    @FXML
    private Button btnVisualizar;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<?, ?> colFechaFin;

    @FXML
    private TableColumn<?, ?> colFechaInicio;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TextField lblFechaFin;

    @FXML
    private TextField lblFechaInicio;

    @FXML
    private TextField lblNombreCliente;

    @FXML
    private TextField lblNuevoPreciodia;

    @FXML
    private TextField lblNumHabitacion;

    @FXML
    private TextField lblNumHabitacionPreciodia;

    @FXML
    private TextField lblRutaActual;

    @FXML
    private TableView<Estancias> tblEstancias;

    @FXML
    private GridPane view;

    @FXML
    private TextField lblNumHabitacionEliminar;

    @FXML
    private TextArea txtAreaHabitaciones;

    private ObservableList<Estancias> estanciasObservableList;
    String fechaInicio = "";
    String fechaFin = "";
    String nombre = "";
    String numHabitacion = "";

    @FXML
    void onActionAniadirEstancia(ActionEvent event) throws IOException {
        String fechaInicio = this.lblFechaInicio.getText();
        String fechaFin = this.lblFechaFin.getText();
        String nombre = this.lblNombreCliente.getText();

        if(!validarFecha(fechaInicio) || !validarFecha(fechaFin)){
            Avisos.warningDialog("Aviso", "Fecha con formato incorrecto", "La fecha introducida tiene un formato incorrecto debe ser dd-MM-yyyy");
        }else {
            this.estanciasObservableList.add(new Estancias(fechaInicio, fechaFin, nombre));
            this.tblEstancias.setItems(this.estanciasObservableList);
            this.lblFechaInicio.setText("");
            this.lblFechaFin.setText("");
            this.lblNombreCliente.setText("");
        }
    }

    @FXML
    void onActionEliminar(ActionEvent event) {
        SAXBuilder builder = new SAXBuilder();
        File xml = new File(this.lblRutaActual.getText());

        try {
            Document document = builder.build(xml);
            Element root = document.getRootElement();

            List<Element> list = root.getChildren("habitacion");
            for (Element habitacion : list) {
                if (habitacion.getAttribute("numHabitacion").getValue().equals(this.lblNumHabitacionEliminar.getText())) {
                    habitacion.detach();
                    System.out.println("Se a borrado");
                }
            }
            XMLOutputter xml2 = new XMLOutputter();
            xml2.setFormat(Format.getPrettyFormat());
            try {
                xml2.output(document, new FileWriter(this.lblRutaActual.getText()));
            } catch (IOException e) {
                Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }
        } catch (JDOMException | IOException e) {
            Avisos.errorDialog("Error", "Error al guardar", "A ocurrido un error al intentar guardar el fichero, comprueba la ruta y los permisos");
        }
    }

    @FXML
    void onActionGuardarHabitacion(ActionEvent event) {
        String resultado = Avisos.confirmationDialog("Confirmación Nuevo Documento", "Nuevo documento", "¿De sea crear un nuevo documento?");
        File xml = new File(this.lblRutaActual.getText());
        SAXBuilder builder;
        Document document;
        Element root;

        try {
            builder = new SAXBuilder();
            document = builder.build(xml);
            root = document.getRootElement();


            List<Element> list = root.getChildren("habitacion");
            for (Element habitacion : list) {
                List<Element> valores_estancias = habitacion.getChildren("estancias");
                for (Element estancias : valores_estancias) {
                    if (habitacion.getAttribute("numHabitacion").getValue().equals(this.lblNumHabitacion.getText())) {
                        //Guardar multiples estancias del observableList
                        this.estanciasObservableList.forEach((tab) ->
                                estancias.addContent(new Element("cliente").
                                        setText(tab.nombre).
                                        setAttribute("fechaInicio", tab.fechaInicio).
                                        setAttribute("fechaFin", tab.fechaFin))
                        );
                    }
                }
            }
            XMLOutputter xml2 = new XMLOutputter();
            xml2.setFormat(Format.getPrettyFormat());
            try {
                if (resultado.equals("")) {
                    xml2.output(document, new FileWriter(this.lblRutaActual.getText()));
                    Avisos.informationDialog("Información", "Nuevas estancias", "Se han añadido nuevas estancias");
                }else {
                    xml2.output(document, new FileWriter(resultado));
                    Avisos.informationDialog("Información", "Nuevas estancias y nuevo documento", "Se a credo un nuevo documento");
                }
                this.estanciasObservableList.clear();
                this.tblEstancias.refresh();
            } catch (IOException e) {
                Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }
        } catch (JDOMException e) {
            Avisos.errorDialog("Error", "Error al guardar", "A ocurrido un error al intentar guardar el fichero, comprueba la ruta y los permisos");
        } catch (IOException e) {
            Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
        }

    }

    @FXML
    void onActionModPreciodia(ActionEvent event) {
        SAXBuilder builder = new SAXBuilder();
        File xml = new File(this.lblRutaActual.getText());

        try {
            Document document = builder.build(xml);
            Element root = document.getRootElement();

            List<Element> list = root.getChildren("habitacion");
            for (Element habitacion : list) {
                if (habitacion.getAttribute("numHabitacion").getValue().equals(this.lblNumHabitacionPreciodia.getText())) {
                    habitacion.getAttribute("preciodia").setValue(this.lblNuevoPreciodia.getText());
                }
            }
            XMLOutputter xml2 = new XMLOutputter();
            xml2.setFormat(Format.getPrettyFormat());
            try {
                xml2.output(document, new FileWriter(this.lblRutaActual.getText()));
            } catch (IOException e) {
                Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
            }
        } catch (JDOMException | IOException e) {
            Avisos.errorDialog("Error", "Error al modificar", "A ocurrido un error al intentar modificar el archivom, comprueba la ruta y los permisos");
        }
    }

    @FXML
    void onActionSwitchSceneMain(ActionEvent event) throws IOException {
        Controller.volver(btnVolver);
    }

    @FXML
    void onActionVisualizar(ActionEvent event) {
        SAXBuilder builder = new SAXBuilder();
        File xml = new File(this.lblRutaActual.getText());
        String datos = "";
        try {
            Document document = builder.build(xml);
            Element root = document.getRootElement();

            List<Element> list = root.getChildren("habitacion");
            for (Element habitacion : list) {
                datos += "numHabitacion = "+habitacion.getAttribute("numHabitacion").getValue()+", preciodia = "+ habitacion.getAttribute("preciodia").getValue()+", codHotel = " + habitacion.getChildTextTrim("codHotel") + ", ";

                List<Element> valores_estancias = habitacion.getChildren("estancias");
                datos += "Estancias = {\n";
                for (Element estancia : valores_estancias) {
                    for (Element cliente : estancia.getChildren("cliente")) {
                        datos += "\tfechaInicio = " + cliente.getAttribute("fechaInicio").getValue() + ", ";
                        datos += "fechaFin = " + cliente.getAttribute("fechaFin").getValue() + ", ";
                        datos += "nombre: " + cliente.getText() + ",";
                        datos += "\n";
                    }

                    datos = datos.substring(0, datos.length() - 2) + "\n}\n";
                }
            }

            this.txtAreaHabitaciones.setText(datos);
        } catch (JDOMParseException e) {
            Avisos.errorDialog("Error", "A ocurrido un error de lectura", "A ocurrido un error al intentar leer el fichero, comprueba la ruta");
        } catch (JDOMException | IOException e) {
            Avisos.errorDialog("Error", "A ocurrido un error de lectura", "A ocurrido un error al intentar leer el fichero, comprueba la ruta");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Tabla estancias
            this.estanciasObservableList = FXCollections.observableArrayList();
            this.lblRutaActual.setText(miDir.getCanonicalPath() + "\\habitaciones.xml");
            this.colFechaInicio.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
            this.colFechaFin.setCellValueFactory(new PropertyValueFactory("fechaFin"));
            this.colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

            //Parametros estancia
            this.fechaInicio = this.colFechaInicio.getText();
            this.fechaFin = this.colFechaFin.getText();
            this.nombre = this.colNombre.getText();
            this.numHabitacion = this.lblNumHabitacion.getText();

        } catch (IOException e) {
            Avisos.errorDialog("Error", "Error en tiempo de ejecución", "A ocurrido un error en tiempo de ejecución");
        }
    }

    public boolean validarFecha(String fecha) {
        boolean correcto = false;

        try {
            //Formato de fecha (día/mes/año)
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
            formatoFecha.setLenient(false);
            //Comprobación de la fecha
            formatoFecha.parse(fecha);
            correcto = true;
        } catch (ParseException e) {
            //Si la fecha no es correcta, pasará por aquí
            correcto = false;
        }

        return correcto;
    }
}
