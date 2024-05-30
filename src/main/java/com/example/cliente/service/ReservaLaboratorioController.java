package com.example.cliente.service;
import com.example.cliente.model.ReservaLaboratorio;
import com.example.cliente.repository.ReservaLaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservalaboratorio")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ReservaLaboratorio store(@RequestBody ReservaLaboratorio reservaLaboratorio)
    {
        reservaLaboratorio.setId(0);
        return reservaLaboratorioRepository.save(reservaLaboratorio);
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
