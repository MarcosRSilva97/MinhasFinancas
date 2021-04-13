package com.justcode.minhasfinancas.model.repository;

import com.justcode.minhasfinancas.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void deveVerificarAExistenciaDeUmEmail(){
        //cenario
        Usuario usuario = criaUsuario();
        testEntityManager.persist(usuario);
        //acao/execucao
        boolean resultado = usuarioRepository.existsByEmail("teste@teste.com");
        //verificacao
        Assertions.assertThat(resultado).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail (){
        //acao
        boolean resultado = usuarioRepository.existsByEmail("usuario@email.com");
        //verificacao
        Assertions.assertThat(resultado).isFalse();
    }

    @Test
    public void devePersistirUsuarioNaBaseDeDados(){
        //cenario
        Usuario usuario = criaUsuario();

        //acao
        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        //verificacao
        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();

    }

    @Test
    public void deveBuscarUmUsuarioPorEmail(){
        //cenario
        Usuario usuario = criaUsuario();

        //acao
        testEntityManager.persist(usuario);

        //verificacao
        Optional <Usuario> resultado = usuarioRepository.findByEmail("teste@teste.com");
        Assertions.assertThat(resultado.isPresent()).isTrue();

    }

    @Test
    public void deveRetornarVazioAoBuscarUmUsuarioPorEmailQuandoNaoExistirNaBase(){

        //verificacao
        Optional <Usuario> resultado = usuarioRepository.findByEmail("teste@teste.com");
        Assertions.assertThat(resultado.isPresent()).isFalse();

    }

    public static Usuario criaUsuario(){
        return Usuario
                .builder()
                .nome("usuario")
                .email("teste@teste.com")
                .senha("1234")
                .build();
    }
}