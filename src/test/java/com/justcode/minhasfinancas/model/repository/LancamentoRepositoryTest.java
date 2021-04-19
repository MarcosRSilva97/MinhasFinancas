package com.justcode.minhasfinancas.model.repository;


import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;
import com.justcode.minhasfinancas.model.enums.TipoLancamento;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;


@RunWith (SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles ("test")
public class LancamentoRepositoryTest {

    @Autowired
    LancamentoRepository lancamentoRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void deveSalvarUmLancamento(){
        Lancamento lancamento = criarLancamento();
        lancamento = lancamentoRepository.save(lancamento);

        assertThat(lancamento.getId()).isNotNull();
    }

    @Test
    public void deveDeletarUmLancamento(){

        Lancamento lancamento = criarEPersistirLancamento();
        lancamento = entityManager.find(Lancamento.class, lancamento.getId());

        lancamentoRepository.delete(lancamento);

        Lancamento lancamentoInexistente = entityManager.find(Lancamento.class, lancamento.getId());

        assertThat(lancamentoInexistente).isNull();
    }

    @Test
    public void deveAtualizarUmLancamento(){
        Lancamento lancamento = criarEPersistirLancamento();
        lancamento.setAno(2021);
        lancamento.setDescricao("Teste atualizar");
        lancamento.setStatus(StatusLancamento.CANCELADO);

        lancamentoRepository.save(lancamento);

        Lancamento lancamentoAtualizado = entityManager.find(Lancamento.class, lancamento.getId());

        assertThat(lancamentoAtualizado.getAno()).isEqualTo(2021);
        assertThat(lancamento.getDescricao()).isEqualTo("Teste atualizar");
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.CANCELADO);
    }

    @Test
    public void deveBuscarUmLancamentoPorId(){
        Lancamento lancamento = criarEPersistirLancamento();

        Optional<Lancamento> lancamentoEncontrado = lancamentoRepository.findById(lancamento.getId());

        assertThat(lancamentoEncontrado.isPresent()).isTrue();
    }

    private Lancamento criarLancamento(){
        return Lancamento.builder()
                .ano(2019)
                .mes(1)
                .descricao("lancamento qualquer")
                .valor(BigDecimal.valueOf(10))
                .tipo(TipoLancamento.RECEITA)
                .status(StatusLancamento.PENDENTE)
                .dataCadastro(LocalDate.now())
                .build();
    }

    private Lancamento criarEPersistirLancamento(){
        Lancamento lancamento = criarLancamento();
        entityManager.persist(lancamento);
        return  lancamento;
    }
}