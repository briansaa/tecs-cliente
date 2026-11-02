package com.example.cliente.repository;

import com.example.cliente.model.ReservaPolideportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaPolideportivoRepository extends JpaRepository<ReservaPolideportivo, Integer> {

    @Query("SELECT COUNT(r) > 0 FROM ReservaPolideportivo r WHERE r.polideportivo_id = :espacio AND r.fecha = :fecha AND r.horario_id = :horario")
    boolean estaOcupado(@Param("espacio") Integer espacio, @Param("fecha") LocalDate fecha, @Param("horario") Integer horario);
}
