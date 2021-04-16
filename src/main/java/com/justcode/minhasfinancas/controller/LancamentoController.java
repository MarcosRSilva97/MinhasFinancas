package com.justcode.minhasfinancas.controller;


import com.justcode.minhasfinancas.dto.LancamentoDTO;
import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Lancamento;
import com.justcode.minhasfinancas.model.entity.Usuario;
import com.justcode.minhasfinancas.model.enums.StatusLancamento;
import com.justcode.minhasfinancas.model.enums.TipoLancamento;
import com.justcode.minhasfinancas.service.LancamentoService;
import com.justcode.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoService lancamentoService;
    private final UsuarioService usuarioService;

    private Lancamento converterLancamento (LancamentoDTO lancamentoDTO){
        Lancamento lancamento = new Lancamento();
        lancamento.setId(lancamentoDTO.getIdUsuario());
        lancamento.setDescricao(lancamentoDTO.getDescricao());
        lancamento.setAno(lancamentoDTO.getAno());
        lancamento.setMes(lancamentoDTO.getMes());
        lancamento.setValor(lancamentoDTO.getValor());

        Usuario usuario = usuarioService.buscarPorId(lancamentoDTO.getIdUsuario())
                .orElseThrow(( )-> new RegraNegocioException("Usuário não encontrado para o Id informado."));

        lancamento.setUsuario(usuario);

        if (lancamentoDTO.getTipo() != null) {
            lancamento.setTipo(TipoLancamento.valueOf(lancamentoDTO.getTipo()));
        }

        if (lancamentoDTO.getStatus() != null){
            lancamento.setStatus(StatusLancamento.valueOf(lancamentoDTO.getStatus()));
        }


        return  lancamento;
    }

    @PostMapping
    @RequestMapping("/salvarLancamento")
    public ResponseEntity salvar (@RequestBody LancamentoDTO dto){
        try {
            Lancamento entidadeLancamento = converterLancamento(dto);
            entidadeLancamento = lancamentoService.salvarLancamento(entidadeLancamento);
            return  new ResponseEntity(entidadeLancamento, HttpStatus.CREATED);
        }
        catch (RegraNegocioException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/atualizarLancamento/{id}")
    @Transactional
    public ResponseEntity atualizar (@PathVariable("id") Long id, @RequestBody LancamentoDTO lancamentoDTO){
        return lancamentoService.buscarPorId(id).map(entity -> {
            try{
                Lancamento lancamento = converterLancamento(lancamentoDTO);
                lancamento.setId(entity.getId());
                lancamentoService.atualizarLancamento(lancamento);
                return ResponseEntity.ok(lancamento);
            }
            catch (RegraNegocioException exception){
                return ResponseEntity.badRequest().body(exception.getMessage());
            }

        }).orElseGet(() -> new ResponseEntity<>("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/deletarLancamento/{id}")
    @Transactional
    public ResponseEntity deletar (@PathVariable("id") Long id){
        return lancamentoService.buscarPorId(id).map(entidade ->{
            lancamentoService.deletarLancamento(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }).orElseGet(() -> new ResponseEntity<>("Lançamento não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
    }

    @GetMapping ("/buscarLancamento")
    public ResponseEntity buscarLancamento(
            @RequestParam (value = "descricao", required = false) String descricao,
            @RequestParam (value = "mes", required = false) Integer mes,
            @RequestParam (value = "ano", required = false) Integer ano,
            @RequestParam (value = "usuario") Long idUsuario
    ){
        Lancamento lancamentoFiltro = new Lancamento();
        lancamentoFiltro.setDescricao(descricao);
        lancamentoFiltro.setMes(mes);
        lancamentoFiltro.setAno(ano);

        Optional<Usuario> usuario = usuarioService.buscarPorId(idUsuario);
        if (!usuario.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realiza a consulta. Usuário não encontrado para o Id informado");
        }else {
            lancamentoFiltro.setUsuario(usuario.get());
        }

        List<Lancamento> lancamentos = lancamentoService.buscarLancamento(lancamentoFiltro);
        return  ResponseEntity.ok(lancamentos);

    }

    @PutMapping("/atualizarStatus/{id}")
    public ResponseEntity atualizarStatus(){

        return null;
    }
}
