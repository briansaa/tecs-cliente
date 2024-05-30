package com.example.cliente.service;
import com.example.cliente.model.Polideportivo;
import com.example.cliente.repository.PolideportivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/polideportivos")
@CrossOrigin(origins = "http://localhost:3000")
public class PolideportivoController {

    @Autowired
    private PolideportivoRepository polideportivoRepository;

    @GetMapping("/getAll")
    public List<Polideportivo> getAll(){
        return polideportivoRepository.findAll();}

    @PostMapping("/getById/{id}")
    public Optional<Polideportivo> getById(@PathVariable("id") Integer id){
        return polideportivoRepository.findById(id);
    }

    @PostMapping("/store")
    public Polideportivo store(@RequestBody Polideportivo polideportivo)
    {
        polideportivo.setId(0);
        return polideportivoRepository.save(polideportivo);

    }
    @PutMapping("/update/{id}")
    public Polideportivo update(@RequestBody Polideportivo polideportivo, @PathVariable ("id") Integer id)
    {
        Polideportivo polideportivoFromDB = polideportivoRepository.getById(id);
        polideportivoFromDB.setNombre(polideportivo.getNombre());

        return polideportivoRepository.save(polideportivoFromDB);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Polideportivo> delete(@PathVariable("id") Integer id)
    {
        polideportivoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
