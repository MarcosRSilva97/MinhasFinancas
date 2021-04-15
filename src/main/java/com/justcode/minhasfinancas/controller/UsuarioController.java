package com.justcode.minhasfinancas.controller;


import com.justcode.minhasfinancas.dto.UsuarioDTO;
import com.justcode.minhasfinancas.exceptions.ErroAutenticacao;
import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Usuario;
import com.justcode.minhasfinancas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/criarUsuario")
    public ResponseEntity salvar(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = Usuario.builder()
                .nome(usuarioDTO.getNome())
                .email(usuarioDTO.getEmail())
                .senha(usuarioDTO.getSenha())
                .build();

        System.out.println(usuarioDTO.getNome());
        System.out.println(usuarioDTO.getEmail());
        System.out.println(usuarioDTO.getSenha());

        try{
            Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        }
        catch (RegraNegocioException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/autenticar")
    public ResponseEntity autenticar (@RequestBody UsuarioDTO usuarioDTO) {
        try {
            Usuario usuarioAutenticado = usuarioService.autenticar(usuarioDTO.getEmail(), usuarioDTO.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }
}