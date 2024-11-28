package org.example.projectjavafxhibernate.Util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class DatabaseUtil {

    public static boolean verificarConexion(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            System.err.println("Error al verificar la conexión: " + e.getMessage());
            return false;
        }
    }
}
