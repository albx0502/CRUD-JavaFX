package org.example.projectjavafxhibernate.Service;

import org.example.projectjavafxhibernate.Model.Usuario;
import org.example.projectjavafxhibernate.Util.DatabaseUtil;
import org.example.projectjavafxhibernate.Util.HibernateUtil;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioService {

    public boolean validarUsuario(String username, String password) {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return false; // Detenemos la operación si no hay conexión
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.createQuery("FROM Usuario WHERE username = :username AND password = :password", Usuario.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
            return usuario != null;
        } catch (Exception e) {
            mostrarAlertaError("Error de Validación", "No se pudo validar al usuario. Detalle: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarUsuario(String username, String password) {
        if (!DatabaseUtil.verificarConexion(HibernateUtil.getSessionFactory())) {
            mostrarAlertaError("Error Crítico", "No se pudo conectar a la base de datos.");
            return false; // Detenemos la operación si no hay conexión
        }

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (session.createQuery("FROM Usuario WHERE username = :username", Usuario.class)
                    .setParameter("username", username)
                    .uniqueResult() != null) {
                mostrarAlertaError("Error de Registro", "El usuario ya existe.");
                return false; // Usuario ya existe
            }

            transaction = session.beginTransaction();
            Usuario usuario = new Usuario(username, password);
            session.save(usuario);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            mostrarAlertaError("Error de Registro", "No se pudo registrar al usuario. Detalle: " + e.getMessage());
            return false;
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
