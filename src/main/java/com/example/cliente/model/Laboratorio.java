package com.example.cliente.model;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "adminapp_laboratorio")
public class Laboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Integer pabellon_id;

    public Laboratorio() {
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

    public Integer getPabellon_id() {
        return pabellon_id;
    }

    public void setPabellon_id(Integer pabellon_id) {
        this.pabellon_id = pabellon_id;
    }
}
