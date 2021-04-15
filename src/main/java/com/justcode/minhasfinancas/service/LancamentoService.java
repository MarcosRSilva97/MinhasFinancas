package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;

import java.util.List;
import java.util.Optional;

public interface LancamentoService {

    Lancamento salvarLancamento  (Lancamento lancamento);

    Lancamento atualizarLancamento (Lancamento lancamento);

    List<Lancamento> buscarLancamento (Lancamento lancamentoFiltro);

    void deletarLancamento (Lancamento lancamento);

    void atualizarStatus (Lancamento lancamento, StatusLancamento statusLancamento);

    void validarLancamento (Lancamento lancamento);

    Optional<Lancamento> buscarPorId (Long id);
}
