package com.example.cliente.repository;
import com.example.cliente.model.ReservaLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaLaboratorioRepository extends JpaRepository<ReservaLaboratorio, Integer> {
}
