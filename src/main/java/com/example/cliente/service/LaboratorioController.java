package com.example.cliente.service;
import com.example.cliente.model.Laboratorio;
import com.example.cliente.repository.LaboratorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/laboratorios")
@CrossOrigin(origins = "http://localhost:3000")
public class LaboratorioController {

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    @GetMapping("/getAll")
    public List<Laboratorio> getAll() {
        return laboratorioRepository.findAll();}

    @PostMapping("/getById/{id}")
    public Optional<Laboratorio> getById(@PathVariable("id") Integer id) {
        return laboratorioRepository.findById(id);}

    @PostMapping("/store")
    public Laboratorio store(@RequestBody Laboratorio laboratorio)
    {
        laboratorio.setId(0);
        return laboratorioRepository.save(laboratorio);
    }

    @PutMapping("/update/{id}")
    public Laboratorio update(@RequestBody Laboratorio laboratorio, @PathVariable("id") Integer id)
    {

        Laboratorio laboratorioFromDB = laboratorioRepository.getById(id);

        laboratorioFromDB.setNombre(laboratorio.getNombre());
        laboratorioFromDB.setPabellon_id(laboratorio.getPabellon_id());

        return laboratorioRepository.save(laboratorioFromDB);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Laboratorio> delete(@PathVariable("id") Integer id)
    {
        laboratorioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
