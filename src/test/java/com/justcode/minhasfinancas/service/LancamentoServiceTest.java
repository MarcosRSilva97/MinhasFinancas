package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;
import com.justcode.minhasfinancas.model.repository.LancamentoRepository;
import com.justcode.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.justcode.minhasfinancas.service.implementacao.LancamentoServiceImplementacao;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith (SpringRunner.class)
@ActiveProfiles ("test")
public class LancamentoServiceTest {

    @SpyBean
    LancamentoServiceImplementacao lancamentoServiceImplementacao;

    @MockBean
    LancamentoRepository lancamentoRepository;

    @Test
    public void deveSalvarUmLancamento () {
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doNothing().when(lancamentoServiceImplementacao).validarLancamento(lancamentoASalvar);

        Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
        lancamentoSalvo.setId(1l);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = lancamentoServiceImplementacao.salvarLancamento(lancamentoASalvar);

        //validação
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

    }


}
