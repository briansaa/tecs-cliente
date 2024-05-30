package com.example.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.cliente.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCodigo(String codigo);
}