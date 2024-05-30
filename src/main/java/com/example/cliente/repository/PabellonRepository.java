package com.example.cliente.repository;

import com.example.cliente.model.Pabellon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PabellonRepository extends JpaRepository<Pabellon, Integer> {
}
