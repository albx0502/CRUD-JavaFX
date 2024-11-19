package org.example.projectjavafxhibernate.Controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.projectjavafxhibernate.Model.Patient;
import org.example.projectjavafxhibernate.Service.PatientService;

import java.io.*;
import java.sql.Date;
import java.util.List;

public class PatientController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private DatePicker dateNacimientoPicker;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreoElectronico;
    @FXML
    private ChoiceBox<String> choiceSexo;
    @FXML
    private ChoiceBox<String> choiceGrupoSanguineo;
    @FXML
    private ChoiceBox<String> choiceEstadoCivil;
    @FXML
    private TableView<Patient> tablePatients;
    @FXML
    private TableColumn<Patient, String> colNombre;
    @FXML
    private TableColumn<Patient, String> colApellido;

    private PatientService patientService = new PatientService();
    private ObservableList<Patient> patientList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar choice boxes
        choiceSexo.getItems().addAll("M", "F");
        choiceGrupoSanguineo.getItems().addAll("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-");
        choiceEstadoCivil.getItems().addAll("Soltero", "Casado", "Divorciado", "Viudo");

        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));

        loadPatients();

        // Configurar listener para selección de tabla
        tablePatients.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedPatient) -> {
            if (selectedPatient != null) {
                txtNombre.setText(selectedPatient.getNombre());
                txtApellido.setText(selectedPatient.getApellido());
                dateNacimientoPicker.setValue(selectedPatient.getFechaNacimiento().toLocalDate());
                txtDireccion.setText(selectedPatient.getDireccion());
                txtTelefono.setText(selectedPatient.getTelefono());
                txtCorreoElectronico.setText(selectedPatient.getCorreoElectronico());
                choiceSexo.setValue(selectedPatient.getSexo());
                choiceGrupoSanguineo.setValue(selectedPatient.getGrupoSanguineo());
                choiceEstadoCivil.setValue(selectedPatient.getEstadoCivil());
            }
        });

        // Asegurarte de configurar el tamaño de la ventana después de la inicialización
        Platform.runLater(() -> {
            Stage stage = (Stage) tablePatients.getScene().getWindow();
            stage.setResizable(false); // Evitar redimensionamiento
            stage.setWidth(800); // Ancho fijo
            stage.setHeight(600); // Altura fija
            stage.centerOnScreen(); // Centrar en pantalla
        });
    }


    private void loadPatients() {
        patientList.clear();
        patientList.addAll(patientService.getAllPatients());
        tablePatients.setItems(patientList);
    }

    @FXML
    private void addPatient() {
        if (!validarCamposPaciente()) {
            return; // Detenemos la ejecución si hay errores de validación.
        }

        Patient patient = new Patient(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                Date.valueOf(dateNacimientoPicker.getValue()),
                txtDireccion.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreoElectronico.getText().trim(),
                choiceSexo.getValue(),
                choiceGrupoSanguineo.getValue(),
                choiceEstadoCivil.getValue()
        );

        patientService.addPatient(patient);
        loadPatients();
    }

    @FXML
    private void deletePatient() {
        Patient selectedPatient = tablePatients.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            patientService.deletePatient(selectedPatient.getId());
            loadPatients();
        }
    }

    @FXML
    private void editPatient() {
        Patient selectedPatient = tablePatients.getSelectionModel().getSelectedItem();
        if (selectedPatient == null) {
            mostrarAlerta("Error", "Selecciona un paciente para editar.");
            return;
        }

        if (!validarCamposPaciente()) {
            return;
        }

        selectedPatient.setNombre(txtNombre.getText().trim());
        selectedPatient.setApellido(txtApellido.getText().trim());
        selectedPatient.setFechaNacimiento(Date.valueOf(dateNacimientoPicker.getValue()));
        selectedPatient.setDireccion(txtDireccion.getText().trim());
        selectedPatient.setTelefono(txtTelefono.getText().trim());
        selectedPatient.setCorreoElectronico(txtCorreoElectronico.getText().trim());
        selectedPatient.setSexo(choiceSexo.getValue());
        selectedPatient.setGrupoSanguineo(choiceGrupoSanguineo.getValue());
        selectedPatient.setEstadoCivil(choiceEstadoCivil.getValue());

        patientService.updatePatient(selectedPatient);
        loadPatients();
    }

    // Método para validar campos de paciente
    private boolean validarCamposPaciente() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreoElectronico.getText().trim();

        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || dateNacimientoPicker.getValue() == null) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.");
            return false;
        }

        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlerta("Error", "El nombre solo puede contener letras y espacios.");
            return false;
        }

        if (!apellido.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlerta("Error", "El apellido solo puede contener letras y espacios.");
            return false;
        }

        if (!telefono.matches("\\d+")) {
            mostrarAlerta("Error", "El teléfono solo puede contener números.");
            return false;
        }

        if (!correo.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            mostrarAlerta("Error", "El correo electrónico no es válido.");
            return false;
        }

        return true;
    }
    @FXML
    public void cerrarSesion() {
        try {
            // Cargar el archivo FXML del login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/projectjavafxhibernate/FXML/LoginRegistro.fxml"));
            Scene scene = new Scene(loader.load());

            // Obtener la ventana actual y cambiar la escena
            Stage stage = (Stage) tablePatients.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestión de Usuarios - Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cerrar la sesión.");
        }
    }
    @FXML
    private void exportToCSV() {
        try {
            // Ruta donde se guardará el archivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar archivo CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                List<Patient> patients = patientService.getAllPatients();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    // Cabeceras del archivo CSV
                    writer.write("ID,Nombre,Apellido,Fecha Nacimiento,Direccion,Telefono,Correo Electronico,Sexo,Grupo Sanguineo,Estado Civil\n");

                    // Escribir los datos de los pacientes
                    for (Patient patient : patients) {
                        writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                                patient.getId(),
                                patient.getNombre(),
                                patient.getApellido(),
                                patient.getFechaNacimiento(),
                                patient.getDireccion(),
                                patient.getTelefono(),
                                patient.getCorreoElectronico(),
                                patient.getSexo(),
                                patient.getGrupoSanguineo(),
                                patient.getEstadoCivil()));
                    }
                }
                mostrarAlertaInformacion("Éxito", "Datos exportados correctamente a " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo exportar los datos: " + e.getMessage());
        }
    }
    @FXML
    private void importFromCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int totalImportados = 0;
                int duplicados = 0;
                boolean isFirstLine = true; // Para manejar encabezados

                while ((line = br.readLine()) != null) {
                    line = line.trim();

                    // Saltar líneas vacías
                    if (line.isEmpty()) {
                        continue;
                    }

                    // Saltar la primera línea si tiene encabezados
                    if (isFirstLine) {
                        if (line.toLowerCase().contains("id") && line.toLowerCase().contains("nombre")) {
                            isFirstLine = false;
                            continue;
                        }
                        isFirstLine = false;
                    }

                    String[] values = line.split(",");

                    // Validar longitud de la línea
                    if (values.length != 10) {
                        mostrarAlerta("Error", "Formato incorrecto en la línea:\n" + line);
                        continue; // Saltar esta línea y seguir con la siguiente
                    }

                    try {
                        // Asignar valores a cada campo
                        int id = Integer.parseInt(values[0].trim());
                        String nombre = values[1].trim();
                        String apellido = values[2].trim();
                        Date fechaNacimiento = Date.valueOf(values[3].trim());
                        String direccion = values[4].trim();
                        String telefono = values[5].trim();
                        String correoElectronico = values[6].trim();
                        String sexo = values[7].trim();
                        String grupoSanguineo = values[8].trim();
                        String estadoCivil = values[9].trim();

                        // Validar datos básicos
                        if (!correoElectronico.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                            mostrarAlerta("Error", "Correo electrónico inválido en la línea:\n" + line);
                            continue;
                        }
                        if (!telefono.matches("\\d+")) {
                            mostrarAlerta("Error", "Teléfono inválido en la línea:\n" + line);
                            continue;
                        }

                        // Validar si ya existe un paciente con el mismo correo electrónico
                        if (patientService.getAllPatients().stream()
                                .anyMatch(p -> p.getCorreoElectronico().equalsIgnoreCase(correoElectronico))) {
                            duplicados++;
                            continue; // Saltar duplicados
                        }

                        // Crear y guardar el paciente
                        Patient patient = new Patient(nombre, apellido, fechaNacimiento, direccion, telefono,
                                correoElectronico, sexo, grupoSanguineo, estadoCivil);
                        patientService.addPatient(patient);
                        totalImportados++;

                    } catch (IllegalArgumentException e) {
                        mostrarAlerta("Error", "Error en los datos de la línea:\n" + line + "\n" + e.getMessage());
                    } catch (Exception e) {
                        mostrarAlerta("Error", "Error inesperado en la línea:\n" + line + "\n" + e.getMessage());
                    }
                }

                mostrarAlertaInformacion("Importación Completa",
                        "Pacientes importados: " + totalImportados + "\nDuplicados: " + duplicados);
                loadPatients(); // Actualizar la tabla

            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo leer el archivo: " + e.getMessage());
            }
        }
    }



    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void mostrarAlertaInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }




}
