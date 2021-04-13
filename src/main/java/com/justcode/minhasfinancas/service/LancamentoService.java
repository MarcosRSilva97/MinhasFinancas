package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;

import java.util.List;

public interface LancamentoService {

    Lancamento salvarLancamento  (Lancamento lancamento);

    Lancamento atualizarLancamento (Lancamento lancamento);

    List<Lancamento> buscarLancamento (Lancamento lancamentoFiltro);

    void deletarLancamento (Lancamento lancamento);

    void atualizarStatus (Lancamento lancamento, StatusLancamento statusLancamento);

    void validarLancamento (Lancamento lancamento);
}
