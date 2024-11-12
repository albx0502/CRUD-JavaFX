package org.example.projectjavafxhibernate.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.projectjavafxhibernate.Model.Patient;
import org.example.projectjavafxhibernate.Service.PatientService;

import java.sql.Date;

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
        choiceSexo.getItems().addAll("M", "F");
        choiceGrupoSanguineo.getItems().addAll("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-");
        choiceEstadoCivil.getItems().addAll("Soltero", "Casado", "Divorciado", "Viudo");

        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));

        loadPatients();

        // Añadir listener para llenar los campos al seleccionar un paciente
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
    }

    private void loadPatients() {
        patientList.clear();
        patientList.addAll(patientService.getAllPatients());
        tablePatients.setItems(patientList);
    }

    @FXML
    private void addPatient() {
        Patient patient = new Patient(
                txtNombre.getText(),
                txtApellido.getText(),
                Date.valueOf(dateNacimientoPicker.getValue()),
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtCorreoElectronico.getText(),
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
        if (selectedPatient != null) {
            // Obtener los nuevos valores desde los campos de texto
            selectedPatient.setNombre(txtNombre.getText());
            selectedPatient.setApellido(txtApellido.getText());

            if (dateNacimientoPicker.getValue() != null) {
                selectedPatient.setFechaNacimiento(Date.valueOf(dateNacimientoPicker.getValue()));
            }

            selectedPatient.setDireccion(txtDireccion.getText());
            selectedPatient.setTelefono(txtTelefono.getText());
            selectedPatient.setCorreoElectronico(txtCorreoElectronico.getText());
            selectedPatient.setSexo(choiceSexo.getValue());
            selectedPatient.setGrupoSanguineo(choiceGrupoSanguineo.getValue());
            selectedPatient.setEstadoCivil(choiceEstadoCivil.getValue());

            // Llamar al servicio para actualizar en la base de datos
            patientService.updatePatient(selectedPatient);
            loadPatients();
        }
    }
}
