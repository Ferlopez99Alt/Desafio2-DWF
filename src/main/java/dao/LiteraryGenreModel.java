package dao;

import model.LiteraryGenre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo (DAO) para manejar las operaciones de la tabla literarygenre
 * Aqui van todos los metodos que interactuan con la base de datos
 * para la entidad LiteraryGenre
 */
public class LiteraryGenreModel {

    private SessionFactory sessionFactory;

    // Constructor que inicializa la sesion de Hibernate
    public LiteraryGenreModel() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(LiteraryGenre.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Error al iniciar Hibernate (LiteraryGenreModel): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los generos literarios de la base de datos
     * @return Lista de generos literarios
     */
    public List<LiteraryGenre> getAllGenres() {
        List<LiteraryGenre> genres = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            // Usamos HQL para traer todos los generos
            genres = session.createQuery("FROM LiteraryGenre ORDER BY name", LiteraryGenre.class).list();
        } catch (Exception e) {
            System.out.println("Error al obtener los generos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return genres;
    }

    /**
     * Busca un genero por su ID
     * @param id El id del genero a buscar
     * @return El genero encontrado o null si no existe
     */
    public LiteraryGenre getGenreById(int id) {
        LiteraryGenre genre = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            genre = session.get(LiteraryGenre.class, id);
        } catch (Exception e) {
            System.out.println("Error al buscar el genero: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return genre;
    }

    // Metodo para cerrar la sesion cuando ya no se necesita
    public void closeFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
