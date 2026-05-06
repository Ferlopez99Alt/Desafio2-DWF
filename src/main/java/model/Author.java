package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * Clase POJO para el Autor
 * Representa la tabla author en la base de datos
 * Usa JPA/Hibernate para la persistencia
 */
@Entity
@Table(name = "author")
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "birthdate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthdate;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    // Relacion ManyToOne con LiteraryGenre
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private LiteraryGenre genre;

    // Constructor vacio requerido por JPA
    public Author() {
    }

    // Constructor con todos los campos
    public Author(String name, Date birthdate, String phone, LiteraryGenre genre) {
        this.name = name;
        this.birthdate = birthdate;
        this.phone = phone;
        this.genre = genre;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LiteraryGenre getGenre() {
        return genre;
    }

    public void setGenre(LiteraryGenre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Author{id=" + id + ", name=" + name + ", phone=" + phone + "}";
    }
}
