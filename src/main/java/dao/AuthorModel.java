package dao;

import model.Author;
import model.LiteraryGenre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Modelo (DAO) para manejar todas las operaciones
 * de la tabla author en la base de datos.
 * Aqui se implementa todo el CRUD de autores.
 */
public class AuthorModel {

    private SessionFactory sessionFactory;

    // Constructor que configura la conexion a Hibernate
    public AuthorModel() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Author.class)
                    .addAnnotatedClass(LiteraryGenre.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            System.out.println("Error al iniciar Hibernate (AuthorModel): " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Trae todos los autores de la base de datos
     * @return Lista con todos los autores
     */
    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            // JOIN FETCH para cargar el genero junto con el autor (evita lazy loading)
            authors = session.createQuery(
                "FROM Author a JOIN FETCH a.genre ORDER BY a.id", Author.class
            ).list();
        } catch (Exception e) {
            System.out.println("Error al obtener autores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return authors;
    }

    /**
     * Busca autores segun el genero literario
     * Se usa para el filtro AJAX de la tabla
     * @param genreId ID del genero a filtrar, 0 = todos
     * @return Lista de autores filtrada
     */
    public List<Author> getAuthorsByGenre(int genreId) {
        List<Author> authors = new ArrayList<>();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            if (genreId == 0) {
                // Si no hay filtro, traer todos
                authors = getAllAuthors();
            } else {
                authors = session.createQuery(
                    "FROM Author a JOIN FETCH a.genre WHERE a.genre.id = :genreId ORDER BY a.id",
                    Author.class
                ).setParameter("genreId", genreId).list();
            }
        } catch (Exception e) {
            System.out.println("Error al filtrar autores: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return authors;
    }

    /**
     * Busca un autor por su ID
     * @param id El id del autor
     * @return El autor encontrado o null
     */
    public Author getAuthorById(int id) {
        Author author = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            author = session.get(Author.class, id);
        } catch (Exception e) {
            System.out.println("Error al buscar autor por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return author;
    }

    /**
     * Agrega un nuevo autor a la base de datos
     * @param author El autor a guardar
     * @return true si se guardo bien, false si hubo error
     */
    public boolean addAuthor(Author author) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(author);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error al agregar autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Actualiza los datos de un autor existente
     * @param author El autor con los datos actualizados
     * @return true si se actualizo bien, false si hubo error
     */
    public boolean updateAuthor(Author author) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.update(author);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error al actualizar autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Elimina un autor de la base de datos por su ID
     * @param id El id del autor a borrar
     * @return true si se elimino bien, false si hubo error
     */
    public boolean deleteAuthor(int id) {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author != null) {
                session.delete(author);
                tx.commit();
                return true;
            } else {
                // Si no se encuentra el autor no hay nada que borrar
                tx.rollback();
                return false;
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.out.println("Error al eliminar autor: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Verifica si ya existe un autor con el mismo nombre
     * Segun el requerimiento, debe indicar si ya fue agregado pero no impedirlo
     * @param name El nombre a verificar
     * @return true si ya existe, false si es nuevo
     */
    public boolean authorExists(String name) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Long count = session.createQuery(
                "SELECT COUNT(a) FROM Author a WHERE LOWER(a.name) = LOWER(:name)", Long.class
            ).setParameter("name", name).uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            System.out.println("Error al verificar autor: " + e.getMessage());
            return false;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Cuenta el total de autores visibles (segun el filtro actual)
     * @return Total de autores
     */
    public long countAuthors() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return session.createQuery("SELECT COUNT(a) FROM Author a", Long.class).uniqueResult();
        } catch (Exception e) {
            System.out.println("Error al contar autores: " + e.getMessage());
            return 0;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void closeFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
