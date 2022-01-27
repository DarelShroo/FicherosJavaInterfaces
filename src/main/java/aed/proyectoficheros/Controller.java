package aed.proyectoficheros;

import static java.nio.file.StandardCopyOption.*;

import aed.proyectoficheros.avisos.Avisos;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private File miDir = new File(".");
    //ficheros.fxml
    @FXML
    private Button btnCrear;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModFichero;

    @FXML
    private Button btnListar;

    @FXML
    private Button btnMover;

    @FXML
    private Button btnVerFichero;

    @FXML
    private Button btnVisualizar;

    @FXML
    private TableColumn<?, ?> colListado;

    @FXML
    private TableView<FilesDocs> tblListado;

    @FXML
    private TextArea txtAreaFichero;

    @FXML
    private TextField txtRutaActual;

    @FXML
    private GridPane view;

    @FXML
    private TextField txtRutaNuevoDocFile;

    @FXML
    private RadioButton esCarpeta;

    @FXML
    private RadioButton esFichero;

    ObservableList<FilesDocs> listDocFil;

    String select;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void onActionCrear(ActionEvent event) {
        try {
            if (select == null) {
                select = "carpeta";
            }

            File f = new File(txtRutaNuevoDocFile.getText());
            if (!f.exists()) {
                if (select.equals("carpeta")) {
                    if (!f.exists()) {
                        if (f.mkdirs()) {
                            Avisos.informationDialog("Aviso", "Carpeta creada", "Sa creado la carpeta");
                        } else if(select.equals("carpeta")) {
                            Avisos.errorDialog("Error", "Error al crear directorio",
                                    this.txtRutaNuevoDocFile.getText().equals("") ?
                                            "El campo nombre del nuevo fichero está vacio" :
                                            "No se a podido crear el directorio, ya existe o problemas con la ruta o permisos.");
                        }
                    }
                }
                if (select.equals("fichero") && !f.exists()) {
                    if(!this.txtRutaNuevoDocFile.getText().equals("")){
                        f.createNewFile();
                    }
                } else if (select.equals("fichero")) {
                    Avisos.errorDialog("Error", "Error al crear directorio",
                            this.txtRutaNuevoDocFile.getText().equals("") ?
                                    "El campo nombre del nuevo fichero está vacio" :
                                    "No se a podido crear el directorio, ya existe o problemas con la ruta o permisos.");
                }
            } else {
                Avisos.errorDialog("Error", "Documento existente", "Este documento ya existe");
            }
            rellenarListView();
        } catch (IOException e) {
            Avisos.errorDialog("Error", "Error en tiempo de ejecucion", "A ocurrido un error en tiempo de ejecución");
        } catch (NullPointerException e) {
            Avisos.warningDialog("Aviso", "Nombre nulo", "El campo nombre está vacio");
        }
    }

    @FXML
    void onActionEliminar(ActionEvent event) throws IOException {
        try {
            String dir = this.tblListado.getSelectionModel().getSelectedItem().getRuta();
            File f = new File(dir);

            if (f.isDirectory()) {
                if (Avisos.confirmationDialog("Aviso borrar", "Borrar carpeta", "Se va a proceder a borrar la carpeta y todo su contenido, estás seguro?").equals("si")) {
                    funcionEliminarCarpeta(f);
                    if (f.exists()) {
                        Avisos.errorDialog("Error", "No eliminado", "No se a podido eliminar");
                    } else {
                        Avisos.confirmationDialog("Aviso", "Borrado con éxito", "El fichero o documento se a borrado");
                    }
                }
            } else {
                if (Avisos.confirmationDialog("Aviso borrar", "Borrar un archivo", "Se va a proceder a borrar el archivo, estás seguro?").equals("si")) {
                    f.delete();
                    if (f.exists()) {
                        Avisos.errorDialog("Error", "No eliminado", "No se a podido eliminar");
                    } else {
                        Avisos.confirmationDialog("Aviso", "Borrado con éxito", "El fichero o documento se a borrado");
                    }
                }
            }

            this.txtRutaActual.setText(new File(".").getCanonicalPath());
            rellenarListView();
        } catch (NullPointerException e) {
            System.out.println("El nombre está a nulo");
        }
    }

    private static void funcionEliminarCarpeta(File pArchivo) {
        if (!pArchivo.exists()) {
            return;
        }

        if (pArchivo.isDirectory()) {
            for (File f : pArchivo.listFiles()) {
                funcionEliminarCarpeta(f);
            }
        }
        pArchivo.delete();
    } // Cerramos funcion.


    @FXML
    void onActionListar(ActionEvent event) {
        rellenarListView();
    }

    @FXML
    void onActionModificarFichero(ActionEvent event) {
        FilesDocs fd = this.tblListado.getSelectionModel().getSelectedItem();
        if (fd.isFile()) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(fd.getNombre(), "rw");
                raf.seek(0);
                raf.write(this.txtAreaFichero.getText().getBytes());
                raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onActionMover(ActionEvent event) {
        boolean continuar = false;
        String ruta = Avisos.textInputDialog("Aviso nueva Ruta", "Nueva ruta", "Introduce la nueva ruta");
        Path rutaActualDoc =new File(miDir + "\\" + tblListado.getSelectionModel().getSelectedItem().getNombre()).toPath();
        Path rutaDestino = new File((ruta +
                "\\" + tblListado.getSelectionModel().getSelectedItem().getNombre())).toPath();
        try {

            if(!rutaActualDoc.equals(rutaDestino)){
                Files.move(rutaActualDoc,
                        rutaDestino,
                        REPLACE_EXISTING);
                this.txtRutaActual.setText(new File(".").getCanonicalPath());
                rellenarListView();
            }else {
                if(Avisos.confirmationDialog("Aviso reemplazar", "Documento ya existente", "Este fichero o documento ya existe, desea reemplazarlo?").equals("si")){
                    Files.move(rutaActualDoc,
                            rutaDestino,
                            REPLACE_EXISTING);
                    this.txtRutaActual.setText(new File(".").getCanonicalPath());
                }
            }
        } catch (NoSuchFileException e) {
            Avisos.warningDialog("Warning", "Archivo o documento no encontrado", "Archivo o documento no encontrado, comprueba la ruta");
        } catch (IOException e) {
            Avisos.errorDialog("Error", "Error en tiempo de ejecucion", "A ocurrido un error en tiempo de ejecución");
        }
    }

    @FXML
    void listarDirectorioSeleccionado(MouseEvent event) throws IOException {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            this.txtRutaActual.setText(new File(".").getCanonicalPath() + "\\" + this.tblListado.getSelectionModel().getSelectedItem().getNombre());
            if (event.getClickCount() == 2) {
                FilesDocs fd = this.tblListado.getSelectionModel().getSelectedItem();
                if (fd.isFile()) {
                    try {
                        leerFichero(fd.getRuta());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    miDir = new File(fd.getRuta());
                    txtRutaActual.setText(fd.getRuta());
                    rellenarListView();
                }
            }
        }
    }

    public Controller() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/ficheros.fxml"));
        loader.setController(this);
        loader.load();
    }

    public GridPane getView() {
        return view;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup tg = new ToggleGroup();
        this.esCarpeta.setToggleGroup(tg);
        this.esCarpeta.setSelected(true);
        this.esFichero.setToggleGroup(tg);

        tg.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                RadioButton r = (RadioButton) t1;
                select = r.getText();
            }
        });


        try {
            this.txtRutaActual.setText(miDir.getCanonicalPath() + "\\");
            this.listDocFil = FXCollections.observableArrayList();
            this.colListado.setCellValueFactory(new PropertyValueFactory("nombre"));
            rellenarListView();
        } catch (IOException e) {
            Avisos.errorDialog("Error", "Error en tiempo de ejecucion", "A ocurrido un error en tiempo de ejecución");
        }
    }

    private void rellenarListView() {
        String ruta = this.txtRutaActual.getText();
        File f = new File(ruta);
        if (f.exists() && f.isDirectory()) {
            this.tblListado.getItems().clear();
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(Paths.get(ruta))) {
                for (Path dir : ds) {
                    this.listDocFil.add(new FilesDocs(dir.getFileName().toString(), dir.toString(), new File(dir.toString()).isFile()));
                }
                this.tblListado.setItems(this.listDocFil);
            } catch (IOException e) {
                Avisos.errorDialog("Error", "Error en tiempo de ejecucion", "A ocurrido un error en tiempo de ejecución");
            }
        }
    }

    public void leerFichero(String filePath) throws IOException {
        //Instantiating the File class
        File file = new File(filePath);
        //Instantiating the StringBuffer
        StringBuffer buffer = new StringBuffer();
        //instantiating the RandomAccessFile
        RandomAccessFile raFile = new RandomAccessFile(file, "r");
        //Reading each line using the readLine() method
        while (raFile.getFilePointer() < raFile.length()) {
            buffer.append(raFile.readLine() + System.lineSeparator());
        }
        String contents = buffer.toString();
        this.txtAreaFichero.setText(contents);
    }

    @FXML
    public void switchToSceneFicherosJdom(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ficherosJdom.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSceneRaf(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/ficherosRaf.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    static void volver(Button btnVolver) throws IOException {
        Controller controller = new Controller();
        Scene scene = new Scene(controller.getView());
        App.getPrimaryStage().setScene(scene);
        App.getPrimaryStage().show();

        try {
            Stage closeStage = (Stage) btnVolver.getScene().getWindow();
            closeStage.close();
        } catch (Exception ignored) {
        }
    }
}