package com.example.cliente.model;
import javax.persistence.*;

import lombok.Data;

import java.time.LocalDate;


@Data
@Entity
@Table(name = "adminapp_reservalaboratorio")
public class ReservaLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fecha;
    private Integer horario_id;
    private Integer laboratorio_id;
    private Integer usuario_id;

    public ReservaLaboratorio() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Integer getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(Integer horario_id) {
        this.horario_id = horario_id;
    }

    public Integer getLaboratorio_id() {
        return laboratorio_id;
    }

    public void setLaboratorio_id(Integer laboratorio_id) {
        this.laboratorio_id = laboratorio_id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }
}
