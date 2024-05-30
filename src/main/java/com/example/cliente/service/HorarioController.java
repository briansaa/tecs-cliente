package com.example.cliente.service;

import com.example.cliente.model.Horario;
import com.example.cliente.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/horarios")
@CrossOrigin(origins = "http://localhost:3000")
public class HorarioController {

    @Autowired
    private HorarioRepository horarioRepository;

    @GetMapping("/getAll")
    public List<Horario> getAll(){
        return horarioRepository.findAll();}

    @PostMapping("/getById/{id}")
    public Optional<Horario> getById(@PathVariable("id") Integer id){
        return horarioRepository.findById(id);}

    @PostMapping("/store")
    public Horario horario(@RequestBody Horario horario)
    {
        horario.setId(0);
        return horarioRepository.save(horario);
    }

    @PutMapping("/update/{id}")
    public Horario update(@RequestBody Horario horario, @PathVariable("id") Integer id)
    {
        Horario horarioFromDB = horarioRepository.getById(id);

        horarioFromDB.setHora_inicio(horario.getHora_inicio());
        horarioFromDB.setHora_fin(horario.getHora_fin());

        return horarioRepository.save(horarioFromDB);

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Horario> delete(@PathVariable("id") Integer id)
    {
        horarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
