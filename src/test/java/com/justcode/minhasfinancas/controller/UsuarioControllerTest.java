package com.justcode.minhasfinancas.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.justcode.minhasfinancas.dto.UsuarioDTO;
import com.justcode.minhasfinancas.exceptions.ErroAutenticacao;
import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
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
public class UsuarioControllerTest {

    static final String API = "/api/usuarios";
    static final MediaType FORMATO_JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsuarioServiceImplementacao usuarioServiceImplementacao;

    @Test
    public void deveAutenticarUmUsuario() throws Exception{
        //cenário
        String email = "usario@email.com";
        String senha = "1234";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuarioAutenticado = Usuario.builder().id(1L).email(email).senha(senha).build();

        Mockito.when(usuarioServiceImplementacao.autenticar(email, senha)).thenReturn(usuarioAutenticado);

        String body = new ObjectMapper().writeValueAsString(usuarioDTO);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(FORMATO_JSON)
                .contentType(FORMATO_JSON)
                .content(body);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioAutenticado.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioAutenticado.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioAutenticado.getNome()));
    }

    @Test
    public void deveRetornarBadRequestQuandoTiverErroDeAutenticacao() throws Exception{
        //cenario
        String email = "usario@email.com";
        String senha = "1234";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
        Mockito.when(usuarioServiceImplementacao.autenticar(email, senha)).thenThrow(ErroAutenticacao.class);

        String body = new ObjectMapper().writeValueAsString(usuarioDTO);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/autenticar"))
                .accept(FORMATO_JSON)
                .contentType(FORMATO_JSON)
                .content(body);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deveCriarUmUsuario() throws Exception {
        //cenario
        String email = "usario@email.com";
        String senha = "1234";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();
        Usuario usuarioCriado = Usuario.builder().id(1L).email(email).senha(senha).build();

        Mockito.when(usuarioServiceImplementacao.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuarioCriado);

        String body = new ObjectMapper().writeValueAsString(usuarioDTO);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/criarUsuario"))
                .accept(FORMATO_JSON)
                .contentType(FORMATO_JSON)
                .content(body);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(usuarioCriado.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(usuarioCriado.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioCriado.getNome()));
    }

    @Test
    public void deveRetornarBadRequestAoTentarCriarUmUsuarioInvalido () throws Exception {
        //cenario
        String email = "usario@email.com";
        String senha = "1234";

        UsuarioDTO usuarioDTO = UsuarioDTO.builder().email(email).senha(senha).build();

        Mockito.when(usuarioServiceImplementacao.salvarUsuario(Mockito.any(Usuario.class))).thenThrow(RegraNegocioException.class);

        String body = new ObjectMapper().writeValueAsString(usuarioDTO);

        //execucao e verificacao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/criarUsuario"))
                .accept(FORMATO_JSON)
                .contentType(FORMATO_JSON)
                .content(body);

        mockMvc
                .perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());


    }
}
