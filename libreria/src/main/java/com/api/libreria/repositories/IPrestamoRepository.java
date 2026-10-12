package com.api.libreria.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.libreria.models.PrestamoModel;

public interface IPrestamoRepository extends JpaRepository<PrestamoModel, Long> {

    long countBySocio_IdAndFechaDevolucionIsNull(Long socioId);

    boolean existsByLibro_Isbn(Long isbn);

    boolean existsBySocio_Id(Long socioId);

}