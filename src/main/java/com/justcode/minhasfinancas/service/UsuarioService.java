package com.justcode.minhasfinancas.service;

import com.justcode.minhasfinancas.model.entity.Usuario;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario (Usuario usuario);

   void validarEmail(String email);

}
