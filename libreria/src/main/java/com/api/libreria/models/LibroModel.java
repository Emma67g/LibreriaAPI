package com.api.libreria.models;

import java.util.Locale;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "libros")
public class LibroModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long isbn;

    @NotBlank(message = "El ISBN es obligatorio")
    @Pattern(regexp = "^(\\d{9}[\\dX]|\\d{13})$", message = "El ISBN debe tener 10 o 13 dígitos (se aceptan guiones)")
    @Column(name = "codigo_isbn", unique = true, length = 13)
    private String codigoIsbn;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar 255 caracteres")
    @Column
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 255, message = "El autor no puede superar 255 caracteres")
    @Column
    private String autor;

    @Size(max = 255, message = "La editorial no puede superar 255 caracteres")
    @Column
    private String editorial;

    @NotBlank(message = "El año de publicación es obligatorio")
    @Pattern(regexp = "^\\d{4}$", message = "El año de publicación debe tener 4 dígitos")
    @Column(name = "anio_publicacion")
    private String anioPublicacion;

    @Min(value = 0, message = "El número de ejemplares no puede ser negativo")
    @Column
    private int numejemplares;

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getCodigoIsbn() {
        return codigoIsbn;
    }

    public void setCodigoIsbn(String codigoIsbn) {
        this.codigoIsbn = codigoIsbn == null ? null : codigoIsbn.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getNumejemplares() {
        return numejemplares;
    }

    public void setNumejemplares(int numejemplares) {
        this.numejemplares = numejemplares;
    }

}
