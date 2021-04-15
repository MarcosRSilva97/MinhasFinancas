package com.justcode.minhasfinancas.service.implementacao;

import com.justcode.minhasfinancas.exceptions.ErroAutenticacao;
import com.justcode.minhasfinancas.exceptions.RegraNegocioException;
import com.justcode.minhasfinancas.model.entity.Usuario;
import com.justcode.minhasfinancas.model.repository.UsuarioRepository;
import com.justcode.minhasfinancas.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioServiceImplementacao implements UsuarioService {

    private UsuarioRepository usuarioRepository;


    public UsuarioServiceImplementacao(UsuarioRepository usuarioRepository) {
        super();
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (!usuario.isPresent()) {
            throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
        }

        if (!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Email ou senha incorretos.");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return usuarioRepository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean existe = usuarioRepository.existsByEmail(email);
        if(existe){
            throw  new RegraNegocioException("Já existe um usuário cadastrado com este email.");
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {

        return usuarioRepository.findById(id);
    }


}