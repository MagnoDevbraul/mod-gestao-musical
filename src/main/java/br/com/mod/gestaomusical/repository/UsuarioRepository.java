package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByPerfilUsuarioNomeIgnoreCaseAndPerfilUsuarioAtivoTrueAndAtivoTrue(
            String nomePerfil
    );

    @EntityGraph(attributePaths = "perfilUsuario")
    Optional<Usuario> findByEmailIgnoreCase(String email);
}