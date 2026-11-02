package com.example.cliente.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "adminapp_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String codigo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // nunca se devuelve en las respuestas
    private String contrasena;

    public Usuario() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
