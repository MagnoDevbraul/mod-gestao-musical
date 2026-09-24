package br.com.mod.gestaomusical.repository;

import br.com.mod.gestaomusical.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissaoRepository
        extends JpaRepository<Permissao, Long> {

    List<Permissao> findAllByOrderByNomeAsc();
}