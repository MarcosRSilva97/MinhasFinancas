package com.justcode.minhasfinancas.model.repository;

import com.justcode.minhasfinancas.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, Long> {

    //Optional<Usuario> findByEmail(String email) neste caso retorna todo o usuário

    boolean existsByEmail(String email);

    Optional<Usuario> findByEmail(String email);




}