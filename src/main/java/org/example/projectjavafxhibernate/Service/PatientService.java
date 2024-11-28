package org.example.projectjavafxhibernate.Service;

import org.example.projectjavafxhibernate.DAO.PatientDAO;
import org.example.projectjavafxhibernate.Model.Patient;
import org.example.projectjavafxhibernate.Util.DatabaseUtil;
import org.example.projectjavafxhibernate.Util.HibernateUtil;
import javafx.scene.control.Alert;

import java.util.List;

public class PatientService {

    private PatientDAO patientDAO = new PatientDAO();

    public void addPatient(Patient patient) {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return; // Detenemos la operación si no hay conexión
        }

        // Validar campos con regex
        if (!patient.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlertaError("Error de Validación", "El nombre solo puede contener letras y espacios.");
            return;
        }
        if (!patient.getApellido().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlertaError("Error de Validación", "El apellido solo puede contener letras y espacios.");
            return;
        }
        if (!patient.getCorreoElectronico().matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            mostrarAlertaError("Error de Validación", "El correo electrónico no es válido.");
            return;
        }
        if (!patient.getTelefono().matches("^\\d{9}$")) {
            mostrarAlertaError("Error de Validación", "El número de teléfono debe tener 9 dígitos.");
            return;
        }

        try {
            patientDAO.savePatient(patient);
        } catch (Exception e) {
            mostrarAlertaError("Error al Añadir Paciente", "No se pudo guardar el paciente. Detalle: " + e.getMessage());
        }
    }

    public List<Patient> getAllPatients() {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return null; // Detenemos la operación si no hay conexión
        }

        try {
            return patientDAO.getAllPatients();
        } catch (Exception e) {
            mostrarAlertaError("Error al Cargar Pacientes", "No se pudieron cargar los pacientes. Detalle: " + e.getMessage());
            return null;
        }
    }

    public void updatePatient(Patient patient) {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return; // Detenemos la operación si no hay conexión
        }

        // Validar campos con regex antes de actualizar
        if (!patient.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            mostrarAlertaError("Error de Validación", "El nombre solo puede contener letras y espacios.");
            return;
        }
        if (!patient.getCorreoElectronico().matches("^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$")) {
            mostrarAlertaError("Error de Validación", "El correo electrónico no es válido.");
            return;
        }

        try {
            patientDAO.updatePatient(patient);
        } catch (Exception e) {
            mostrarAlertaError("Error al Actualizar Paciente", "No se pudo actualizar el paciente. Detalle: " + e.getMessage());
        }
    }

    public void deletePatient(Long id) {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return; // Detenemos la operación si no hay conexión
        }

        try {
            patientDAO.deletePatient(id);
        } catch (Exception e) {
            mostrarAlertaError("Error al Eliminar Paciente", "No se pudo eliminar el paciente. Detalle: " + e.getMessage());
        }
    }

    private void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
