package com.example.cliente.service;

import com.example.cliente.model.ReservaPolideportivo;
import com.example.cliente.repository.ReservaPolideportivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/reservapolideportivo")
public class ReservaPolideportivoController {

    @Autowired
    private ReservaPolideportivoRepository reservaPolideportivoRepository;

    @GetMapping("/getAll")
    public List<ReservaPolideportivo> getAll(){
        return reservaPolideportivoRepository.findAll();}

    @PostMapping("/getById/{id}")
    public ResponseEntity<ReservaPolideportivo> getReservaById(@PathVariable("id") Integer id){
        Optional<ReservaPolideportivo> reserva = reservaPolideportivoRepository.findById(id);
        return reserva.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody ReservaPolideportivo reservaPolideportivo) {
        if (reservaPolideportivo.getFecha() == null || reservaPolideportivo.getHorario_id() == null
                || reservaPolideportivo.getPolideportivo_id() == null || reservaPolideportivo.getUsuario_id() == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "Completa fecha, polideportivo, horario y usuario."));
        }
        if (reservaPolideportivo.getFecha().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No se puede reservar en una fecha pasada."));
        }
        if (reservaPolideportivoRepository.estaOcupado(reservaPolideportivo.getPolideportivo_id(), reservaPolideportivo.getFecha(), reservaPolideportivo.getHorario_id())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("mensaje", "Ese polideportivo ya está reservado en la fecha y horario elegidos."));
        }
        reservaPolideportivo.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaPolideportivoRepository.save(reservaPolideportivo));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReservaPolideportivo> updateReserva(
            @RequestBody ReservaPolideportivo reservaPolideportivo,
            @PathVariable("id") Integer id){
        if (!reservaPolideportivoRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }

        ReservaPolideportivo existingReserva = reservaPolideportivoRepository.getById(id);
        existingReserva.setFecha(reservaPolideportivo.getFecha());
        existingReserva.setHorario_id(reservaPolideportivo.getHorario_id());
        existingReserva.setPolideportivo_id(reservaPolideportivo.getPolideportivo_id());
        existingReserva.setUsuario_id(reservaPolideportivo.getUsuario_id());

        ReservaPolideportivo updateReserva = reservaPolideportivoRepository.save(existingReserva);
        return ResponseEntity.ok(updateReserva);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable("id") Integer id){
        if (!reservaPolideportivoRepository.existsById(id)){
            return ResponseEntity.notFound().build();
    }

        reservaPolideportivoRepository.deleteById(id);
        return ResponseEntity.ok().build();
}
}
