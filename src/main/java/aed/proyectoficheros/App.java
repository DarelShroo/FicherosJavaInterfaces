package aed.proyectoficheros;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        App.primaryStage = new Stage();
        Controller controller = new Controller();
        Scene scene = new Scene(controller.getView());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Proyecto Ficheros");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}