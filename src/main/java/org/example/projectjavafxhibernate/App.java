package org.example.projectjavafxhibernate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projectjavafxhibernate/FXML/LoginRegistro.fxml"));
            Scene scene = new Scene(loader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestión de Usuarios");

            // Configurar tamaño fijo
            primaryStage.setResizable(false);
            primaryStage.setWidth(800); // Ancho fijo
            primaryStage.setHeight(600); // Altura fija
            primaryStage.centerOnScreen(); // Centrar en pantalla

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch();
    }
}
