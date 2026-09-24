package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.PerfilUsuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilUsuarioRepository
        extends JpaRepository<PerfilUsuario, Long> {

    @EntityGraph(attributePaths = "permissoes")
    Optional<PerfilUsuario> findComPermissoesById(Long id);
}