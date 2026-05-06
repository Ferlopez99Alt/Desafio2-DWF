package bean;

import dao.AuthorModel;
import dao.LiteraryGenreModel;
import model.Author;
import model.LiteraryGenre;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ManagedBean que actua como controlador de la aplicacion.
 * Es el que conecta la vista (XHTML) con los modelos (DAO).
 * Maneja toda la logica del formulario y la tabla de autores.
 *
 * Usa SessionScoped para que los datos persistan durante la sesion del usuario.
 */
@ManagedBean(name = "authorBean")
@SessionScoped
public class AuthorBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // ==================== ATRIBUTOS DEL FORMULARIO ====================

    // Datos del autor que se esta agregando/editando
    private String authorName;
    private Date birthdate;
    private String phone;
    private int selectedGenreId;

    // Para controlar si estamos editando o agregando
    private boolean editMode = false;
    private int editingAuthorId;

    // Mensaje del area de validaciones
    private String validationMessage = "";

    // ==================== ATRIBUTOS DE LA TABLA ====================

    // Filtro por genero (para AJAX)
    private int filterGenreId = 0;

    // Texto de busqueda para filtrar la tabla
    private String searchText = "";

    // Contador de autores visibles
    private long authorCount = 0;

    // ==================== MODELOS (DAO) ====================

    private AuthorModel authorModel;
    private LiteraryGenreModel genreModel;

    // Listas de datos
    private List<Author> authorList;
    private List<LiteraryGenre> genreList;

    // ==================== CONSTRUCTOR ====================

    public AuthorBean() {
    try {
        authorModel = new AuthorModel();
        genreModel = new LiteraryGenreModel();
        loadData();
        System.out.println("=== BEAN INICIADO OK ===");
        System.out.println("=== Generos cargados: " + (genreList != null ? genreList.size() : "NULL") + " ===");
        System.out.println("=== Autores cargados: " + (authorList != null ? authorList.size() : "NULL") + " ===");
    } catch (Exception e) {
        System.out.println("=== ERROR EN BEAN: " + e.getMessage() + " ===");
        e.printStackTrace();
    }
}

    /**
     * Carga inicial de datos desde la base de datos
     */
    private void loadData() {
        genreList = genreModel.getAllGenres();
        authorList = authorModel.getAllAuthors();
        authorCount = authorList.size();
    }

    // ==================== METODOS DE ACCION ====================

    /**
     * Agrega un nuevo autor o actualiza uno existente
     * Valida que los campos obligatorios no esten vacios
     * y avisa si el autor ya fue registrado anteriormente
     */
    public void saveAuthor() {
        validationMessage = "";

        // Validacion basica de campos
        if (authorName == null || authorName.trim().isEmpty()) {
            validationMessage = "ERROR: El nombre del autor es obligatorio.";
            return;
        }

        if (birthdate == null) {
            validationMessage = "ERROR: La fecha de nacimiento es obligatoria.";
            return;
        }

        if (phone == null || phone.trim().isEmpty()) {
            validationMessage = "ERROR: El telefono es obligatorio.";
            return;
        }

        if (selectedGenreId == 0) {
            validationMessage = "ERROR: Debe seleccionar un genero literario.";
            return;
        }

        // Buscar el objeto genero por el ID seleccionado
        LiteraryGenre genre = genreModel.getGenreById(selectedGenreId);
        if (genre == null) {
            validationMessage = "ERROR: El genero seleccionado no es valido.";
            return;
        }

        if (editMode) {
            // -- MODO EDICION --
            Author author = authorModel.getAuthorById(editingAuthorId);
            if (author != null) {
                author.setName(authorName.trim());
                author.setBirthdate(birthdate);
                author.setPhone(phone.trim());
                author.setGenre(genre);

                boolean updated = authorModel.updateAuthor(author);
                if (updated) {
                    validationMessage = "OK: El autor fue actualizado correctamente.";
                } else {
                    validationMessage = "ERROR: No se pudo actualizar el autor. Intente de nuevo.";
                }
            }
            editMode = false;
            editingAuthorId = 0;

        } else {
            // -- MODO AGREGAR NUEVO --
            // Verificar si ya existe (pero se puede agregar igual segun el requerimiento)
            boolean alreadyExists = authorModel.authorExists(authorName.trim());

            Author newAuthor = new Author();
            newAuthor.setName(authorName.trim());
            newAuthor.setBirthdate(birthdate);
            newAuthor.setPhone(phone.trim());
            newAuthor.setGenre(genre);

            boolean added = authorModel.addAuthor(newAuthor);
            if (added) {
                if (alreadyExists) {
                    // Avisa que ya existia pero lo agrega igual
                    validationMessage = "AVISO: El autor ya habia sido agregado anteriormente, " +
                                        "pero se registro nuevamente.";
                } else {
                    validationMessage = "OK: El autor fue agregado correctamente al directorio.";
                }
            } else {
                validationMessage = "ERROR: No se pudo agregar el autor. Revise los datos.";
            }
        }

        // Recargar la lista y limpiar el formulario
        reloadAuthorList();
        clearForm();
    }

    /**
     * Carga los datos de un autor al formulario para editarlo
     * @param author El autor que se va a editar
     */
    public void editAuthor(Author author) {
        validationMessage = "";
        editMode = true;
        editingAuthorId = author.getId();
        authorName = author.getName();
        birthdate = author.getBirthdate();
        phone = author.getPhone();
        selectedGenreId = author.getGenre().getId();
        validationMessage = "EDITANDO: Se cargaron los datos del autor. Modifique y haga click en AGREGAR.";
    }

    /**
     * Elimina un autor de la base de datos
     * @param authorId El ID del autor a borrar
     */
    public void deleteAuthor(int authorId) {
        validationMessage = "";
        boolean deleted = authorModel.deleteAuthor(authorId);
        if (deleted) {
            validationMessage = "OK: El autor fue eliminado del directorio.";
        } else {
            validationMessage = "ERROR: No se pudo eliminar el autor.";
        }
        reloadAuthorList();
    }

    /**
     * Filtra la tabla por genero usando AJAX
     * Se llama cuando el usuario cambia el select de genero en la tabla
     */
    public void filterByGenre() {
        if (filterGenreId == 0) {
            authorList = authorModel.getAllAuthors();
        } else {
            authorList = authorModel.getAuthorsByGenre(filterGenreId);
        }
        authorCount = authorList.size();
    }

    /**
     * Cuenta y muestra el total de autores visibles
     * Metodo llamado por el boton CONTAR via AJAX
     */
    public void countAuthors() {
        authorCount = authorList.size();
    }

    /**
     * Filtra la lista de autores segun el texto de busqueda
     * Se actualiza de forma sincrona con h:inputText
     */
    public List<Author> getFilteredAuthors() {
        if (searchText == null || searchText.trim().isEmpty()) {
            return authorList;
        }

        String search = searchText.toLowerCase().trim();
        List<Author> filtered = new ArrayList<>();

        for (Author a : authorList) {
            // Filtra por nombre o por nombre del genero
            if (a.getName().toLowerCase().contains(search) ||
                a.getGenre().getName().toLowerCase().contains(search) ||
                a.getPhone().contains(search)) {
                filtered.add(a);
            }
        }
        return filtered;
    }

    // ==================== METODOS AUXILIARES ====================

    /**
     * Limpia todos los campos del formulario
     */
    public void clearForm() {
        authorName = null;
        birthdate = null;
        phone = null;
        selectedGenreId = 0;
        editMode = false;
        editingAuthorId = 0;
    }

    /**
     * Recarga la lista de autores desde la base de datos
     */
    private void reloadAuthorList() {
        if (filterGenreId > 0) {
            authorList = authorModel.getAuthorsByGenre(filterGenreId);
        } else {
            authorList = authorModel.getAllAuthors();
        }
        authorCount = authorList.size();
    }

    // ==================== GETTERS Y SETTERS ====================

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public Date getBirthdate() { return birthdate; }
    public void setBirthdate(Date birthdate) { this.birthdate = birthdate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getSelectedGenreId() { return selectedGenreId; }
    public void setSelectedGenreId(int selectedGenreId) { this.selectedGenreId = selectedGenreId; }

    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }

    public String getValidationMessage() { return validationMessage; }
    public void setValidationMessage(String validationMessage) { this.validationMessage = validationMessage; }

    public int getFilterGenreId() { return filterGenreId; }
    public void setFilterGenreId(int filterGenreId) { this.filterGenreId = filterGenreId; }

    public String getSearchText() { return searchText; }
    public void setSearchText(String searchText) { this.searchText = searchText; }

    public long getAuthorCount() { return authorCount; }
    public void setAuthorCount(long authorCount) { this.authorCount = authorCount; }

    public List<Author> getAuthorList() { return authorList; }
    public void setAuthorList(List<Author> authorList) { this.authorList = authorList; }

    public List<LiteraryGenre> getGenreList() { return genreList; }
    public void setGenreList(List<LiteraryGenre> genreList) { this.genreList = genreList; }

    public int getEditingAuthorId() { return editingAuthorId; }
    public void setEditingAuthorId(int editingAuthorId) { this.editingAuthorId = editingAuthorId; }
}
