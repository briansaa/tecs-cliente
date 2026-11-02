package com.example.cliente.service;
import com.example.cliente.model.ReservaLaboratorio;
import com.example.cliente.repository.ReservaLaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reservalaboratorio")
public class ReservaLaboratorioController {

    @Autowired
    private ReservaLaboratorioRepository reservaLaboratorioRepository;

    @GetMapping("/getAll")
    public List<ReservaLaboratorio> getAllReservas() {
        return reservaLaboratorioRepository.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ReservaLaboratorio> getReservaById(@PathVariable("id") Integer id) {
        Optional<ReservaLaboratorio> reserva = reservaLaboratorioRepository.findById(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody ReservaLaboratorio reservaLaboratorio) {
        if (reservaLaboratorio.getFecha() == null || reservaLaboratorio.getHorario_id() == null
                || reservaLaboratorio.getLaboratorio_id() == null || reservaLaboratorio.getUsuario_id() == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Completa fecha, laboratorio, horario y usuario."));
        }
        if (reservaLaboratorio.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No se puede reservar en una fecha pasada."));
        }
        if (reservaLaboratorioRepository.estaOcupado(reservaLaboratorio.getLaboratorio_id(), reservaLaboratorio.getFecha(), reservaLaboratorio.getHorario_id())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "Ese laboratorio ya está reservado en la fecha y horario elegidos."));
        }
        reservaLaboratorio.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaLaboratorioRepository.save(reservaLaboratorio));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReservaLaboratorio> updateReserva(
            @RequestBody ReservaLaboratorio reservaLaboratorio,
            @PathVariable("id") Integer id) {
        if (!reservaLaboratorioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        ReservaLaboratorio existingReserva = reservaLaboratorioRepository.getById(id);
        existingReserva.setFecha(reservaLaboratorio.getFecha());
        existingReserva.setHorario_id(reservaLaboratorio.getHorario_id());
        existingReserva.setLaboratorio_id(reservaLaboratorio.getLaboratorio_id());
        existingReserva.setUsuario_id(reservaLaboratorio.getUsuario_id());

        ReservaLaboratorio updatedReserva = reservaLaboratorioRepository.save(existingReserva);
        return ResponseEntity.ok(updatedReserva);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable("id") Integer id) {
        if (!reservaLaboratorioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        reservaLaboratorioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
