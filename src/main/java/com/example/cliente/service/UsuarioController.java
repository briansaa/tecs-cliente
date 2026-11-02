package com.example.cliente.service;

import com.example.cliente.model.Usuario;
import com.example.cliente.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/getAll")
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Usuario> getById(@PathVariable("id") Integer id) {
        return usuarioRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/store")
    public Usuario store(@RequestBody Usuario usuario) {
        usuario.setId(null);
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> update(@RequestBody Usuario usuario, @PathVariable("id") Integer id) {
        return usuarioRepository.findById(id).map(existente -> {
            existente.setCodigo(usuario.getCodigo());
            if (usuario.getContrasena() != null && !usuario.getContrasena().isBlank()) {
                existente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
            }
            return ResponseEntity.ok(usuarioRepository.save(existente));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        if (!usuarioRepository.existsById(id)) return ResponseEntity.notFound().build();
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Login por código de alumno. Devuelve id y código para que el frontend sepa quién reserva.
     * Acepta contraseñas antiguas en texto plano y las convierte a BCrypt en el primer login.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario credenciales) {
        Optional<Usuario> encontrado = usuarioRepository.findByCodigo(credenciales.getCodigo());
        if (encontrado.isEmpty() || credenciales.getContrasena() == null) {
            return credencialesIncorrectas();
        }

        Usuario usuario = encontrado.get();
        String guardada = usuario.getContrasena();
        boolean esHash = guardada != null && guardada.matches("^\\$2[aby]\\$.{56}$");
        boolean valida = esHash
                ? passwordEncoder.matches(credenciales.getContrasena(), guardada)
                : credenciales.getContrasena().equals(guardada);

        if (!valida) return credencialesIncorrectas();

        if (!esHash) {
            usuario.setContrasena(passwordEncoder.encode(credenciales.getContrasena()));
            usuarioRepository.save(usuario);
        }
        return ResponseEntity.ok(Map.of("id", usuario.getId(), "codigo", usuario.getCodigo()));
    }

    private ResponseEntity<Map<String, String>> credencialesIncorrectas() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("mensaje", "Credenciales incorrectas"));
    }
}
