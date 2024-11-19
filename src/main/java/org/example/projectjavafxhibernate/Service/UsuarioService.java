package org.example.projectjavafxhibernate.Service;

import org.example.projectjavafxhibernate.Model.Usuario;
import org.example.projectjavafxhibernate.Util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UsuarioService {

    public boolean validarUsuario(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.createQuery("FROM Usuario WHERE username = :username AND password = :password", Usuario.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();
            return usuario != null;
        }
    }

    public boolean registrarUsuario(String username, String password) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (session.createQuery("FROM Usuario WHERE username = :username", Usuario.class)
                    .setParameter("username", username)
                    .uniqueResult() != null) {
                return false; // Usuario ya existe
            }

            transaction = session.beginTransaction();
            Usuario usuario = new Usuario(username, password);
            session.save(usuario);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return false;
        }
    }
}
