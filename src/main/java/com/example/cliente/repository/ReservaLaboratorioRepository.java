package com.example.cliente.repository;

import com.example.cliente.model.ReservaLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaLaboratorioRepository extends JpaRepository<ReservaLaboratorio, Integer> {

    @Query("SELECT COUNT(r) > 0 FROM ReservaLaboratorio r WHERE r.laboratorio_id = :espacio AND r.fecha = :fecha AND r.horario_id = :horario")
    boolean estaOcupado(@Param("espacio") Integer espacio, @Param("fecha") LocalDate fecha, @Param("horario") Integer horario);
}
