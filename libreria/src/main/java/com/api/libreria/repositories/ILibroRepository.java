package com.api.libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.api.libreria.models.LibroModel;

public interface ILibroRepository extends JpaRepository<LibroModel, Long> {

    @Modifying
    @Query("UPDATE LibroModel l SET l.numejemplares = l.numejemplares - 1 WHERE l.isbn = :isbn AND l.numejemplares > 0")
    int descontarEjemplar(@Param("isbn") Long isbn);

    @Modifying
    @Query("UPDATE LibroModel l SET l.numejemplares = l.numejemplares + 1 WHERE l.isbn = :isbn")
    int reponerEjemplar(@Param("isbn") Long isbn);

}
