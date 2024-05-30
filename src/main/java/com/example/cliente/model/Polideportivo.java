package com.example.cliente.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "adminapp_polideportivo")
public class Polideportivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;

    public Polideportivo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
