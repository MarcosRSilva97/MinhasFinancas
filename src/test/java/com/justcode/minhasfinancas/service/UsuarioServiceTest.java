package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.exceptions.ErroAutenticacao;
import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Usuario;
import com.justcode.minhasfinancas.model.repository.UsuarioRepository;
import com.justcode.minhasfinancas.service.implementacao.UsuarioServiceImplementacao;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImplementacao usuarioServiceImplementacao;
    @MockBean
    UsuarioRepository usuarioRepository;


     @Test (expected = Test.None.class)
    public void deveValidarEmail(){

        //cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false);

        //acao
         usuarioServiceImplementacao.validarEmail("email@email.com");
    }

    @Test (expected = RegraNegocioException.class)
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado(){
        //cenario
        Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

        //acao
        usuarioServiceImplementacao.validarEmail("email@email.com");
    }

    @Test (expected = Test.None.class)
    public void deveAutenticarUmUsuarioComSucesso(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //acao
        Usuario resultado = usuarioServiceImplementacao.autenticar(email, senha);

        //verificacao
        Assertions.assertThat(resultado).isNotNull();
    }

    @Test
    public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado(){
        //cenario
        Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        //acao
        Throwable exception = Assertions.catchThrowable(() ->usuarioServiceImplementacao.autenticar("email@email.com", "senha"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o email informado.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoForACerta(){
        //cenário
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        //acao
        Throwable exception = Assertions.catchThrowable(() -> usuarioServiceImplementacao.autenticar(email, "12345"));
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Email ou senha incorretos.");

    }

    @Test(expected = Test.None.class)
    public void deveSalvarUsuario (){
         //Cenário
        Mockito.doNothing().when(usuarioServiceImplementacao).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@email.com").senha("senha").build();

        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        //Acao
        Usuario usuarioSalvo = usuarioServiceImplementacao.salvarUsuario(new Usuario());

        //Verificacao
        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
    }

    @Test (expected = RegraNegocioException.class)
    public void naoDeveSalvarUsuarioComEmailJaCadastrado(){
        //Cenário
        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(usuarioServiceImplementacao).validarEmail(email);

        //Acao
        usuarioServiceImplementacao.salvarUsuario(usuario);

        //Verificacao
        Mockito.verify(usuarioRepository, Mockito.never()).save(usuario);
    }
}
