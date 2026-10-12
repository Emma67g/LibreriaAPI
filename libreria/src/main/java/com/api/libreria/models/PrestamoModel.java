package com.api.libreria.models;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "prestamos")
public class PrestamoModel {

    private static final int DIAS_PRESTAMO = 7;
    private static final double MULTA_POR_DIA = 5.0;
    public static final LocalTime FIN_DEL_DIA = LocalTime.of(23, 59, 59);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "libro_isbn", nullable = false)
    private LibroModel libro;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "socio_id", nullable = false)
    private UsuarioModel socio;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPrestamo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaLimite;

    @Column
    private LocalDateTime fechaDevolucion;

    @PrePersist
    private void asignarFechas() {
        if (fechaPrestamo == null) {
            fechaPrestamo = LocalDateTime.now();
        }
        if (fechaLimite == null) {
            fechaLimite = fechaPrestamo.toLocalDate().plusDays(DIAS_PRESTAMO).atTime(FIN_DEL_DIA);
        }
    }

    @Transient
    public boolean isActivo() {
        return fechaDevolucion == null;
    }

    @Transient
    public boolean isVencido() {
        return fechaDevolucion == null && LocalDateTime.now().isAfter(fechaLimite);
    }

    @Transient
    public long getDiasRetraso() {
        LocalDateTime referencia = fechaDevolucion != null ? fechaDevolucion : LocalDateTime.now();
        if (!referencia.isAfter(fechaLimite)) {
            return 0;
        }
        return Math.max(1, ChronoUnit.DAYS.between(fechaLimite.toLocalDate(), referencia.toLocalDate()));
    }

    @Transient
    public double getMulta() {
        return getDiasRetraso() * MULTA_POR_DIA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LibroModel getLibro() {
        return libro;
    }

    public void setLibro(LibroModel libro) {
        this.libro = libro;
    }

    public UsuarioModel getSocio() {
        return socio;
    }

    public void setSocio(UsuarioModel socio) {
        this.socio = socio;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDateTime fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }
}