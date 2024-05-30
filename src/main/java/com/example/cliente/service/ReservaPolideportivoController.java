package com.example.cliente.service;

import com.example.cliente.model.ReservaPolideportivo;
import com.example.cliente.repository.ReservaPolideportivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/reservapolideportivo")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ReservaPolideportivo store(@RequestBody ReservaPolideportivo reservaPolideportivo)
    {
        reservaPolideportivo.setId(0);
        return reservaPolideportivoRepository.save(reservaPolideportivo);
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