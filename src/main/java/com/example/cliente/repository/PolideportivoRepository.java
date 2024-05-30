package com.example.cliente.repository;

import com.example.cliente.model.Polideportivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolideportivoRepository extends JpaRepository<Polideportivo, Integer> {
}
