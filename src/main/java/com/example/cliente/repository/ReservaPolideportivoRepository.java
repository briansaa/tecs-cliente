package com.example.cliente.repository;
import com.example.cliente.model.ReservaPolideportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaPolideportivoRepository extends JpaRepository<ReservaPolideportivo, Integer> {
}
