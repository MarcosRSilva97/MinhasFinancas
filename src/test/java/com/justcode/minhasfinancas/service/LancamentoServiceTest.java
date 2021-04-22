package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.entity.Usuario;
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
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        lancamentoSalvo.setId(1L);
        lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
        Mockito.when(lancamentoRepository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);

        //execução
        Lancamento lancamento = lancamentoServiceImplementacao.salvarLancamento(lancamentoASalvar);

        //validação
        assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
        assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);

    }

    @Test
    public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao(){
        //cenário
        Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
        Mockito.doThrow(RegraNegocioException.class).when(lancamentoServiceImplementacao).validarLancamento(lancamentoASalvar);

        //execucao
        catchThrowableOfType(() -> lancamentoServiceImplementacao.salvarLancamento(lancamentoASalvar), RegraNegocioException.class);

        //verificacao
        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoASalvar);

    }

    @Test
    public void deveAtualizarUmLancamento(){
        //cenario
        Lancamento lancamentoParaAtualizar = LancamentoRepositoryTest.criarLancamento();
        lancamentoParaAtualizar.setId(1L);
        lancamentoParaAtualizar.setStatus(StatusLancamento.PENDENTE);

        Mockito.doNothing().when(lancamentoServiceImplementacao).validarLancamento(lancamentoParaAtualizar);
        Mockito.when(lancamentoRepository.save(lancamentoParaAtualizar)).thenReturn(lancamentoParaAtualizar);

        //execucao
        lancamentoServiceImplementacao.atualizarLancamento(lancamentoParaAtualizar);

        //validacao
        Mockito.verify(lancamentoRepository, Mockito.times(1)).save(lancamentoParaAtualizar);
    }

    //Entender o porque o id está preenchido

    /*
    @Test
    public void deveLancarErroAoTentarAtualizarLancamentoQueAindaNaoFoiSalvo(){
        //cenario
        Lancamento lancamentoParaAtualizar = LancamentoRepositoryTest.criarLancamento();

        //execucao
        catchThrowableOfType(() ->lancamentoServiceImplementacao.atualizarLancamento(lancamentoParaAtualizar), NullPointerException.class);

        //validacao
        Mockito.verify(lancamentoRepository, Mockito.never()).save(lancamentoParaAtualizar);
    } */

    @Test
    public void  deveDeletarUmLancamento (){
        //cenário
        Lancamento lancamentoParaDeletar = LancamentoRepositoryTest.criarLancamento();
        lancamentoParaDeletar.setId(1L);

        //execução
        lancamentoServiceImplementacao.deletarLancamento(lancamentoParaDeletar);

        //verificação
        Mockito.verify(lancamentoRepository).delete(lancamentoParaDeletar);
    }
    //Entender o porque o id está preenchido

    /*
    @Test
    public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo(){
        //cenário
        Lancamento lancamentoParaDeletar = LancamentoRepositoryTest.criarLancamento();

        //execucao
        catchThrowableOfType(() -> lancamentoServiceImplementacao.deletarLancamento(lancamentoParaDeletar), NullPointerException.class);

        //verificacao
        Mockito.verify(lancamentoRepository, Mockito.never()).delete(lancamentoParaDeletar);
    } */

    @Test
    public void deveFiltarLancamentos(){
        //cenário
        Lancamento lancamentoFiltrado = LancamentoRepositoryTest.criarLancamento();
        lancamentoFiltrado.setId(1L);

        List<Lancamento> lista = Arrays.asList(lancamentoFiltrado);
        Mockito.when(lancamentoRepository.findAll(Mockito.any(Example.class))).thenReturn(lista);

        //execução
        List<Lancamento> resultado = lancamentoServiceImplementacao.buscarLancamento(lancamentoFiltrado);

        //validação
        assertThat(resultado)
                .isNotEmpty()
                .hasSize(1)
                .contains(lancamentoFiltrado);
    }

    @Test
    public void deveAtualizarOStatusDeUmLancamento(){
        //cenário
        Lancamento lancamentoParaAtualizarStatus = LancamentoRepositoryTest.criarLancamento();
        lancamentoParaAtualizarStatus.setId(1L);

        StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
        Mockito.doReturn(lancamentoParaAtualizarStatus).when(lancamentoServiceImplementacao).atualizarLancamento(lancamentoParaAtualizarStatus);

        //execução
        lancamentoServiceImplementacao.atualizarStatus(lancamentoParaAtualizarStatus, novoStatus);

        //validação
        assertThat(lancamentoParaAtualizarStatus.getStatus()).isEqualTo(novoStatus);
        Mockito.verify(lancamentoServiceImplementacao).atualizarLancamento(lancamentoParaAtualizarStatus);
    }

    @Test
    public void devePesquisarUmLancamentoPorId(){
        //cenário
        Long id = 1L;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.of(lancamento));

        //execução
        Optional<Lancamento> resultado = lancamentoServiceImplementacao.buscarPorId(id);

        //verificação
        assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    public void deveRetornarVazioQuandoOLancamentoNaoExiste(){
        //cenário
        Long id = 1L;

        Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
        lancamento.setId(id);

        Mockito.when(lancamentoRepository.findById(id)).thenReturn(Optional.empty());

        //execução
        Optional<Lancamento> resultado = lancamentoServiceImplementacao.buscarPorId(id);

        //verificação
        assertThat(resultado.isPresent()).isFalse();
    }

    @Test
    public void deveLancarErroAoValidarLancamento(){
        Lancamento lancamento = new Lancamento();

        Throwable erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("");

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));
        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida.");

        lancamento.setDescricao("Salário");

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(0);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(13);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido.");

        lancamento.setMes(2);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(208);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(20201);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido.");

        lancamento.setAno(2020);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");

        lancamento.setUsuario(new Usuario());
        //falta pegar o cenário onde não encontra o ID
        lancamento.getUsuario().setId(1L);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.ZERO);

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");

        lancamento.setValor(BigDecimal.valueOf(1));

        erro = catchThrowable(() -> lancamentoServiceImplementacao.validarLancamento(lancamento));

        assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um tipo de Lançamento.");
    }
}