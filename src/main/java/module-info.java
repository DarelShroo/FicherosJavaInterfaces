module aed.proyectoficheros {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom;


    opens aed.proyectoficheros to javafx.fxml;
    exports aed.proyectoficheros;
}