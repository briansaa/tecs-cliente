package com.example.cliente;

import com.example.cliente.model.Usuario;
import com.example.cliente.repository.ReservaLaboratorioRepository;
import com.example.cliente.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired ReservaLaboratorioRepository reservaLaboratorioRepository;
    @Autowired PasswordEncoder passwordEncoder;

    private Integer usuarioId;

    @BeforeEach
    void setUp() {
        reservaLaboratorioRepository.deleteAll();
        usuarioRepository.deleteAll();
        Usuario u = new Usuario();
        u.setCodigo("U001");
        u.setContrasena(passwordEncoder.encode("demo1234"));
        usuarioId = usuarioRepository.save(u).getId();
    }

    @Test
    void loginCorrectoDevuelveUsuario() throws Exception {
        mvc.perform(post("/clientes/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"U001\",\"contrasena\":\"demo1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("U001"))
                .andExpect(jsonPath("$.id").value(usuarioId));
    }

    @Test
    void loginIncorrectoDevuelve401() throws Exception {
        mvc.perform(post("/clientes/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"U001\",\"contrasena\":\"mala\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void contrasenaAntiguaEnTextoPlanoSeMigraABcrypt() throws Exception {
        Usuario viejo = new Usuario();
        viejo.setCodigo("U002");
        viejo.setContrasena("plano123");
        usuarioRepository.save(viejo);

        mvc.perform(post("/clientes/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"codigo\":\"U002\",\"contrasena\":\"plano123\"}"))
                .andExpect(status().isOk());

        assertThat(usuarioRepository.findByCodigo("U002").orElseThrow().getContrasena()).startsWith("$2a$");
    }

    @Test
    void listadoNoExponeContrasenas() throws Exception {
        mvc.perform(get("/clientes/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contrasena").doesNotExist());
    }

    @Test
    void reservaDuplicadaDevuelve409() throws Exception {
        String body = String.format("{\"fecha\":\"%s\",\"laboratorio_id\":1,\"horario_id\":1,\"usuario_id\":%d}",
                LocalDate.now().plusDays(1), usuarioId);

        mvc.perform(post("/reservalaboratorio/store").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated());
        mvc.perform(post("/reservalaboratorio/store").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void reservaEnFechaPasadaDevuelve400() throws Exception {
        String body = String.format("{\"fecha\":\"%s\",\"laboratorio_id\":1,\"horario_id\":1,\"usuario_id\":%d}",
                LocalDate.now().minusDays(1), usuarioId);
        mvc.perform(post("/reservalaboratorio/store").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }
}
