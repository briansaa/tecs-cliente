package com.example.cliente.service;

import com.example.cliente.model.Pabellon;
import com.example.cliente.repository.PabellonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pabellon")
@CrossOrigin(origins = "http://localhost:3000")
public class PabellonController {

    @Autowired
    private PabellonRepository pabellonRepository;

    @GetMapping("/getAll")
    public List<Pabellon> getAll(){
        return pabellonRepository.findAll();
    }

    @PostMapping("/getById/{id}")
    public Optional<Pabellon> getById(@PathVariable("id") Integer id){
        return pabellonRepository.findById(id);
    }

    @PostMapping("/store")
    public Pabellon store(@RequestBody Pabellon pabellon)
    {
        pabellon.setId(0);
        return pabellonRepository.save(pabellon);
    }

    @PutMapping("/update/{id}")
    public Pabellon update(@RequestBody Pabellon pabellon, @PathVariable("id") Integer id)
    {
        Pabellon pabellonFromDB = pabellonRepository.getById(id);

        pabellonFromDB.setNombre(pabellon.getNombre());

        return pabellonRepository.save(pabellonFromDB);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Pabellon> delete(@PathVariable("id") Integer id)
    {
        pabellonRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
