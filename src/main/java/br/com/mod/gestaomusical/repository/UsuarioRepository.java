package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByPerfilUsuarioNomeIgnoreCaseAndPerfilUsuarioAtivoTrueAndAtivoTrue(
            String nomePerfil
    );
}