package com.justcode.minhasfinancas.controller;


import com.justcode.minhasfinancas.dto.UsuarioDTO;
import com.justcode.minhasfinancas.service.implementacao.LancamentoServiceImplementacao;
import com.justcode.minhasfinancas.service.implementacao.UsuarioServiceImplementacao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioController {

    static final String API = "/api/usuarios";
    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioServiceImplementacao usuarioServiceImplementacao;

    @MockBean
    LancamentoServiceImplementacao lancamentoServiceImplementacao;

    @Test
    public void deveAutenticarUmUsuario(){
        //cenario
        String email = "usuario@email.com";
        String senha = "1234";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
    }
}
