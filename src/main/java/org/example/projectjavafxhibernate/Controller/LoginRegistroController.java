package org.example.projectjavafxhibernate.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.projectjavafxhibernate.Service.UsuarioService;

import java.io.IOException;

public class LoginRegistroController {

    @FXML
    private TextField txtUsuarioLogin;
    @FXML
    private PasswordField txtPasswordLogin;

    @FXML
    private TextField txtUsuarioRegistro;
    @FXML
    private PasswordField txtPasswordRegistro;

    private UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void handleLogin() {
        String username = txtUsuarioLogin.getText().trim();
        String password = txtPasswordLogin.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        if (usuarioService.validarUsuario(username, password)) {
            mostrarAlerta("Login Exitoso", "Bienvenido, " + username);
            cambiarAPantallaPrincipal();
        } else {
            mostrarAlerta("Error de Login", "Usuario o contraseña incorrectos.");
        }
    }

    public void cambiarAPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projectjavafxhibernate/FXML/PatientView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) txtUsuarioLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión de Pacientes");

            // Configurar tamaño fijo
            stage.setResizable(false);
            stage.setWidth(800); // Ancho fijo
            stage.setHeight(600); // Altura fija
            stage.centerOnScreen(); // Centrar en pantalla
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la pantalla principal.");
        }
    }







    @FXML
    private void handleRegistro() {
        String username = txtUsuarioRegistro.getText().trim();
        String password = txtPasswordRegistro.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return;
        }

        if (password.length() < 8) {
            mostrarAlerta("Error", "La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!username.matches("^[a-zA-Z0-9_.-]*$")) {
            mostrarAlerta("Error", "El nombre de usuario solo puede contener letras, números y los caracteres '_', '.' o '-'.");
            return;
        }

        if (usuarioService.registrarUsuario(username, password)) {
            mostrarAlerta("Registro Exitoso", "El usuario ha sido registrado correctamente.");
        } else {
            mostrarAlerta("Error de Registro", "El usuario ya existe.");
        }
    }


    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
