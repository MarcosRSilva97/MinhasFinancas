package com.justcode.minhasfinancas.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.justcode.minhasfinancas.dto.UsuarioDTO;
import com.justcode.minhasfinancas.model.entity.Usuario;
import com.justcode.minhasfinancas.service.implementacao.LancamentoServiceImplementacao;
import com.justcode.minhasfinancas.service.implementacao.UsuarioServiceImplementacao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioController {

    static final String API = "/api/usuarios";
    static final MediaType FORMATO_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    UsuarioServiceImplementacao usuarioServiceImplementacao;

    @MockBean
    LancamentoServiceImplementacao lancamentoServiceImplementacao;

    @Test
    public void deveAutenticarUmUsuario() throws Exception{
        //cenario
        String email = "usuario@email.com";
        String senha = "1234";
        String nome = "teste";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuario = Usuario.builder().id(1L).email(email).senha(senha).build();
        Mockito.when(usuarioServiceImplementacao.autenticar(email, senha)).thenReturn(usuario);
        String requisicao = new ObjectMapper().writeValueAsString(usuarioDTO);

        //verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(FORMATO_JSON)
                .contentType(FORMATO_JSON)
                .content(requisicao);

        //execucao
            mvc
                    .perform(request)
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuario.getId()))
                    .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuario.getNome()))
                    .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuario.getEmail()));
    }
}
