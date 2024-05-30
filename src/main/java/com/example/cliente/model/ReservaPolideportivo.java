package com.example.cliente.model;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.Id;

@Data
@Entity
@Table(name = "adminapp_reservapolideportivo")
public class ReservaPolideportivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fecha;
    private Integer horario_id;
    private Integer polideportivo_id;
    private Integer usuario_id;


    public ReservaPolideportivo() {
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

    public Integer getPolideportivo_id() {
        return polideportivo_id;
    }

    public void setPolideportivo_id(Integer polideportivo_id) {
        this.polideportivo_id = polideportivo_id;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }
}
