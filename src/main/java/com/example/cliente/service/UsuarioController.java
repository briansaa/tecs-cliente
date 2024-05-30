package com.example.cliente.service;

import com.example.cliente.model.Usuario;
import com.example.cliente.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "http://localhost:3000")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/getAll")
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @PostMapping("/getById/{id}")
    public Optional<Usuario> getById(@PathVariable("id") Integer id) {
        return usuarioRepository.findById(id);
    }

    @PostMapping("/store")
    public Usuario store(@RequestBody Usuario usuario)
    {
        usuario.setId(0);
        return usuarioRepository.save(usuario);
    }
    @PutMapping("/update/{id}")
    public Usuario update(@RequestBody Usuario usuario, @PathVariable ("id") Integer id)
    {

        Usuario usuarioFromDB = usuarioRepository.getById(id);

        usuarioFromDB.setCodigo(usuario.getCodigo());
        usuarioFromDB.setContrasena(usuario.getContrasena());

        return usuarioRepository.save(usuarioFromDB);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Usuario> delete(@PathVariable("id") Integer id)
    {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByCodigo(usuario.getCodigo());

        if (usuarioEncontrado.isPresent() && usuarioEncontrado.get().getContrasena().equals(usuario.getContrasena())) {
            return ResponseEntity.ok("Autenticación exitosa");
        } else {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }
    }
}