package com.justcode.minhasfinancas.service.implementacao;

import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;
import com.justcode.minhasfinancas.model.repository.LancamentoRepository;
import com.justcode.minhasfinancas.service.LancamentoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LancamentoServiceImplementacao implements LancamentoService {

    private LancamentoRepository lancamentoRepository;

    public LancamentoServiceImplementacao(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Override
    @Transactional
    public Lancamento salvarLancamento(Lancamento lancamento) {
        validarLancamento(lancamento);
        lancamento.setStatus(StatusLancamento.PENDENTE);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional
    public Lancamento atualizarLancamento(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        validarLancamento(lancamento);
        return lancamentoRepository.save(lancamento);
    }

    @Override
    @Transactional (readOnly = true)
    public List<Lancamento> buscarLancamento(Lancamento lancamentoFiltro) {
        Example example = Example.of(lancamentoFiltro, ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return lancamentoRepository.findAll(example);
    }

    @Override
    @Transactional
    public void deletarLancamento(Lancamento lancamento) {
        Objects.requireNonNull(lancamento.getId());
        lancamentoRepository.delete(lancamento);
    }

    @Override
    @Transactional
    public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
        lancamento.setStatus(statusLancamento);
        atualizarLancamento(lancamento);
    }

    @Override
    public void validarLancamento(Lancamento lancamento) {
        if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")){
            throw new RegraNegocioException("Informe uma Descrição válida.");
        }

        if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12){
            throw new RegraNegocioException("Informe um Mês válido.");
        }

        if (lancamento.getAno() == null || lancamento.getAno().toString().length() != 4){
            throw new RegraNegocioException("Informe um Ano válido.");
        }

        if (lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null){
            throw new RegraNegocioException("Informe um Usuário.");
        }

        if (lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1){
            throw new RegraNegocioException("Informe um Valor válido.");
        }

        if (lancamento.getTipo() == null ){
            throw new RegraNegocioException("Informe um tipo de Lançamento.");
        }
    }

    @Override
    public Optional<Lancamento> buscarPorId(Long id) {
        return lancamentoRepository.findById(id);
    }
}
